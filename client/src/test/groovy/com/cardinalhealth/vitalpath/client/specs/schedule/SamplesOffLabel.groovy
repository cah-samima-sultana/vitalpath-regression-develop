package com.cardinalhealth.vitalpath.client.specs.schedule

import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.pages.schedule.ScheduleHomePage
import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.common.traits.CommonTrait
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class SamplesOffLabel extends BaseSpec implements CommonTrait {
    def offLabelPatient, samplesPatient

    def offLabelOrder, samplesOrder

    def setupSamplesOrder(siteId) {
        samplesPatient = patientService.makePatient()
        samplesOrder = createMedicationOrder(siteId, samplesPatient, PhysicianConstants.physician1)
        def samplesOrderLine = createMedicationOrderLine(samplesOrder.id, 1, ItemConstants.GEMZAR_200mg_00002_7501_01.getDrugFamilyId(), 100)
        createMedicationOrderLinePlan(siteId, InventorySegment.SAMPLES.id(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 100, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), samplesOrderLine, null)
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SAMPLES.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(),
                ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, null)
    }

    def setupOffLabelOrder(siteId) {
        offLabelPatient = patientService.makePatient()
        offLabelOrder = createMedicationOrder(siteId, offLabelPatient, PhysicianConstants.physician1)
        def offLabelOrderLine = createMedicationOrderLine(offLabelOrder.id, 1, ItemConstants.GEMZAR_200mg_00002_7501_01.getDrugFamilyId(), 100)
        createMedicationOrderLinePlan(siteId, InventorySegment.OFFLABEL.id(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 100, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), offLabelOrderLine, offLabelPatient.id)
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.OFFLABEL.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(),
                ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, offLabelPatient.id)
    }

    def createMedicationOrder(siteId, patient, physician) {
        medicationOrderService.makeMedicationOrder(patient, physician, System.currentTimeMillis(), siteId)
    }

    def createMedicationOrderLine(orderId, lineNumber, drugFamilyId, quantity) {
        medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, 'IV', orderId)
    }

    def createMedicationOrderLinePlan(siteId, inventorySegmentId, ndc, plannedQuantity, inventoryLocation, orderLine, patientId) {
        def itemId = itemService.findItemIdByNDC(ndc)
        def inventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(inventoryLocation, siteId)

        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(itemId, inventoryLocationId, inventorySegmentId, patientId, plannedQuantity, Session.loggedInSiteId, orderLine)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I do not see a samples badge on a planned medication order"() {

        given: 'I am on schedule home page'
        to ScheduleHomePage
        setupSamplesOrder(Session.loggedInSiteId)

        when: 'I refresh the page and have a planned samples medication order'
            scheduleSummary.clickRefreshButton()

        then: "I do not see a samples badge on that order line"
            def samplesCard = scheduleSummary.findCardByMrn(samplesPatient)
            isEmptyNavigator(samplesCard.find(".badgeContainer .fuse-sample-pills"))
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I see an off-label badge on a planned medication order"() {

        given: 'I am on schedule home page'
        to ScheduleHomePage
        setupOffLabelOrder(Session.loggedInSiteId)

        when: 'I refresh the page and have a planned off-label medication order'
        scheduleSummary.clickRefreshButton()

        then: "I see a samples badge on that order line"
        def offLabelCard = scheduleSummary.findCardByMrn(offLabelPatient)
        isNonEmptyNavigator(offLabelCard.find(".badgeContainer .fuse-off-label"))
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I don't see a samples badge after I have planned a samples medication order and then unplanned the order"() {
        given: 'I am on schedule home page'
        goToSchedulePage()
        setupSamplesOrder(Session.loggedInSiteId)

        and: 'I refresh the page and have a planned samples medication order'
        scheduleSummary.clickRefreshButton()

        and: 'I click on the medication order card'
        scheduleSummary.selectCardByMrn(samplesPatient)

        and: 'I see the planned samples medication order line in the pane on the right'
        def plannedSamplesLine = scheduleDetails.getRowByLineNumber(1)
        plannedSamplesLine.find('[data-id="status-column"]').text().trim() == "Planned"

        when: 'I delete the plan the MO with the sample'
        plannedSamplesLine.clickRow()
        scheduleDetails.batchPlan.clickDiscardAllButton()
        waitFor {confirmationModal.isOnScreen()}
        confirmationModal.clickContinueButton()

        then: 'I cannot see the samples badge in the MO card and I cannot see the samples badge on the MOL pane on the right'
        def samplesCard = scheduleSummary.findCardByMrn(samplesPatient)
        isEmptyNavigator(samplesCard.find(".badgeContainer .fuse-sample-pills"))

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.NOT_STARTED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.IN_PROGRESS_FILTER, value = SettingsConstant.TRUE)])
    def "I don't see a off-label badge after I have planned an off-label medication order and then unplanned the order"() {
        given: 'I am on schedule home page'
        goToSchedulePage()
        setupOffLabelOrder(Session.loggedInSiteId)

        and: 'I refresh the page and have a planned off-label medication order'
        scheduleSummary.clickRefreshButton()
        isNonEmptyNavigator(scheduleSummary.findCardByMrn(offLabelPatient).find(".badgeContainer .fuse-off-label"))

        and: 'I click on the medication order card'
        scheduleSummary.selectCardByMrn(offLabelPatient)

        and: 'I see the planned samples medication order line in the pane on the right'
        def plannedOffLabelLine = scheduleDetails.getRowByLineNumber(1)
        plannedOffLabelLine.find('[data-id="status-column"]').text().trim() == "Planned"

        when: 'I delete the plan the MO with the off-label'
        plannedOffLabelLine.clickRow()
        scheduleDetails.batchPlan.clickDiscardAllButton()
        waitFor {confirmationModal.isOnScreen()}
        confirmationModal.clickContinueButton()

        then: 'I cannot see the off-label badge in the MO card and I cannot see the off-label badge on the MOL pane on the right'
        def offLabelCard = scheduleSummary.findCardByMrn(offLabelPatient)
        isNonEmptyNavigator(offLabelCard.find(".badgeContainer .fuse-off-label"))
    }
}
