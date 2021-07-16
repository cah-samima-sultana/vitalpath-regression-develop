package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class ConfirmWithReasonModule extends BaseModule {

    static content = {
        wrapper(wait: true) { $('data-id' : 'confirm-remove-line') }
    }

    def clickConfirmButton() {
        clickDataId("confirmConfirmWidgetBtn")
    }

    def clickCancelButton() {
        clickDataId("cancelConfirmWidgetBtn")
    }

}
