package com.cardinalhealth.vitalpath.cabinet.specs.inventory

import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryHomePage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import com.cardinalhealth.vitalpath.utils.InventorySearchType

class EditSiteOwnedInventorySpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Adjustment Widget focus and tab tests"() {
        when: "I go to inventory"
        gotoInventory()

        then:"I am at inventory page"
        at InventoryHomePage

        and: "I search by All with lot number"
        searchInventory(InventorySearchType.ALL, "123456")

        then:
        verifySearchResult("123456")

        and: "I click the edit button"
        editInventoryByItemAndSegment(ItemConstants.GEMZAR_1000mg_00069_3858_10, InventorySegment.SITE_OWNED)

        and: "I see the focus is on quantity field"
        verifyQtyIsFocused()

        and: "I click tab"
        tabClick()

        and: "I see the focus is on notes field"
        verifyReasonCodeIsFocused()

        and: "I click tab"
        tabClick()

        and: "I see the focus is on notes field"
        verifyNotesIsFocused()

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Adjustment Widget shift tab tests"() {
        when: "I go to inventory"
        gotoInventory()

        then:"I am at inventory page"
        at InventoryHomePage

        and: "I search by All with lot number"
        searchInventory(InventorySearchType.ALL, "123456")

        then:
        verifySearchResult("123456")

        and: "I click the edit button"
        editInventoryByItemAndSegment(ItemConstants.GEMZAR_1000mg_00069_3858_10, InventorySegment.SITE_OWNED)

        and: "I see the focus is on quantity field"
        verifyQtyIsFocused()

        and: "I click shift tab from quantity box"
        shiftTabClick()

        and: "I see the focus is on lot number field"
        verifyExpirationIsFocused()

        and: "I click shift tab from lot number"
        shiftTabClick()

        and: "I see the focus is on expiration date field"
        verifyLotIsFocused()
    }

}
