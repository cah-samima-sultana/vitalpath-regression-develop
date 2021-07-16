package com.cardinalhealth.vitalpath.cabinet.specs.inventory

import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryPatientPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import com.cardinalhealth.vitalpath.utils.InventorySearchType

class InventorySearchSpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Search Inventory can search by #testDescription"() {

        when: "I go to inventory"
        gotoInventory()

        and: "I see the search filter is sorted alphabetically"
        verifySearchByOrder("All", "Patient Name/MRN")

        and: "I search by All"
        searchInventory(InventorySearchType.ALL, "gemcitabine")

        then:
        verifySearchResult("gemcitabine")

        and: "I search by All with lot number"
        searchInventory(InventorySearchType.ALL, "123456")

        then:
        verifySearchResult("123456")

        when: "I search by Item Name"
        searchInventory(InventorySearchType.ITEM_NAME, "gemzar")

        then:
        verifySearchResult("gemzar")

        when: "I search by Inventory Location"
        searchInventory(InventorySearchType.INV_LOCATION, "Door 2")

        then:
        verifySearchResult("Door 2")

        when: "I search by NDC"
        searchInventory(InventorySearchType.NDC, "00409-0183-01")

        then:
        verifySearchResult("00409-0183-01")

        when: "I search by Lot Number"
        searchInventory(InventorySearchType.LOT_NUMBER, "123456")

        then:
        verifySearchResult("123456")

        when: "I go to patient inventory"
        clickPatientButton()
        at InventoryPatientPage

        def patient = patientService.makePatient()

        and: "I select a patient"
        searchAndSelectPatientByName(patient)

        and: "I add a drug"
        waitForAnimationToComplete()
        addPatientDrug(ItemConstants.GEMZAR_200mg_00002_7501_01, InventorySegment.PATIENT_ASSIST, 100, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2, false)

        and: "I go back to the inventory home page"
        gotoInventory()
        at InventoryHomePage

        and: "I search by Patient Name"
        searchInventory(InventorySearchType.PATIENT_NAME_MRN, patient.lastName)

        then:
        verifySearchResult(patient.lastName)

        when: "I search by Patient MRN"
        searchInventory(InventorySearchType.PATIENT_NAME_MRN, patient.accountNumber)

        then:
        verifySearchResult(patient.accountNumber)

        when: "I search by Inventory Segment"
        searchInventory(InventorySearchType.INVENTORY_SEGMENT, "patient assist")

        then:
        verifySearchResult("patient assist")
    }
}
