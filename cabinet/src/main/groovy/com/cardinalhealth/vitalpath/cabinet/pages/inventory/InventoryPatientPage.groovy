package com.cardinalhealth.vitalpath.cabinet.pages.inventory

import com.cardinalhealth.vitalpath.cabinet.modules.components.InventoryAdjustmentModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.utils.Item
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.utils.InventoryLocation

class InventoryPatientPage extends BaseInventoryPage {

    static url = "#/cabinet/inventory/patient"
    static at = { waitFor {title == "IMS | Cabinet – Inventory – Patient" } }


    static content = {
        adjustment { module(new InventoryAdjustmentModule()) }

        patientSearchBox(wait:true) { $('[data-id=patientSelector]') }
        patientSelectDropDown { module(new SelectorModule( selector : $(dataId("patientSelector")))) }

        emptyPatientInvenory(wait:true) { $('[data-id=emptyPatientInv]') }

        patientInventoryCards { $(class: "inventoryPatientCard") }
    }


    def searchAndSelectPatientByName(patient) {
        waitForAnimationToComplete()
        println "Searching for [$patient.lastName, $patient.firstName]"
        patientSelectDropDown.openSearchAndSelect("$patient.lastName, $patient.firstName")

    }

    def verifyNoPatientInventory(patient) {
        sleep(1000)
        def x = emptyPatientInventory.text()
        assert emptyPatientInvenory.text() == "$patient.lastName, $patient.firstName"
    }

    def verifyPatientInventory(patient, Item item, InventorySegment inventorySegment, InventoryLocation location, quantity){
        waitForAnimationToComplete(2500)
        def currentInventory = $(class: "inventoryPatientCard")
        assert currentInventory.size() > 0 : "No patient inventory found"
        def cardAsString = currentInventory[0].text().toString()
        assert cardAsString : "Could not find patient inventory card"
        assert cardAsString.contains(patient.firstName) : "Could not find correct patient first name"
        assert cardAsString.contains(patient.lastName) : "Could not find correct patient last name"
        assert cardAsString.contains(item.drugName()) : "Could not find correct drug"
        assert cardAsString.contains(inventorySegment.title()) : "Could not find correct drug type"
        assert cardAsString.contains(location.locationName()) : "Could not find correct location"
        assert cardAsString.contains(quantity) : "Could not find correct qty"
        return true

    }

    def verifyPatientInventory(patient, ItemConstants item, InventorySegment inventorySegment, Enum inventoryLocation, quantity){
        waitForAnimationToComplete(2500)
        def currentInventory = $(class: "inventoryPatientCard")
        assert currentInventory.size() > 0 : "No patient inventory found"
        def cardAsString = currentInventory[0].text().toString()
        assert cardAsString : "Could not find patient inventory card"
        assert cardAsString.contains(patient.firstName) : "Could not find correct patient first name"
        assert cardAsString.contains(patient.lastName) : "Could not find correct patient last name"
        assert cardAsString.contains(item.getName()) : "Could not find correct drug"
        assert cardAsString.contains(inventorySegment.title()) : "Could not find correct drug type"
        assert cardAsString.contains(inventoryLocation.displayPath()) : "Could not find correct location"
        assert cardAsString.contains(quantity) : "Could not find correct qty"
        return true

    }


}
