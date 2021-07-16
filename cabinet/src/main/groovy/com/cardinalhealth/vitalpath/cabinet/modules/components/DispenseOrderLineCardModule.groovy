package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class DispenseOrderLineCardModule extends BaseModule {

    final static String DISPENSE_ORDER_LINE_CARD_CLASS = "dispenseOrderLineCard"
    final static String COMPLETED_BADGE_CLASS = "c"
    final static String IN_PROGRESS_BADGE_CLASS = "ip"
    final static String NOT_STARTED_BADGE_CLASS = "ns"
    final static String NEEDS_ATTENTION_BADGE_CLASS = "na"
    final static String PLANNED_BADGE_CLASS = "p"
    final static String SELECTED_CLASS = "isSelected"

    static content = {
        wrapper(wait: true) { $(".${DISPENSE_ORDER_LINE_CARD_CLASS}") }

    }

    def isSelected(){
        isNonEmptyNavigatorByClassName(SELECTED_CLASS)
    }

    def isComplete(){
        isNonEmptyNavigatorByClassName(COMPLETED_BADGE_CLASS)
    }

    def isNotStarted(){
        isNonEmptyNavigatorByClassName(NOT_STARTED_BADGE_CLASS)
    }

    def isInProgress(){
        isNonEmptyNavigatorByClassName(IN_PROGRESS_BADGE_CLASS)
    }

    def isNeedsAttention(){
        isNonEmptyNavigatorByClassName(NEEDS_ATTENTION_BADGE_CLASS)
    }

    def isPlanned(){
        isNonEmptyNavigatorByClassName(PLANNED_BADGE_CLASS)
    }

    def verifyPrescribedQty(value){
        assertTextContainsByDataId("prescribed-quantity", value)
    }

    def verifyDispensedQty(value){
        assertTextContainsByDataId("dispensed-quantity", value)
    }

    def verifyPlannedQty(value){
        assertTextContainsByDataId("planned-quantity", value)
    }

    def verifyEstimatedWasteQty(value){
        assertTextContainsByDataId("estimated-waste-quantity", value)
    }

    def verifyName(value){
        assertTextContainsByDataId("order-name", value)
    }
}

