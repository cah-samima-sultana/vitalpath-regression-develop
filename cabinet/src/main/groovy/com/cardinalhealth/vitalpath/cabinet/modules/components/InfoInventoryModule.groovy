package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule
import org.openqa.selenium.By

class InfoInventoryModule extends BaseModule {

    final static String MODULE_CLASS = "infoInventoryHelper"

    final static String MULTI_DOSE_VIAL_ICON_CLASS = "fuse-multi-dose-vial"
    final static String SINGLE_DOSE_VIAL_ICON_CLASS = "fuse-single-dose-vial"
    final static String TABLET_ICON_CLASS = "fuse-form-tablet"
    final static String SOLUTION_ICON_CLASS = "fuse-ampule"
    final static String SYRINGE_ICON_CLASS = "fuse-syringe"

    static content = {
        wrapper(wait: true) { $(".${MODULE_CLASS}") }
    }


    def verifyItemName(value){
        assert assertTextContainsByDataId("inventory-name", value) : "Item name ${value} was not found"
    }

    def verifyInventoryLocationName(value){
        assert assertTextContainsByDataId("inventory-location-name", value) : "Location name ${value} was not found"
    }

    def verifyNdc(value){
        assert assertTextContainsByDataId("inventory-ndc", value) : "NDC ${value} was not found"
    }

    def verifyTai(value){
        assert assertTextContainsByDataId("inventory-tai-formatted", value) : "TAI ${value} was not found"
    }

    def verifyForm(value){
        assert assertTextContainsByDataId("inventory-form-name", value) : "Form ${value} was not found"
    }

    def verifyExpires(value){
        assert assertTextContainsByDataId("expires-date-formatted", value) : "Expires ${value} was not found"
    }

    def verifyStatus(value){
        assert assertTextContainsByDataId("order-state-name", value) : "Status ${value} was not found"
    }

    def verifyPartialQuantity(value){
        assert assertTextContainsByDataId("partial-quantity-formatted", value) : "Partial Qty ${value} was not found"
    }

    def verifyPatientName(value){
        assert assertTextContainsByDataId("patient-full-name-mrn", value) : "Patient name ${value} was not found"
    }

    def verifyInventorySegment(value){
        assert assertTextContainsByDataId("inventory-segment-name", value) : "Inventory Segment ${value} was not found"
    }

    def verifyInventoryIcon(value){
        assert isNonEmptyNavigatorByClassName(value) : "Icon class is not {$value}"
    }

    def isMultiDoseVial(){
        isNonEmptyNavigatorByClassName(MULTI_DOSE_VIAL_ICON_CLASS)
    }

    def isSingleDoseVial(){
        isNonEmptyNavigatorByClassName(SINGLE_DOSE_VIAL_ICON_CLASS)
    }

    def isTablet(){
        isNonEmptyNavigatorByClassName(TABLET_ICON_CLASS)
    }

    def isSolution(){
        isNonEmptyNavigatorByClassName(SOLUTION_ICON_CLASS)
    }

    def isSyringe(){
        isNonEmptyNavigatorByClassName(SYRINGE_ICON_CLASS)
    }

}

