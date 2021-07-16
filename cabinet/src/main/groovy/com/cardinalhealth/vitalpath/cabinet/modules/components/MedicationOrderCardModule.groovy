package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class MedicationOrderCardModule extends BaseModule {

    final static String DISPENSE_ORDER_LINE_CARD_CLASS = "medicationOrderCard"
    final static String COMPLETED_BADGE_CLASS = "c"
    final static String IN_PROGRESS_BADGE_CLASS = "ip"
    final static String NOT_STARTED_BADGE_CLASS = "ns"
    final static String NEEDS_ATTENTION_BADGE_CLASS = "na"
    final static String PLANNED_BADGE_CLASS = "p"

    static content = {
        wrapper(wait: true) { $(".${DISPENSE_ORDER_LINE_CARD_CLASS}") }

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
}

