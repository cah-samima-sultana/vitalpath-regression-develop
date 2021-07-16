package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule


class LotEditorModule extends BaseModule {

    static final String LOT_EDITOR_CLASS = "lotEditor"


    static content = {
        wrapper(wait: true) { $(".${LOT_EDITOR_CLASS}") }
        lineContainer(required: true) { $("data-id" : "line-container") }
    }

    def clickSaveAndCloseButton() {
        clickName("saveAndCloseButton")
    }

    def clickSaveAndConfirmButton() {
        clickName("saveAndConfirmButton")
    }

    def clickCancelButton() {
        clickName("cancelButton")
    }

    def getLines(){
        getModules(lineContainer, ".${LotEditorLineModule.LOT_EDITOR_LINE_CLASS}", LotEditorLineModule.class)
    }

    def getFirstLine(){
        def line = null
        def items = getLines()
        if(items.size() > 0){
            line = items.first();
        }

        return line

    }

    def enterSingleLotNumber(lotNumber){
        def line = getFirstLine()
        if(line != null){
            line.enterLotNumber(lotNumber)
            return true
        }

        return false
    }
}

