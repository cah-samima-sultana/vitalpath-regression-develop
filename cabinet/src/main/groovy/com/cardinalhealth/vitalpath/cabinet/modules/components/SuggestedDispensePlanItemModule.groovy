package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class SuggestedDispensePlanItemModule extends BaseModule {

    static content = {
        wrapper(wait: true) { $("[data-id=plan-item]") }
    }

    def verifyVialCount(count){
        assertTextContainsByDataId("containers", count)
    }

    def verifyNdc(ndc){
        assertTextContainsByDataId("ndc-value", ndc)
    }

}

