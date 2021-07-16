package com.cardinalhealth.vitalpath.client.modules


class InventorySideNavModule extends BaseModule {

    def digPar = 'dig-pars'
    def orderPreferences = 'order-preferences'

    static content = {
        wrapper(wait: true) { $("data-id" : "inventory-side-nav") }
    }


    def clickDigPars(){
        waitForAnimationToComplete()
        clickId(digPar)
    }

    def clickOrderPreferences(){
        waitForAnimationToComplete()
        clickId(orderPreferences)
    }



}