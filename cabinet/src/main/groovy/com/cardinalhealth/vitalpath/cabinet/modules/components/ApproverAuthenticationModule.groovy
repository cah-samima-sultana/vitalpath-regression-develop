package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule


class ApproverAuthenticationModule extends BaseModule {

    static content = {
        wrapper(wait: true) { $('data-id': 'approver-authentication-widget') }
    }

    def clickConfirmButton() {
        clickDataId("confirmButton")
    }

    def clickCancelButton() {
        clickDataId("cancelButton")
    }

    def enterLogonName(value){
        enterTextByName("logonName", value)
    }

    def enterPassword(value){
        enterTextByName("password", value)
    }

    def enterApproval(id, pwd){
        enterLogonName(id)
        enterPassword(pwd)
        clickConfirmButton()
    }

    def isShown(){
        $("#approverAuthenticationWidget").size() > 0
    }
}

