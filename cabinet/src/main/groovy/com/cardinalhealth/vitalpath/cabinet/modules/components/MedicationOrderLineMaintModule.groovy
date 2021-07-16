package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule
import com.cardinalhealth.vitalpath.common.modules.Select2Module
import com.cardinalhealth.vitalpath.common.modules.SelectorModule

class MedicationOrderLineMaintModule extends BaseModule{

    static content = {

        wrapper(wait: true) { $('data-id' : 'clinical-order-line-maint') }

        drugFamilySelect { module(new SelectorModule(selector: $(dataId("col-maint-drug-family-selector")))) }
        routeSelect { module(new SelectorModule(selector: $(dataId("col-maint-route-selector")))) }

        itemSelect { module(new SelectorModule(selector: $(dataId("col-maint-item-selector")))) }
        quantityInputBox{$(".quantityInput")}

        saveAddAnotherButton(wait:true) { dataId("save-add-clinical-order-line-button") }
        addLineDoneButton(wait:true) { dataId("save-add-done-clinical-order-line-button") }

        saveClinicalOrderLineAndCloseButton(wait:true) { $('#saveClinicalOrderLineAndClose') }
        cancelButton(wait:true) { $('#cancelClinicalOrderLine') }
    }

    def addDrugFamilyLine(drugFamily, quantity, route){
        drugFamilySelect.searchAndSelect(drugFamily)
        enterQuantity(quantity)
        if(route != null){
            routeSelect.openSearchAndSelect(route)
        }

        return true
    }

    def enterQuantity(quantity){
        waitFor(3, { quantityInputBox.value(quantity) })

        return true
    }

    def selectRoute(route){
        routeSelect.openSearchAndSelect(route)
    }

    def saveLineAndClose(){
        clickDataId("save-clinical-order-line-close-button")
        waitForAnimationToComplete()
    }

    def clickSaveAddAnotherButton(){
        clickDataId("save-add-clinical-order-line-button")
        waitForAnimationToComplete()
    }

    def clickAddLineDoneButton(){
        clickDataId("save-add-done-clinical-order-line-button")
        waitForAnimationToComplete()
    }



}
