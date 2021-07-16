package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule

class InventoryInfoModule extends BaseModule {

    static content = {
        wrapper(wait: true, required: true) { $("[data-id='inventory-info']") }

        itemName { wrapper.find("[data-id='inventory-name']") }
        locationName { wrapper.find("[data-id='inventory-location-name']") }
        ndc { wrapper.find("[data-id='inventory-ndc']") }
        tai { wrapper.find("[data-id='inventory-tai-formatted']") }
        form { wrapper.find("[data-id='inventory-form-name']") }
        inventorySegment { wrapper.find("[data-id='inventory-segment-name']") }

        //lotNumber(required: false) { wrapper.find("[data-id='lot-number']") }
        //expirationDate(required: false) { wrapper.find("[data-id='expires-date-formatted']") }
        patientNameMRN(required: false) { wrapper.find("[data-id='patient-full-name-mrn']") }

    }

    def matchLotNumber(String matchText){
        def lotNumber = wrapper.find("[data-id='lot-number']")

        if((isEmptyNavigator(lotNumber) || lotNumber.text() == null) && matchText == null){
            return true
        }

        lotNumber.text().trim() == matchText
    }

    def matchExpirationDate(String matchText){
        def expDate = wrapper.find("[data-id='expires-date-formatted']")

        if((isEmptyNavigator(expDate) || expDate.text() == null) && matchText == null){
            return true
        }

        expDate.text().trim() == matchText
    }

    def matchItemName(String matchText){
        itemName.text().trim() == matchText
    }

    def matchLocationName(String matchText){
        locationName.text().trim() == matchText
    }

    def matchNDC(String matchText){
        ndc.text().trim() == matchText
    }

    def matchPatient(String matchText){
        def patientText = isNonEmptyNavigator(patientNameMRN) ? patientNameMRN.text().trim() : null

        (matchText == null && patientText == null) || patientText == matchText
    }

    def matchInventorySegmentType(String matchText){
        inventorySegment.text().trim() == matchText
    }

}

