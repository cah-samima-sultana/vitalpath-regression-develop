package com.cardinalhealth.vitalpath.cabinet.specs.dispense

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.PlanPickPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.InventorySegment
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PhysicianConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class ControlledSubstanceDispense extends BaseSpec {

    def patient
    def clinicalOrder
    def orderLine

    def setup(){
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.path(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.getNdc(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.getItemId(), 10000, null, null)

        patient = patientService.makePatient()
        clinicalOrder = medicationOrderService.makeMedicationOrder(patient, PhysicianConstants.physician1, System.currentTimeMillis(), Session.loggedInSiteId)
        orderLine = medicationOrderService.addMedicationLine(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.drugFamilyId, 1, 200, 'IV', clinicalOrder.id)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'I can dispense a controlled substance medication order with witness verification'(){
        EnvironmentProperties env = EnvironmentProperties.instance
        given: 'I have a controlled substance medication order line'
            goToDispensePage()
            clickRefreshButton()

        when: 'Select medication order for dispense'
            selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the dispense planning page'
            at DispensePlanPage

        and: 'I plan line for pick'
            planItemByLocationNdc(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.displayPath(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.ndc, 1)

        and: 'I start to pick the line'
            clickPlanPickButton()

        then: "I am at the plan pick page"
            at PlanPickPage

        then: "The controlled substance verification is displayed and I approve it"
            enterApproval(env.approverId(), env.approverPassword())

        and: 'I confirm the pick'
            confirmPlanPickItemByNdc(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.ndc)

        then: 'I am at the dispense planning page'
            at DispensePlanPage

        and: 'the line is complete'
            isLineComplete(1) == true

        and: 'the dispensed line values are correct'
            verifyLineQuantities(1, 2, 2, 0, 0, 'MG')

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value= SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'I can dispense a controlled substance medication order with witness verification off'(){
        EnvironmentProperties env = EnvironmentProperties.instance
        given: 'I have a controlled substance medication order line'
        goToDispensePage()
        clickRefreshButton()

        when: 'Select medication order for dispense'
        waitFor{ $(".medicationOrderCard").size() > 0 }
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the dispense planning page'
        at DispensePlanPage

        and: 'I plan line for pick'
        planItemByLocationNdc(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.displayPath(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.ndc, 1)

        and: 'I start to pick the line'
        waitFor{ $("[data-id='pick-button']").hasClass("isDisabled") == false }
        clickPlanPickButton()

        then: "I am at the plan pick page"
        at PlanPickPage

        and: 'I confirm the pick'
        confirmPlanPickItemByNdc(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.ndc)

        then: 'I am at the dispense planning page'
        at DispensePlanPage

        and: 'the line is complete'
        isLineComplete(1) == true

        and: 'the dispensed line values are correct'
        verifyLineQuantities(1, 2, 2, 0, 0, 'MG')

    }

}
