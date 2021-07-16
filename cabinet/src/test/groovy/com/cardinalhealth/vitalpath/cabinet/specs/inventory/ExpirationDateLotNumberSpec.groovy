package com.cardinalhealth.vitalpath.cabinet.specs.inventory

import com.cardinalhealth.vitalpath.cabinet.modules.components.EditInventoryCardModule
import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryAddPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class ExpirationDateLotNumberSpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)])
    def "After creating inventory with lot number/exp date, creating new (matching) inventory does not overwrite if blank, overrides if present"() {
        when: "I go to the Inventory page"
            gotoInventory()
            at InventoryAddPage

        and: "I click the add button"
            clickAddButton()
            def ndc = ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.getNdc()
            def invSegType = InventorySegment.SITE_OWNED
            def invLocation = PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2
            
        
        and: "Add inventory with a lot number and expiration date"
            adjustment.searchForDrug(ndc)
            adjustment.selectDrugType(invSegType)
            adjustment.selectLocation(invLocation)
            adjustment.inputLotNumber("abc123")
            // need to wait for network request for most recent expiration date with that lot number to come back first before entering one
            adjustment.expirationDateLabel.click()
            sleep(2000)
            adjustment.inputExpirationDate("2017/07/01")
            adjustment.selectReasonCode("Restock Cardinal")
            adjustment.clickSaveButton()

        then: "The inventory is created"
            at InventoryAddPage
            searchForInventory(ndc)
            waitFor { $(".scrollBoxCard").size() > 0 }
            def card = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null)
            card.matchExpirationDate("2017-07-01")
            card.matchLotNumber("abc123")

        and: "I click the add button"
            clickAddButton()

        and: "Add the same inventory with no lot number or expiration date"
            adjustment.searchForDrug(ndc)
            adjustment.selectDrugType(invSegType)
            adjustment.selectLocation(invLocation)
            adjustment.selectReasonCode("Restock Cardinal")
            adjustment.clickSaveButton()

        then: "The inventory is created"
            at InventoryAddPage
            searchForInventory(ndc)
            waitFor { $(".scrollBoxCard").size() > 0 }
            def card2 = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null)
            card2.matchExpirationDate("2017-07-01")
            card2.matchLotNumber("abc123")

        and: "I click the add button"
            clickAddButton()

        and: "Add the same inventory with a new lot number or expiration date"
            adjustment.searchForDrug(ndc)
            adjustment.selectDrugType(invSegType)
            adjustment.selectLocation(invLocation)
            adjustment.inputLotNumber("def456")
            // need to wait for network request for most recent expiration date with that lot number to come back first before entering one
            adjustment.expirationDateLabel.click()
            sleep(2000)
            adjustment.inputExpirationDate("2015/10/10")
            adjustment.selectReasonCode("Restock Cardinal")
            adjustment.clickSaveButton()

        then: "The inventory possesses the updated lot number/expiration date"
            at InventoryAddPage
            searchForInventory(ndc)
            waitFor { $(".scrollBoxCard").size() > 0 }
            def card3 = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null)
            card3.matchExpirationDate("2015-10-10")
            card3.matchLotNumber("def456")
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)])
    def "Editing inventory allows override lot number"() {
        when: "I go to the Inventory page"
            gotoInventory()
            at InventoryAddPage

        and: "I click the add button"
            clickAddButton()
            def ndc = ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.getNdc()
            def invSegType = InventorySegment.SITE_OWNED
            def invLocation = PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2

        and: "Add inventory with a lot number and expiration date"
            adjustment.searchForDrug(ndc)
            adjustment.selectDrugType(invSegType)
            adjustment.selectLocation(invLocation)
            adjustment.inputLotNumber("abc123")
            // need to wait for network request for most recent expiration date with that lot number to come back first before entering one
            adjustment.expirationDateLabel.click()
            sleep(2000)
            adjustment.inputExpirationDate("2017/01/01")
            adjustment.selectReasonCode("Restock Cardinal")
            adjustment.clickSaveButton()

        then: "The inventory is created"
            at InventoryAddPage
            searchForInventory(ndc)
            waitFor { $(".scrollBoxCard").size() > 0 }
            def inventoryInfo = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null)
            inventoryInfo.matchExpirationDate("2017-01-01")
            inventoryInfo.matchLotNumber("abc123")
        
        then: "I edit the inventory"
            def inventoryCard = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null).module(EditInventoryCardModule.class)
            inventoryCard.clickEditButton()
            adjustment.inputLotNumber("def456")
            // need to wait for network request for most recent expiration date with that lot number to come back first before entering one
            adjustment.expirationDateLabel.click()
            sleep(2000)
            adjustment.inputExpirationDate("2017/02/02")
            adjustment.selectReasonCode("Restock Cardinal")
            adjustment.clickSaveButton()

        then: "The inventory is modified"
            at InventoryAddPage
            searchForInventory(ndc)
            waitFor { $(".scrollBoxCard").size() > 0 }
            def card = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null)
            card.matchExpirationDate("2017-02-02")
            card.matchLotNumber("def456")
    }

// TODO: Uncomment this code out when lot number and exp date overwriting issue is fixed
//    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
//            siteSettings=[@Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)])
//    def "Editing inventory allows lot number/expiration date to be cleared"() {
//        when: "I go to the Inventory page"
//            gotoInventory()
//            at InventoryAddPage
//
//        and: "I click the add button"
//            clickAddButton()
//            def ndc = ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.getNdc()
//            def invSegType = InventorySegment.SITE_OWNED
//            def invLocation = PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2
//
//        and: "Add inventory with a lot number and expiration date"
//            adjustment.searchForDrug(ndc)
//            adjustment.selectDrugType(invSegType)
//            adjustment.selectLocation(invLocation)
//            adjustment.inputLotNumber("abc123")
//            // need to wait for network request for most recent expiration date with that lot number to come back first before entering one
//            adjustment.expirationDateLabel.click()
//            sleep(2000)
//            adjustment.inputExpirationDate("2017/01/01")
//            adjustment.selectReasonCode("Restock Cardinal")
//            adjustment.clickSaveButton()
//
//        then: "The inventory is created"
//            at InventoryAddPage
//            searchForInventory(ndc)
//            waitFor { $(".scrollBoxCard").size() > 0 }
//            def inventoryInfo = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null)
//            inventoryInfo.matchExpirationDate("2017-01-01")
//            inventoryInfo.matchLotNumber("abc123")
//
//        then: "I edit the inventory"
//            def inventoryCard = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null).module(EditInventoryCardModule.class)
//            inventoryCard.clickEditButton()
//            adjustment.inputLotNumber("")
//            // need to wait for network request for most recent expiration date with that lot number to come back first before entering one
//            adjustment.expirationDateLabel.click()
//            sleep(2000)
//            adjustment.inputExpirationDate("")
//            adjustment.selectReasonCode("Restock Cardinal")
//            adjustment.clickSaveButton()
//
//        then: "The inventory is saved with no lot number or expiration date"
//            at InventoryAddPage
//            searchForInventory(ndc)
//            waitFor { $(".scrollBoxCard").size() > 0 }
//            def card = findInventoryInfo(ndc, invLocation.displayPath(), invSegType.title(), null)
//            card.matchExpirationDate(null)
//            card.matchLotNumber(null)
//    }
}
