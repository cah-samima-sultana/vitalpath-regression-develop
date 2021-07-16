package com.cardinalhealth.vitalpath.cabinet.pages.login

import com.cardinalhealth.vitalpath.cabinet.pages.BasePage
import com.cardinalhealth.vitalpath.common.modules.Select2Module

class LoginReasonPage extends BasePage {
    static at = { waitFor {title == "IMS | Login – Reason" } }

    static content = {
        cancel { $('#cancelBtn') }
        confirm{ $('#confirmBtn') }
        name{ $("#userName") }
        reasonCode(wait:true) { module(new Select2Module(selectId: "s2id_systemLoginReason")) }
        reasonCodeItem(wait:true){ reasonCodeName ->  $('div', text: reasonCodeName )}
    }

    def cancel() {
        cancel.click()
    }
    def confirm(){
        confirm.click()
    }

    def verifyConfirmIsDisabled(){
        waitFor { confirm.displayed }
       // assert confirm.hasClass('isEnabled'), 'The confirm button on reason screen was not disabled initially.'
        assert confirm.hasClass('isDisabled'), 'The confirm button on reason screen was not disabled initially.'
    }

    def verifyConfirmIsEnabled(){
        waitFor { confirm.displayed }

        assert !confirm.hasClass('isDisabled'), 'The confirm button on reason screen is not enabled.'
    }

    def enterNameAndReason(name, reasonValue){
        this.name.value name

        reasonCode.selectItem(reasonValue)
    }
}
