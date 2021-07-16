package com.cardinalhealth.vitalpath.client.modules

import com.cardinalhealth.vitalpath.common.modules.DateSelectModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule
import com.cardinalhealth.vitalpath.utils.SelectorOptions

class ClinicalOrderHeaderModule extends BaseModule {

    def patientDropDownDataId = "patient-selector"
    def physicianDropDownDataId = "physician-selector"
    def cancelButtonDataId = "cancel-button"
    def saveButtonDataId = "save-button"
    def deleteButtonDataId = "delete-button"
    def displayModeDataId ="display-mode"

    static content = {
        wrapper(wait: true) { $("class": "clinical-order-header") }

        patientDropDown(required: true, wait: true) { module(new SelectorModule(selector: $(dataId(patientDropDownDataId)))) }
        physicianDropDown(required: true, wait: true) { module(new SelectorModule(selector: $(dataId(physicianDropDownDataId)))) }
        dateSelect(required: true, wait: true) { module(new DateSelectModule()) }
    }

    def clickSaveButton(){
        clickDataId(saveButtonDataId)
    }

    def clickCancelButton(){
        clickDataId(cancelButtonDataId)
    }

    def clickDeleteButton(){
        waitFor{$('.isDisabled', 'data-id': deleteButtonDataId).size() == 0}
        clickDataId(deleteButtonDataId)
    }

    def searchForPatient(SelectorOptions options) {
        patientDropDown.search(options)
    }

    def searchForPhysician(SelectorOptions options) {
        physicianDropDown.search(options)
    }

    def isInDisplayMode() {
        isDataIdDisplayed(displayModeDataId)
    }

    def selectCurrentDate(){
        dateSelect.selectCurrentDate();
    }
}