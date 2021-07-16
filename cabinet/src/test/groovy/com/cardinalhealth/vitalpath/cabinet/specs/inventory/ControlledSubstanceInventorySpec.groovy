package com.cardinalhealth.vitalpath.cabinet.specs.inventory

import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryAddPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class ControlledSubstanceInventorySpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.TRUE)
            ])
    def "Add controlled substance to inventory w/ controlled substance turned on"() {
        given: "I am logged on to Site 1/Cabinet A"
        at DispenseHomePage

        when: "I go to inventory"
        gotoInventory()

        then: "I click the add button"
        clickAddButton()

        and: "I arrive at the add inventory page"
        at InventoryAddPage

        when: "I select a drug"
        searchForDrug(ItemConstants.DECADRON_100mg_63323_0516_10.ndc)

        and: "I select a location"
        searchForLocation(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1)

        then: "The controlled substance approval is shown"
        approverAuthentication.isShown() == true

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Add controlled substance to inventory w/ controlled substance turned off"() {
        given: "I am logged on to Site 1/Cabinet A"
        at DispenseHomePage

        when: "I go to inventory"
        gotoInventory()

        then: "I click the add button"
        clickAddButton()

        and: "I arrive at the add inventory page"
        at InventoryAddPage

        when: "I select a drug"
        searchForDrug(ItemConstants.DECADRON_100mg_63323_0516_10.ndc)

        and: "I select a location"
        searchForLocation(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1)

        then: "The controlled substance approval is not shown"
        approverAuthentication.isShown() == false
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.TRUE)
            ])
    def "Add non controlled substance to inventory w/ controlled substance turned on"() {
        given: "I am logged on to Site 1/Cabinet A"
        at DispenseHomePage

        when: "I go to inventory"
        gotoInventory()

        then: "I click the add button"
        clickAddButton()

        and: "I arrive at the add inventory page"
        at InventoryAddPage

        when: "I select a drug"
        searchForDrug(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc)

        and: "I select a location"
        searchForLocation(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1)

        then: "The controlled substance approval is shown"
        approverAuthentication.isShown() == true

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "Add non controlled substance to inventory w/ controlled substance turned off"() {
        given: "I am logged on to Site 1/Cabinet A"
        at DispenseHomePage

        when: "I go to inventory"
        gotoInventory()

        then: "I click the add button"
        clickAddButton()

        and: "I arrive at the add inventory page"
        at InventoryAddPage

        when: "I select a drug"
        searchForDrug(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc)

        and: "I select a location"
        searchForLocation(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1)

        then: "The controlled substance approval is not shown"
        approverAuthentication.isShown() == false
    }

}