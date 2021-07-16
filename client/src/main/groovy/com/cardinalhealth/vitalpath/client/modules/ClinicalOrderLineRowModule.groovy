package com.cardinalhealth.vitalpath.client.modules

import com.cardinalhealth.vitalpath.common.modules.QuantityModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule

class ClinicalOrderLineRowModule extends BaseModule {

    def wrapperDataId = ""
    def drugColumnDataId = "drug-column"
    def drugFamilyDataId = "drug-family-selector"
    def drugItemDataId = "item-selector"
    def doseInputDataId = "quantity"
    def doseColumnDataId = "dose-column"
    def routeDataId = "route-selector"
    def statusColumnDataId = "status-column"
    def cancelButtonDataId = "line-cancel-button"
    def addSaveButtonDataId = "line-save-add-button"
    def lineSaveButtonDataId = "line-save-button"
    def editButtonDataId = "line-edit-button"
    def addEditModeDataId ="add-edit-mode"
    def displayModeDataId ="display-mode"
    def lineDeleteDataId ="line-delete-button"

    static content = {
        wrapper(required: false, wait: 1) { $("data-id": wrapperDataId) }

        drugColumn(required: false, wait: true) {
            $(dataId(drugColumnDataId))
        }

        drugFamilyDropDown(required: false, wait: true) {
            module(new SelectorModule(selector: $("[data-id='${drugFamilyDataId}']")))
        }
        drugItemDropDown(required: false) {
            module(new SelectorModule(selector: $("[data-id='${drugItemDataId}']")))
        }
        doseWidget(required: false, wait: true) { module(new QuantityModule(dataId: doseInputDataId)) }

        routeDropDown(required: false, wait: true) {
            module(new SelectorModule(selector: $("[data-id='${routeDataId}']")))
        }
    }

    def clickAddSaveButton(){
        clickDataId(addSaveButtonDataId)
    }

    def clickLineSaveButton(){
        clickDataId(lineSaveButtonDataId)
    }

    def clickLineDeleteButton(){
        clickDataId(lineDeleteDataId)
    }

    def clickEditButton(){
        clickDataId(editButtonDataId)
    }

    def clickCancelButton(){
        clickDataId(cancelButtonDataId)
    }

    def searchForDrugFamily(value) {
        drugFamilyDropDown.searchAndSelect(value)
    }

    def searchForDrugItem(value) {
        drugItemDropDown.openSearchAndSelect(value)
    }

    def searchForRoute(value) {
        routeDropDown.openSearchAndSelect(value)
    }

    def enterDose(value) {
        doseWidget.enterQuantity(value)
    }

    def isDoseEnabled(){
        waitFor(3, {$(".quantityInput").attr("disabled") != "disabled"})
    }

    def doseIsEqualTo(value) {
        $(dataId(doseColumnDataId), text: contains(value))
    }

    def statusIsEqualTo(value) {
        $(dataId(statusColumnDataId), text: contains(value))
    }

    def statusIsNotStarted() {
        waitFor { statusIsEqualTo("Not Started") }
    }

    def statusIsPlanned() {
        waitFor { statusIsEqualTo("Planned") }
    }

    def isShowingBatchPlanIcon() {
        waitFor { $(dataId("line-planned-icon")) }
    }

    def isInDisplayMode() {
        isDataIdDisplayed(displayModeDataId)
    }
    def isInEditMode() {
        waitFor { $(dataId(addEditModeDataId)) }
    }

    def pressEnterInDose(){
        doseWidget.pressEnter()
    }

    def clickRow() {
        drugColumn.click()
    }
}