package com.cardinalhealth.vitalpath.cabinet.specs.dispense

import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.LabelPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class Labels extends BaseSpec {

    def patient
    def clinicalOrder
    def orderLine

    def setup(){
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, null)

        patient = patientService.makePatient()
        clinicalOrder = medicationOrderService.makeMedicationOrder(patient, PhysicianConstants.physician1, System.currentTimeMillis(), Session.loggedInSiteId)
        orderLine = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 10000, 'IV', clinicalOrder.id)
        dispenseService.dispenseItem(Session.loggedInSiteId, orderLine.id, InventorySegment.SITE_OWNED.id(), PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(),  ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.LABEL_TEMPLATE_DEFINITION, value = SettingsConstant.Labels.LABEL_TEMPLATE_7), @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE)])
    def "Label 7"() {
        given: "I have a dispensed order"
        to DispenseHomePage

        when: 'I go to order planning screen'
        clickRefreshButton()
        selectFirstCardByMrn(patient.accountNumber)
        at DispensePlanPage

        and: "I navigate to labels"
        clickLabelButton()
        at LabelPage

        then: "The template is label 7"
        isLabelTemplateByNumber("7")

        and: "The template menu displays Template 7"
        waitFor(3, { templateButton.click() })
        waitFor(3, { $(".label-filters").size() == 1 })

        and: "The selected template is 7"
        waitFor(3, { selectedTemplate.text().trim() == "Template 7" })
    }

}
