package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class ConfirmModule extends BaseModule {
    final static String MODULE_CLASS = "confirmWidget"

    static content = {
        wrapper(wait: true) { $(".${MODULE_CLASS}") }
    }

    def clickConfirmButton() {
        clickDataId("confirmConfirmWidgetBtn")
        waitForAnimationToComplete()
    }

    def clickCancelButton() {
        clickDataId("cancelConfirmWidgetBtn")
        waitForAnimationToComplete()
    }

}
