package com.cardinalhealth.vitalpath.client.pages.inventory
import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.client.modules.InventorySideNavModule

class InventoryHomePage extends BasePage{

    static url = "#/client/inventory/inventoryInquiry"
    static at = { waitFor { $("data-id" : "inventory-side-nav") } }

    static content = {
        sideNav (wait: true) { module(new InventorySideNavModule()) }
    }

    def goToDigPars(){
        sideNav.clickDigPars()
        browser.at DigParPage
    }

    def goToOrderPreferences(){
        sideNav.clickOrderPreferences()
        browser.at OrderPreferencesPage
    }

}
