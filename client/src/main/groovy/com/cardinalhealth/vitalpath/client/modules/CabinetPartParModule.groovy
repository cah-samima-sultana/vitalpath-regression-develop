package com.cardinalhealth.vitalpath.client.modules

class CabinetPartParModule extends BaseModule {

    static content = {
        dataId { $().getAttribute("data-id") }

        cabinetName(required:true, wait: true) { $("[data-id='cabinetName']").text() }
        parMin(required:true, wait: true) { $(".par-text-min") }
        parMax(required:true, wait: true) { $(".par-text-max") }
        notAssigned(required: false) { $(".not-assigned") }
    }

    def verifyMinEquals(value){
        parMin.text() == value
    }

    def verifyMaxEquals(value){
        parMax.text() == value
    }

    def isAssigned(){
        notAssigned.displayed == false
    }

    def isNotAssigned(){
        notAssigned.displayed == true
    }

}