package com.cardinalhealth.vitalpath.client.specs

import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.common.CommonBaseSpec
import spock.lang.Ignore

@Ignore
class BaseSpec extends CommonBaseSpec {

    def initConfig() {
        homePage = BasePage
        siteSelectionPage = BasePage
    }

    def goToSchedulePage(){
        at BasePage
        gotoSchedule()
    }

    def goToDigParPage(){
        // if(isAt(DigParPage) == false){
        at BasePage
        gotoInventory()
        goToDigPars()
         }

    def goToOrderPreferencesPage(){
        at BasePage
        gotoInventory()
        goToOrderPreferences()
    }

    def goToLabelSettingsPage(){
        at BasePage
        goToSetup()
        goToLabelSettings()
    }

    def goToPatientsPage(){
        at BasePage
        goToSetup()
        goToPatients()
    }

    def refreshPage(){
        clickRefresh()
        waitFor ({at BasePage})
    }
}