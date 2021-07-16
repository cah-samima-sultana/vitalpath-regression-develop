package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class CloneMedicationOrderModule extends  BaseModule{
    def cloneMedicationOrderButtonDataId = 'confirm-clone-button'
    def cancelCloneMedicationOrderButtonDataId = 'cancel-clone-button'

    static content = {
        wrapper(wait: true) { $("class": "medOrderCloneWidget") }
    }

    def clickCloneMedicationOrderButton(){
        clickDataId(cloneMedicationOrderButtonDataId)
    }

    def clickCancelCloneMedicationOrderButton(){
        clickDataId(cancelCloneMedicationOrderButtonDataId)
    }

    def clickMedicationOrderToClone(orderId){
        clickDataId(orderId.replace('-', '')+"cloneList")
    }
}
