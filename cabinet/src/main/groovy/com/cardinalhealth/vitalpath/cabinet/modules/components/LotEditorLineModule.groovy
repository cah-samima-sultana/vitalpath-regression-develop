package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule


class LotEditorLineModule extends BaseModule {

    static final String LOT_EDITOR_LINE_CLASS = "lotEditorLine"

    static content = {
        wrapper(wait: true) { $(".${LOT_EDITOR_LINE_CLASS}") }
    }

    def clickMinusButton() {
        clickName("minusButton")
    }

    def clickPlusButton() {
        clickName("plusButton")
    }

    def enterLotNumber(value){
        enterTextByName("lotNumber", value)
    }

    def enterQuantity(value){
        enterTextByName("quantityInput", value)
    }
}

