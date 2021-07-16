package com.cardinalhealth.vitalpath.common.modules

class MedicationOrderFilterModule extends CommonBaseModule {

    def dataId = ""

    static content = {
        wrapper(wait: true) { $(".dispenseActiveFilter") }

        filterNotStarted(wait: true) { $("data-id" : "filter-not-started")}
        filterPlanned(wait: true) { $("data-id" : "filter-planned")}
        filterInProgress(wait: true) { $("data-id" : "filter-in-progress")}
        filterComplete(wait: true) { $("data-id" : "filter-complete")}
        filterNeedsAttention(wait: true) { $("data-id" : "filter-needs-attention")}
    }


    def clickFilterNotStarted(){
        filterNotStarted.click()
        waitForAnimationToComplete()
    }

    def clickFilterPlanned(){
        filterPlanned.click()
        waitForAnimationToComplete()
    }

    def clickFilterInProgress(){
        filterInProgress.click()
        waitForAnimationToComplete()
    }

    def clickFilterComplete(){
        filterComplete.click()
        waitForAnimationToComplete()
    }

    def clickFilterNeedsAttention(){
        filterNeedsAttention.click()
        waitForAnimationToComplete()
    }
}