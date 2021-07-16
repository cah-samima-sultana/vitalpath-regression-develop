package com.cardinalhealth.vitalpath.client.modules

class ClinicalOrderLinesGridModule extends BaseModule {

    def dataId = "clinical-order-lines-grid"

    def addButtonDataId = "line-add-button"
    def cancelButtonDataId = "line-cancel-button"
    def clinicalOrderLineDataId = "add-line"
    def clinicalOrderLineDataIdPrefix = "line-"

    static content = {
        wrapper(wait: true) { $("class": dataId) }

        clinicalOrderLineRow(required: false, wait: true) { module(new ClinicalOrderLineRowModule(wrapperDataId: clinicalOrderLineDataId)) }
    }

    def clickAddButton(){
        clickDataId(addButtonDataId)
    }

    def clickCancelButton(){
        clickDataId(cancelButtonDataId)
    }

    def findRowByLineNumber = {lineNumber ->
        waitFor { $(dataId("line-${lineNumber}")) }
        $(dataId("line-${lineNumber}"))
    }

    def getRowByLineNumber = {lineNumber ->
        def row = findRowByLineNumber(lineNumber)
        def rowModule
        if(row) {
            rowModule = row.module(ClinicalOrderLineRowModule.class)
            rowModule.wrapperDataId = "${clinicalOrderLineDataIdPrefix}${lineNumber}"
        }

        rowModule
    }

    def addDrugFamilyLine(drugFamily, dose){
        addDrugFamilyLine(drugFamily, dose, null)
    }

    def addDrugFamilyLine(drugFamily, dose, route){
        clinicalOrderLineRow.searchForDrugFamily(drugFamily)
        clinicalOrderLineRow.isDoseEnabled()
        clinicalOrderLineRow.enterDose(dose)
        if(route){
            clinicalOrderLineRow.searchForRoute(route)
            clinicalOrderLineRow.clickLineSaveButton()
        } else {
            clinicalOrderLineRow.pressEnterInDose()
        }
    }

    def editLineByLineNumber(lineNumber){
        def row = findRowByLineNumber(lineNumber)
        row.find(dataId("line-edit-button")).click();
    }
}