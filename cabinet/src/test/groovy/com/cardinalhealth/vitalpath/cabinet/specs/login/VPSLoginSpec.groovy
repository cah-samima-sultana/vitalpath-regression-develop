package com.cardinalhealth.vitalpath.cabinet.specs.login

import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.login.LoginReasonPage
import com.cardinalhealth.vitalpath.cabinet.pages.login.LogoutPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.common.pages.LoginPage
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class VPSLoginSpec extends BaseSpec{

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SYSTEM_USER_LOGIN_REASON, value = SettingsConstant.TRUE)
            ])
    def "If VPS user confirms on login reason page, they are logged in"() {

        setup: "Enable Login Reason screen for VPS user"
            waitFor { header.clickSignOut() } // BaseSpec logged us in on setup

        when: "I login as VPS user"
            at LoginPage
            login.loginWith "vps", "vps"
            at LoginReasonPage

        and: "I enter name and select reason code, the confirm button enables"
        //    verifyConfirmIsDisabled()
            enterNameAndReason "vps user", "Production Support"
            verifyConfirmIsEnabled()

        and: "I confirm"
            confirm()

        then: "I'm logged in"
            at DispenseHomePage
        waitFor { header.clickSignOut() }

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.SYSTEM_USER_LOGIN_REASON, value = SettingsConstant.TRUE)
            ])
    def "If VPS User cancels on Login Reason Page, they are logged out"() {
        setup: "Enable Login Reason screen for VPS user"
        waitFor { header.clickSignOut() }
        when: "I login as VPS user"

            at LoginPage
          sleep(1000)
            login.loginWith "vps", "vps"
            at LoginReasonPage

        and: "I cancel"
            cancel()

        then: "I'm logged out"
            via LogoutPage
            at LoginPage

    }
}

