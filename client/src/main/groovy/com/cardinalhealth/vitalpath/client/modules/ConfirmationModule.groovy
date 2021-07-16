package com.cardinalhealth.vitalpath.client.modules

import com.cardinalhealth.vitalpath.common.modules.DateSelectModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule
import com.cardinalhealth.vitalpath.utils.SelectorOptions

class ConfirmationModule extends BaseModule {

    static content = {
        overlayWrapper(wait: true) { $("class": "ember-modal-wrapper") }
        wrapper(wait: true) { $("class": "ember-modal-dialog") }

        primaryLine(required: false, wait: true) { $(dataId("confirmation-message-primary-line"))}
        secondaryLine(required: false, wait: true) { $(dataId("confirmation-message-secondary-line"))}
        continueButton(required: false, wait: true) { $(dataId("confirmation-continue-button"))}
        closeButton(required: false, wait: true) { $(dataId("confirmation-close-button"))}
    }

    def overlayIsOnScreen(){
        $(".ember-modal-wrapper").size() > 0
    }

    def isOnScreen() {
        $(".ember-modal-dialog").size() > 0
    }

    def clickContinueButton(){
        continueButton.click()
    }

    def clickCloseButton(){
        closeButton.click()
    }

    def verifyPrimaryLine(value){
        primaryLine.text() == value

    }

    def verifySecondaryLine(value){
        secondaryLine.text() == value
    }

    def verifyCloseButton(value) {
        closeButton.text() == value
    }

    def verifyContinueButton(value) {
        continueButton.text() == value
    }

}