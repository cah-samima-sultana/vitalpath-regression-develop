package com.cardinalhealth.vitalpath.cabinet.modules

import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.inventory.InventoryHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.messages.MessagesHomePage

class CabinetHeaderModule extends BaseModule {
    static content = {
        dispense(to: DispenseHomePage) { $('a', id:'dispenseHeaderLink')}
        inventory(to: InventoryHomePage, toWait: true) { $('a', id:'inventoryHeaderLink')}
        messages(to: MessagesHomePage){ $('a', id:'messagesHeaderLink')}
        signOutLink { $('a', id:'signoutHeaderLink')}
        about { $('a', id:'aboutHeaderLink')}
        temperature { $('a', id:'temperatureHeaderLink')}
        controls { $('a', id:'controlsHeaderLink')}



//        dispenseContainer { $( "#dispenseContainer" ) }
//        inventoryContainer { $(".fuse-location") }
    }

    def clickDispense() {
        //dispense.click()
        //wait(dispense).click()
        waitFor { dispense.click() }
    }

    def clickInventory() {
        doClick(inventory)
    }

    def clickMessages() {
        doClick(messages)

    }

    def clickSignOut(){
//        doClick(signOutLink)
//        def thisSignoutLink = $('#signoutHeaderLink')
//println "signout"
//        waitForCoveringElementToGoAwayThenDo({
            $('#signoutHeaderLink').click()
//            signOutLink.click()
//        })
//        return true
    }

    def clickAbout() {
        doClick(about)
    }

    def clickTemperature() {
        doClick(temperature)
    }

    def clickControls() {
        doClick(controls)
    }


}