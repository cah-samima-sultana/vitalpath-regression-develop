package com.cardinalhealth.vitalpath.cabinet.pages.dispense

import com.cardinalhealth.vitalpath.cabinet.modules.components.AddMedicationOrderModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.CloneMedicationOrderModule
import com.cardinalhealth.vitalpath.cabinet.modules.components.MedicationOrderHistoryModule


class DispenseAddMedicationOrderPage extends BaseDispensePage {
    static url = "#/cabinet/dispense/add"
    static at = { waitFor { title == "IMS | Cabinet – Dispense – Add" } }

    static content = {
        addMedicationOrder { module(new AddMedicationOrderModule()) }
        cloneMedicationOrder { module(new CloneMedicationOrderModule())}
        historyMedicationOrder { module(new MedicationOrderHistoryModule()) }
    }

    def searchAndSelectPatientByMrn(patient) {
        addMedicationOrder.searchAndSelectPatientByMrn(patient)
    }

    def searchPhysician(physician) {
        addMedicationOrder.searchPhysician(physician)
    }

    def clickAddLineButton(){
        addMedicationOrder.clickAddLineButton()
    }

    def clickCloneButton(){
        addMedicationOrder.clickCloneButton()
    }

    def clickSaveButton(){
        addMedicationOrder.clickSaveButton()
    }

    def clickAddLineDoneButton(){
        addMedicationOrder.clickAddLineDoneButton()
    }

    def clickSaveAddAnotherButton(){
        addMedicationOrder.clickSaveAddAnotherButton()
    }


    def addNewDrugFamilyLine(drugFamily, quantity, route) {
        clickAddLineButton()
        addMedicationOrder.addDrugFamilyLine(drugFamily, quantity, route)
    }

    def addAnotherDrugFamilyLine(drugFamily, quantity, route) {
        clickSaveAddAnotherButton()
        addMedicationOrder.addDrugFamilyLine(drugFamily, quantity, route)
    }

}
