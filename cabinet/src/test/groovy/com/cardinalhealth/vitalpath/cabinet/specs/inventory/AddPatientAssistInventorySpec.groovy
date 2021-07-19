package com.cardinalhealth.vitalpath.cabinet.specs.inventory

import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryPatientPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class AddPatientAssistInventorySpec extends BaseSpec {

    def patient

    def setup() {
        patient = patientService.makePatient()
        refreshPage()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.DISPENSE_DOOR_SELECT, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Add and delete patient assist inventory"(){
        when: "I go to the inventory page"
       // browser.$("inventoryHeaderLink").click()
        //click on inventoryHeaderLink
        goToInventory()
        print "I am here on the top "
        and: "I click the patient button"
        clickPatientButton()
        at InventoryPatientPage

        and: "I select a patient"
        searchAndSelectPatientByName(patient)bg

        and: "Verify no inventory currently exists"
        verifyNoPatientInventory(patient)
        //click add id: icon-left
        and: "I add patient assist inventory for the selected patient"
        clickPlusButton()
        synchronized (available) {
            available.wait()
        }
        sleep(5000)
       waitForAnimationToComplete()

        addPatientDrug(ItemConstants.ARANESP_300mcg_55513_0111_01, InventorySegment.PATIENT_ASSIST, 100, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2, true)
        then: "I verify that the patient assist inventory was added correctly"
        verifyPatientInventory(patient, ItemConstants.ARANESP_300mcg_55513_0111_01, InventorySegment.PATIENT_ASSIST, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2, "100")

    }
}