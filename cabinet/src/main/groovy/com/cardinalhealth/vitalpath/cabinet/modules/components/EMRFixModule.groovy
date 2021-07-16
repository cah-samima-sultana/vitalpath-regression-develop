package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule

class EMRFixModule extends BaseModule {


    static content = {
        wrapper(wait: true) { dataId("clinical-order-line-source-maint") }

        drugFamilySelector(wait: true) { module(new SelectorModule( selector: $("[data-id=col-source-maint-drug-family-selector]") )) }
        quantityInput(wait: true) { $("[data-id=quantityWidget]").find("input") }
        routeSelector(wait: true) { module(new SelectorModule( selector: $("[data-id=col-source-maint-route-selector]") )) }
        fixButton(wait: true) { $("[data-id=fix-button]") }
    }

    def clickFixButton(){
        fixButton.click()
    }

    def inputQuantity(String quantity){
        waitFor(3, {quantityInput.hasClass("isDisabled") == false})
        quantityInput << quantity
    }

    def selectDrugFamily(String drugFamily){
        drugFamilySelector.openSearchAndSelect(drugFamily)
    }

    def selectRoute(String route){
        waitFor(3, {routeSelector.hasClass("isDisabled") == false})
        routeSelector.openSearchAndSelect(route)
    }
}

