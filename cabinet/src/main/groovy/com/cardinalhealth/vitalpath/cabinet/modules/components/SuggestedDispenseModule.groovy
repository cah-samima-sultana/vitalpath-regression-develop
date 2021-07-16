package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class SuggestedDispenseModule extends BaseModule {

    static content = {
    }

    def getEditButton(){
        $("[data-id=edit-button]").find("button")
    }

    def getSkipButton(){
        $("[data-id=skip-button]").find("[data-id=toggle-widget]")
    }

    def getDoseEditButton(){
        $("[data-id=dose-edit]")
    }

    def clickEdit(){
        waitFor(3, { getEditButton().click() } )
    }

    def clickSkip(){
        getSkipButton().click()
    }

    def clickEditDose(){
        waitFor(3, { getDoseEditButton().click() } )
    }

    def getPlanItemByIndex(int index){
        $("[data-id=plan-item]").moduleList(SuggestedDispensePlanItemModule).get(index)
    }

    def verifyPlanItemVialCount(int index, String count){
        def planItem = getPlanItemByIndex(index)
        planItem.verifyVialCount(count)
    }

    def verifyNdc(int index, String ndc){
        def planItem = getPlanItemByIndex(index)
        planItem.verifyNdc(ndc)
    }

    def verifyDose(String dose){
        assertTextContainsByDataId("prescribed-quantity", dose)
    }

}

