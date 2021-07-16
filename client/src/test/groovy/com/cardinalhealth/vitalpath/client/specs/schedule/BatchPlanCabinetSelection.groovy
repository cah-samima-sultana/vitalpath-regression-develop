package com.cardinalhealth.vitalpath.client.specs.schedule

import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.pages.schedule.ScheduleHomePage
import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class BatchPlanCabinetSelection  extends BaseSpec {
    def patient1

    def clinicalOrder1

    def setupOrder(siteId) {
        patient1 = patientService.makePatient()
        clinicalOrder1 = createMedicationOrder(siteId, patient1, PhysicianConstants.physician1);
        return true
    }

    def createMedicationOrder(siteId, patient, physician) {
        medicationOrderService.makeMedicationOrder(patient, physician, System.currentTimeMillis(), siteId)
    }

    def createMedicationOrderLine(orderId, lineNumber, drugFamilyId, quantity) {
        medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, 'IV', orderId)
    }

    def setSite2OnHandInventory() {
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(),
                Session.site2Id, PracticeConstants.Site_2_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET1.path(),
                ItemConstants.GEMZAR_1000mg_16729_0117_11.getNdc(), ItemConstants.GEMZAR_1000mg_16729_0117_11.getItemId(), 15000, null, null)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CENTRAL_DISPENSE, value = SettingsConstant.TRUE, location = PracticeConstants.SITE_2),
                          @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE)])
    def "I automatically see the inventory in a cabinet if only one cabinet has inventory"() {
        given: 'I am on schedule home page'
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.loggedInSiteId)
            createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 50000)
            setSite2OnHandInventory()
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        and: "The status of the line is not started"
            scheduleDetails.getRowByLineNumber(1).statusIsNotStarted()

        when: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        and: "I select a site that has inventory in only one cabinet"
            openAndSelectPlanSiteSelector(Session.site2)

        then: "The cabinet with an inventory location is automatically selected"
            verifyLinePlanCabinetSelection(PracticeConstants.CABINET_A)

        and: "I see plan items"
            scheduleDetails.batchPlan.hasPlanItems()

        and: "The plan item has the correct TAI and inventory on hand"
            def planItems = scheduleDetails.batchPlan.getPlanItems()
            assert(planItems[0].verifyTaiFormatted("1000 MG"))
            assert(planItems[0].verifyOnHandFormatted("15000 MG"))
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CENTRAL_DISPENSE, value = SettingsConstant.TRUE, location = PracticeConstants.SITE_2), @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE), @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE), @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I select a different drug and the cabinet selection remains the same"(){

        given: 'I am on schedule home page'
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.loggedInSiteId)
            createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.GEMZAR_1000mg_16729_0117_11.drugFamilyId, 50000)
            createMedicationOrderLine(clinicalOrder1.id, 2, ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 50000)
            scheduleSummary.clickRefreshButton()

        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        when: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        and: "I select Cabinet B"
            openAndSelectPlanCabinetSelector(PracticeConstants.CABINET_B)

        then: "I select the second drug on the order"
            scheduleDetails.getRowByLineNumber(2).clickRow()

        and: "The cabinet selector remains on cabinet B"
            verifyLinePlanCabinetSelection(PracticeConstants.CABINET_B)
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CENTRAL_DISPENSE, value = SettingsConstant.TRUE, location = PracticeConstants.SITE_1), @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE), @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I select a drug with no inventory"(){

        given: "I am at the schedule home page"
            goToSchedulePage()

        and: 'I have a medication order'
            setupOrder(Session.site1Id)
            createMedicationOrderLine(clinicalOrder1.id, 1, ItemConstants.DECADRON_100mg_63323_0516_10.drugFamilyId, 50000)
            scheduleSummary.clickRefreshButton()


        and: "I click on a medication order"
            scheduleSummary.openClinicalOrderById(clinicalOrder1.id)

        when: "I select the drug name"
            scheduleDetails.getRowByLineNumber(1).clickRow()

        and: "I select a site that has no inventory for the drug"
            openAndSelectPlanSiteSelector(Session.site2)

        and: "I select Cabinet A"
            openAndSelectPlanCabinetSelector(PracticeConstants.CABINET_A)

        then: "The no items message is displayed"
            verifyNoItemsMessage()
    }
}
