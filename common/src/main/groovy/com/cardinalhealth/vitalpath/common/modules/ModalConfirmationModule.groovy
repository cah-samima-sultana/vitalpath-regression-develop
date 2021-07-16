package com.cardinalhealth.vitalpath.common.modules

class ModalConfirmationModule extends CommonBaseModule {

    static content = {
        wrapper(wait: true) { $(".confirmation-container") }

        cancelButton(wait: true) { $("data-id" : "confirmation-close-button")}
        continueButton(wait: true) { $("data-id" : "confirmation-continue-button")}
    }


    def clickCancelButton(){
        cancelButton.click()
    }

    def clickContinueButton(){
        continueButton.click()
    }

}