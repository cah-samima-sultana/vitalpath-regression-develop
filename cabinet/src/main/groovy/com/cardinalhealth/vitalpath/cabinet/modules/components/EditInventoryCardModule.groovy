package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class EditInventoryCardModule extends BaseModule {

    final static String MODULE_CLASS = "editInventoryCard"
    final static String PATIENT_ASSIST_BADGE_CLASS = "fuse-user-favorite"
    final static String WHITEBAG_CLASS = "ip"
    final static String CLINICAL_TRIAL_BADGE_CLASS = "ns"

    static content = {
        wrapper(wait: true) { $(".${MODULE_CLASS}") }
    }

    def InfoInventoryModule infoInventory(){
        getModule(this, InfoInventoryModule.MODULE_CLASS, InfoInventoryModule.class)
    }

    def clickEditButton() {
        clickDataId("editButton")
    }

    def clickDeleteButton() {
        clickDataId("deleteButton")
    }

    def isPatientAssist(){
        isNonEmptyNavigatorByClassName(PATIENT_ASSIST_BADGE_CLASS)
    }

    def isClinicalTrial(){
        isNonEmptyNavigatorByClassName(CLINICAL_TRIAL_BADGE_CLASS)
    }

    def isWhitebag(){
        isNonEmptyNavigatorByClassName(WHITEBAG_CLASS)
    }

}

