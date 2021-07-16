package com.cardinalhealth.vitalpath.client.modules

import com.cardinalhealth.vitalpath.common.modules.DateSelectModule
import com.cardinalhealth.vitalpath.common.modules.MedicationOrderFilterMenuModule
import com.cardinalhealth.vitalpath.common.modules.MedicationOrderFilterModule
import com.cardinalhealth.vitalpath.common.modules.ScrollBoxSearchModule
import geb.navigator.Navigator

class ScheduleSummaryModule extends BaseModule {

    static content = {
        wrapper(wait: true) { $("data-id": "schedule-summary-component") }

        dateSelect { module(new DateSelectModule()) }
        medicationOrderFilter { module(new MedicationOrderFilterModule()) }
        scrollBoxSearchModule { module(new ScrollBoxSearchModule()) }
        medicationOrderFilterMenu { module(new MedicationOrderFilterMenuModule()) }

        addButton(wait:true) { $("data-id": "add-button") }
        printButton(wait:true) { $("data-id": "print-button") }
        refreshButton(wait:true) { $("data-id": "refresh-button") }

        scrollContainer(wait:true) { $("data-id": "schedule-summary-component-scroll-container") }
        noOrdersMessage(required: false, wait:true) { $(dataId("no-orders-message")) }

        plannedFilterButton(wait: true) { $("data-id": "filter-planned") }
        notStartedFilterButton(wait: true) { $("data-id": "filter-not-started") }
        inProgressFilterButton(wait: true) { $("data-id": "filter-in-progress") }
        completeFilterButton(wait: true) { $("data-id": "filter-complete") }
    }

    def clickFilterPlanned(){
        doClick(plannedFilterButton)
        waitForAnimationToComplete()
    }

    def clickFilterNotStarted(){
        doClick(notStartedFilterButton)
        waitForAnimationToComplete()
    }

    def clickFilterInProgress(){
        doClick(inProgressFilterButton)
        waitForAnimationToComplete()
    }

    def clickFilterComplete(){
        doClick(completeFilterButton)
        waitForAnimationToComplete()
    }

    def clickAddButton(){
        doClick(addButton)
        waitForAnimationToComplete()
    }

    def clickPrintButton(){
        doClick(printButton)
    }

    def clickRefreshButton(){
        doClick(refreshButton)
        waitForAnimationToComplete()
    }

    def openFilterMenu(){
        if(medicationOrderFilterMenu.modalDialogWrapper.displayed) {
            doClick(medicationOrderFilterMenu.open())
        }
    }

    def cardExists(Navigator card){
        isNonEmptyNavigator(card)
    }

    def Navigator findCardByMrn(patient){
        waitForAnimationToComplete()
        $(".medicationOrderCard").filter(text: iContains(patient.accountNumber))
    }

    def selectCardByMrn(patient){
        def card = findCardByMrn(patient)
        def orderDetailsWrapper = card.find(".order-details-wrapper")
        doClick(orderDetailsWrapper)
        waitForAnimationToComplete()
    }

    def openClinicalOrderById(orderId){
        waitFor{$('.order-details-wrapper', 'data-id': orderId)}.click()
        waitFor(3, {$(".clinical-order").size() > 0})
    }

    def cardByMrnNotExist = {mrnValue ->
        sleep(500)
        //mrnValue = "1486123716269"
        def elements = scrollContainer.children().findAll { it.text().contains mrnValue }

        elements.size() == 0
    }

    def verifyNoOrdersMessageExists() {
        isNonEmptyNavigator($("data-id": "no-orders-message"))
    }

}