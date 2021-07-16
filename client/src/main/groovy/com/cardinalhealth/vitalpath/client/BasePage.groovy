package com.cardinalhealth.vitalpath.client
import com.cardinalhealth.vitalpath.client.modules.ConfirmationModule
import com.cardinalhealth.vitalpath.client.modules.HeaderModule
import com.cardinalhealth.vitalpath.client.pages.inventory.InventoryHomePage
import com.cardinalhealth.vitalpath.client.pages.schedule.ScheduleHomePage
import com.cardinalhealth.vitalpath.client.pages.setup.SetupHomePage
import com.cardinalhealth.vitalpath.common.pages.CommonBasePage

class BasePage extends CommonBasePage {

    static content = {
    	header(required:true, wait: true) { module(new HeaderModule()) }
        confirmationModal(required: false, wait: true) { module(new ConfirmationModule())}
    }


    def gotoSchedule() {
        header.clickSchedule()
        browser.at ScheduleHomePage
    }

    def gotoInventory() {
        header.clickInventory()
        browser.at InventoryHomePage
    }


    def clickSiteSelectionDropDown(){
        clickSiteSwitcherDropDown()
    }

    def selectCabinet(site, cabinet){
        header.selectSite(site)
        waitFor { $(".site-header").size() > 0 }
    }

    def goToSetup() {
        header.clickSetup()
        browser.at SetupHomePage
    }

}