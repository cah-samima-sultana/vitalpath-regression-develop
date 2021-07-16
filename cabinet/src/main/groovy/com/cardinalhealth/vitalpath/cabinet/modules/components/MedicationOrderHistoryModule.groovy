package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class MedicationOrderHistoryModule extends BaseModule{

    static content = {
        wrapper(wait: true) { $(class: "contentContainer") }
        newOrderButton(wait:true) { $(id:"cancelExistingOrder") }
        useExistingOrderButton(wait:true) { $(id:"useExistingOrder") }
    }

    def selectHistoryCard(orderId){
        waitForAnimationToComplete()
        clickDataId(orderId)
    }

    def clickUseExistingOrder(){
        waitForAnimationToComplete()
        doClick(useExistingOrderButton)
        waitForAnimationToComplete()
    }

    def clickNewOrder(){
        waitForAnimationToComplete()
        doClick(newOrderButton)
        waitForAnimationToComplete()
    }

}
