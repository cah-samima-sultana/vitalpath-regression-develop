package com.cardinalhealth.vitalpath.cabinet.specs.inventory

import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryAddPage
import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryHomePage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import com.cardinalhealth.vitalpath.utils.InventorySearchType

class AddSiteOwnedInventorySpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Cancel adding site-owned inventory"() {
        when: "I go to the Inventory page"
            gotoInventoryPage()
            at InventoryAddPage

        and: "I click the add button"
            clickAddButton()

        and: "I can cancel adding a drug"
            adjustment.clickCancelButton()
            waitForTapToCloseOverlayToGoAway()

        then:
            at InventoryHomePage
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Test Adjustment Widget for site-owned inventory"() {
        when: "I am at the inventory add page"
            gotoInventory()
            at InventoryAddPage

        and: "I click the add button"
            clickAddButton()

        and: "I check the Inventory Add module works correctly"
            testSingleDose()

        then:
            at InventoryHomePage

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Verify Reason Codes"() {
        when: "I am at the inventory add page"
        gotoInventory()
        at InventoryAddPage

        and: "I select to add an item to inventory"
        clickAddButton()

        and: "I search and select a drug"
        def drug = drugService.findNonItemDrug()
        searchForDrug(drug.id)

        then: "The valid list of reasons are in the reason dropdown"
        adjustment.openReasonDropdown()
        $(".ember-power-select-option").size() == 14
        adjustment.verifyReason("Restock Cardinal")
        adjustment.verifyReason("Restock Non-Cardinal")
        adjustment.verifyReason("Broken")
        adjustment.verifyReason("Expired")
        adjustment.verifyReason("Inventory Adj")
        adjustment.verifyReason("Item Removed")
        adjustment.verifyReason("Off Label Adj")
        adjustment.verifyReason("Other")
        adjustment.verifyReason("Patient Assist Adj")
        adjustment.verifyReason("Pharmacy")
        adjustment.verifyReason("Transfer In")
        adjustment.verifyReason("Transfer Out")
        adjustment.verifyReason("Volume Adj")
        adjustment.verifyReason("Whitebag Adj")
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Search and Select Drug Not in Formulary"() {
        when: "I am at the inventory add page"
        gotoInventory()
        at InventoryAddPage

        and: "I select to add an item to inventory"
        clickAddButton()

        and: "I search and select a drug that is not in the formulary"
        def drug = drugService.findNonItemDrug()
        searchForDrug(drug.id)

        then: "I verify ndc"
        verifyNdc(drug.id)

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Add Item Not in Formulary to Cabinet"() {
        when: "I am at the inventory add page"
        gotoInventory()
        at InventoryAddPage

        and: "I select to add an item to inventory"
        clickAddButton()

        and: "I select and add inventory for a drug that is not in the formulary"
        def drug = drugService.findNonItemDrug()
        testNonItemDrug(drug)

        then:
        at InventoryHomePage

        when: "I search for the item I just added"
        searchInventory(InventorySearchType.NDC, drug.id)

        then: "The inventory drug card displays"
        verifySearchResult(drug.id)

    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Search and add inventory for Inactive Item"() {
        given: "I make 00409-0183-01 item inactive"
        def item = itemService.updateItem(ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc(), null, false)
        assert !item.active

        and: "I delete any existing inventory"
        def invLocation= inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), Session.loggedInSiteId)
        inventoryAdjustmentService.deleteInventory(InventorySegment.SITE_OWNED.id(), ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc(), invLocation, null, null)

        when: "I am at the inventory add page"
        refreshPage()
        goToInventoryPage()
        at InventoryAddPage

        and: "I select to add an item to inventory"
        clickAddButton()

        and: "I search and select a drug that is not in the formulary"
        searchForDrug(ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc())

        then: "I verify ndc"
        verifyNdc(ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc())

        and: "I select location"
        selectLocation(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1)

        and: "The reason dropdown defaults to 'Select a Reason'"
        adjustment.find("[data-id=adj-comp-prompt-for-reason-select]").text().trim() == "Select a Reason"

        and: "I cannot save until I select a reason"
        adjustment.saveButton.hasClass("isDisabled")

        and: "I select reason code"
        selectReasonCode("Restock Cardinal")

        and: "I save the inventory"
        clickSaveButton()

        then:
        at InventoryHomePage

        when: "I search for the item I just added"
        searchInventory(InventorySearchType.NDC, ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc())

        and: "The inventory drug card displays"
        verifySearchResult(ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc())

        then: "I verify this item is made active"
        def updatedItem = itemService.findItemByNDC(ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc())
        assert updatedItem.active

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Search a non drug"() {
        when: "I am at the inventory add page"
        gotoInventory()
        at InventoryAddPage

        and: "I select to add an item to inventory"
        clickAddButton()

        and: "I search and select a drug that is not in the formulary"
        searchForDrug(ItemConstants.THERMAL_PAPER.getName())

        then: "I verify ndc"
        verifyDrugItemSelectedName(ItemConstants.THERMAL_PAPER.getName())

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Type should default to Samples for a sample drug"() {
        given: "I mark 00004-0086-94 as a sample drug"
        drugService.updateDrug("00004-0086-94", true)
        when: "I am at the inventory add page"
        refreshPage()
        waitForAnimationToComplete()
        gotoInventory()
        at InventoryAddPage

        and: "I select to add an item to inventory"
        clickAddButton()

        and: "I search and select a sample drug"
        searchForDrug("00004-0086-94")

        and: "I verify type is Samples"
        verifyInventorySegmentType(InventorySegment.SAMPLES.title())

        then: "I verify type is disabled"
        verifyInventorySegmentTypeIsDisabled()

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Type should default to Site Owned for a non sample drug"() {
        when: "I am at the inventory add page"
        gotoInventory()
        at InventoryAddPage

        and: "I select to add an item to inventory"
        clickAddButton()

        and: "I search and select a sample drug"
        searchForDrug(ItemConstants.GEMZAR_200mg_00409_0183_01.getNdc())

        and: "I verify type is Site Owned"
        verifyInventorySegmentType(InventorySegment.SITE_OWNED.title())

        then: "I verify type is not disabled"
        verifyInventorySegmentTypeIsDisabled() == false

    }
}
