package com.cardinalhealth.vitalpath.cabinet.pages
import com.cardinalhealth.vitalpath.cabinet.modules.CabinetHeaderModule
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.messages.MessagesHomePage
import com.cardinalhealth.vitalpath.cabinet.traits.CabinetAnimationTrait
import com.cardinalhealth.vitalpath.cabinet.traits.CabinetTrait
import com.cardinalhealth.vitalpath.common.pages.CommonBasePage

class BasePage extends CommonBasePage implements CabinetAnimationTrait, CabinetTrait {

    static at = { waitFor {title.contains('IMS | Cabinet') } }

    static content = {
    	header (required: true, wait: true) { module(new CabinetHeaderModule()) }
    	helperText { $("div.helperText") }
        activePageElement(required:false) { driver.switchTo().activeElement() }

        cardById { id ->
            def mId = id.replaceAll(/-/, "")
            $(id: "$mId")
        }

    }

    def getActiveElement() {
        return driver.switchTo().activeElement()
    }

    def gotoDispense() {
        waitForAnimationToComplete()
        header.clickDispense()
        browser.at DispenseHomePage
    }

    def gotoInventory() {
        waitForAnimationToComplete()
        sleep(2000)
        //wait(200)
        header.clickInventory()
        wait()
        synchronized (available) {
            available.wait();
        }
        browser.at InventoryHomePage
    }

    def gotoMessages() {
        waitForAnimationToComplete()
        header.clickMessages()
        browser.at MessagesHomePage
    }

    def signOut() {
        waitForAnimationToComplete()
        header.clickSignOut()
    }

    def gotoAbout() {
        waitForAnimationToComplete()
        header.clickAbout()
//        browser.at AboutPage)  // TODO uncomment when AboutPage exists
    }

    def gotoTemperature() {
        waitForAnimationToComplete()
        header.clickTemperature()
//        browser.at ControlsHomePage) // TODO uncomment when ControlsHomePage exists
    }

    def gotoControls() {
        waitForAnimationToComplete()
        header.clickControls()
//        browser.at ControlsHomePage) // TODO uncomment when ControlsHomePage exists
    }





}