package com.cardinalhealth.vitalpath.common.modules

class MedicationOrderFilterMenuModule extends CommonBaseModule {

    static content = {
        modalDialogWrapper(wait: true) { $("data-id" : "medication-order-filter-menu") }
        modalDialogContent(required:false) { $("data-id" : "medication-order-filter-menu-dialog") }
        header(required: false) { $(".menu-header")}
    }

    def open(){
        if(modalDialogWrapper.displayed) {
           doClick(modalDialogWrapper)
        }

//        boolean t = modalDialogContent.isDisplayed()
        waitFor { header.displayed }
    }
}