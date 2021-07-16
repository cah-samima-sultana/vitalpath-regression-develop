package com.cardinalhealth.vitalpath.cabinet.specs.dispense

import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class PlannedFilterSpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE)])
    def "I can see that the planned filter setting is on"(){

        given: "I have navigated to the Dispense Home Page"
            to DispenseHomePage

        and: "The main page planned filter is enabled"
            verifyMainPlannedFilterButtonIsSelected()

        when: "I click the right menu button"
            clickMenuButton()

        then: "I can see the planned filter is selected"
            verifyPlannedFilterIsSelected()
        and: "I see the planned filter checkbox exists"
            verifyPlannedFilterCheckboxExists()
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.FALSE)])
    def "I can see that the planned filter setting is off"(){

        given: "I have navigated to the Dispense Home Page"
            to DispenseHomePage

        and: "I can see the planned filter is not active"
            verifyMainPlannedFilterButtonIsSelected() == false
    }

}
