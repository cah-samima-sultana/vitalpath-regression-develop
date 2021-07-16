package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule
import com.cardinalhealth.vitalpath.common.modules.Select2Module
import com.cardinalhealth.vitalpath.common.modules.SelectorModule

class AddMedicationOrderModule extends BaseModule {

    static content = {
        patientSelect { module(new SelectorModule(selector: $('data-id': 'patient-selector'))) }
        physicianSelect { module(new SelectorModule(selector: $('data-id': 'physician-selector'))) }

        medicationOrderLineMaint { module(new MedicationOrderLineMaintModule()) }


        medicationOrderCard { $(class: "baseCard") }

        saveButton(wait:true) { $('data-id': 'save-clinical-order-line-button') }
        addLineButton(wait:true) { $('data-id': 'add-clinical-order-line-button') }
        cloneButton(wait:true) { $('data-id': 'clone-clinical-order-button') }
        editLineButton(wait:true) { $('data-id': 'edit-clincial-order-line') }
        removeLineButton(wait:true) { $('data-id': 'remove-clinical-order-line') }

        addPatientButton(wait:true) { $('data-id': 'add-patient') }
        homeButton(wait:true) { $('data-id': 'home-button') }

        lineContainer(wait:true) { $(class: "scrollContainer")}
    }


    def clickSaveButton(){
        doClick(saveButton)
    }

    def clickAddLineButton(){
        doClick(addLineButton)
    }

    def clickCloneButton(){
        doClick(cloneButton)
        waitForAnimationToComplete()
    }

    def clickEditLineButton(){
        doClick(editLineButton)
    }

    def clickRemoveLineButton(){
        doClick(removeLineButton)
    }

    def clickAddPatientButton(){
        doClick(addPatientButton)
    }

    def clickHomeButton(){
        doClick(homeButton)
    }

    def clickSaveAddAnotherButton(){
        medicationOrderLineMaint.clickSaveAddAnotherButton();
    }

    def clickAddLineDoneButton(){
        medicationOrderLineMaint.clickAddLineDoneButton()
    }

    def searchAndSelectPatientByLastName(patient) {
        patientSelect.openSearchAndSelect(patient.lastName)
    }

    def searchAndSelectPatientByMrn(patient) {
        patientSelect.openSearchAndSelect(patient.accountNumber)
    }

    def searchPhysician(physician) {
        def searchText = "${physician.lastName}, ${physician.firstName}"
        physicianSelect.openSearchAndSelect(searchText)
    }

    def addDrugFamilyLine(drugFamily, quantity, route) {
        medicationOrderLineMaint.addDrugFamilyLine(drugFamily, quantity, route)
    }

}
