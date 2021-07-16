package com.cardinalhealth.vitalpath.client.pages.setup
import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.client.modules.SetupSideNavModule

class SetupHomePage extends BasePage{

    static url = "#/client/setup/items"
    static at = { waitFor { $("data-id" : "setup-side-nav") } }

    static content = {
        sideNav (wait: true) { module(new SetupSideNavModule()) }
    }

    def goToLabelSettings(){
        sideNav.clickLabelSettings()
        browser.at LabelPage
    }

    def goToPatients(){
        sideNav.clickPatients()
        browser.at PatientPage
    }

}
