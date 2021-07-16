package com.cardinalhealth.vitalpath.cabinet.pages.dispense

import com.cardinalhealth.vitalpath.cabinet.modules.components.MedicationOrderCardModule
import geb.navigator.Navigator

class DispenseHomePage extends BaseDispensePage {

    static url = "#/cabinet/dispense"
    static at = { waitFor { title == "IMS | Cabinet – Dispense – Index" } }

    static content = {

        searchTextInputBox(wait: true) { $("#medicationOrderSearchTextField") }
    }

    def clickEmergencyButton(){
        waitForAnimationToComplete()
        clickId("emergencyItemsButton")
    }

    def clickRefreshButton(){
        waitForAnimationToComplete()
        clickId("refreshbutton")
        waitForAnimationToComplete()
    }

    def clickAddButton(){
        clickId("addMedicationOrderButton")
        waitForAnimationToComplete()
    }

    def clickFilterNotStartedButton(){
        clickId("filterNS")
        waitForAnimationToComplete()
    }

    def clickFilterInProgressButton(){
        waitForAnimationToComplete()
        clickId("filterIP")
    }

    def clickFilterCompleteButton(){
        clickId("filterComplete")
        waitForAnimationToComplete()
    }

    def clickFilterNeedsAttentionButton(){
        clickId("filterNA")
        waitForAnimationToComplete()
    }

    def verifyMainPlannedFilterButtonIsSelected(){
        waitFor{ $("id" : "filterP")}
        $("id" : "filterP").hasClass("isSelected") == true
    }

    def clickBatchButton(){
        waitForAnimationToComplete()
        clickDataId("batchPickButton")
    }

    def clickMenuButton(){
        waitForAnimationToComplete()
        clickId("rightMenuButton");
    }

    def closeMenu() {
        clickId("offCanvasMenuClose")
        waitForAnimationToComplete()
    }

    def sortByAscendingDate(){
        waitForAnimationToComplete()
        clickMenuButton()
        waitForAnimationToComplete()
        clickDataId("date-ascending")
        waitForAnimationToComplete()
    }

    def sortByDescendingDate(){
        waitForAnimationToComplete()
        clickMenuButton()
        waitForAnimationToComplete()
        clickDataId("date-descending")
        waitForAnimationToComplete()
    }

    def sortByDescendingPatient(){
        clickMenuButton()
        waitForAnimationToComplete()
        clickDataId("patient-descending")
        waitForAnimationToComplete()
    }

    def verifyPlannedFilterIsSelected() {
        waitFor { $(dataId("planned-filter")).displayed }
        $(dataId("planned-filter")).hasClass("isSelected") == true
    }

    def verifyPlannedFilterCheckboxExists() {
        waitFor { $(dataId("planned-filter")).displayed }
        $(dataId("planned-check")).verifyNotEmpty()
    }

    def verifyBatchIsDisabled(){
        waitFor { $(dataId("batchPickButton")).displayed }
        $(dataId("batchPickButton")).hasClass("isDisabled") == true
    }

    def getFirstCardByMrn(String mrnValue) {
        def medicationOrderCard = $(class: "baseCard")
        def elements = medicationOrderCard.findAll { card -> card.text().contains mrnValue }

        if(elements.size() > 0){
            return elements[0].module(MedicationOrderCardModule.class)
        }

        return null
    }

    def selectFirstCardByMrn(String mrn){
        def card = getFirstCardByMrn(mrn)
        if(MedicationOrderCardModule.isInstance(card)){
            waitFor(3, {card.click() })
        }
    }

    def Navigator selectFirstCardByPatient(patient){
        selectFirstCardByMrn(patient.accountNumber)
    }

    def cardByMrnNotExist = {mrnValue ->
        waitForAnimationToComplete(500)
        def medicationOrderCard = $(class: "baseCard")
        def elements = medicationOrderCard.findAll { it.text().contains mrnValue }
        elements.size() == 0
    }


    def isFirstCardByPatientNotStarted(patient){
        waitForAnimationToComplete()
        def card = getFirstCardByMrn(patient.accountNumber)
        if(MedicationOrderCardModule.isInstance(card)){
            return card.isNotStarted()
        }
        return false
    }

    def waitForBatchPickEnabled(){
        waitFor{ $(dataId("batchPickButton")).hasClass("isDisabled") == false }
    }

}
