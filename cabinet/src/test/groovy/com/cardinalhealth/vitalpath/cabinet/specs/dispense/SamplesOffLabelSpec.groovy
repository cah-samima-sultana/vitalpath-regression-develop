package com.cardinalhealth.vitalpath.cabinet.specs.dispense
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.modules.components.MedicationOrderCardModule
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.PlanPickPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.common.traits.CommonTrait
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login

//import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class SamplesOffLabelSpec extends BaseSpec implements CommonTrait {
    def offLabelPatient, samplesPatient

    def offLabelOrder, samplesOrder

    def setupSamplesOrder() {
        def siteId = Session.loggedInSiteId
        samplesPatient = patientService.makePatient()
        samplesOrder = createMedicationOrder(siteId, samplesPatient, PhysicianConstants.physician1)
        def samplesOrderLine = createMedicationOrderLine(samplesOrder.id, 1, ItemConstants.GEMZAR_200mg_00002_7501_01.getDrugFamilyId(), 100)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SAMPLES.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(),
                ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, null)

        createMedicationOrderLinePlan(siteId, InventorySegment.SAMPLES.id(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 100, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), samplesOrderLine, null)
    }

    def setupOffLabelOrder() {
        def siteId = Session.loggedInSiteId
        offLabelPatient = patientService.makePatient()
        offLabelOrder = createMedicationOrder(siteId, offLabelPatient, PhysicianConstants.physician1)
        def offLabelOrderLine = createMedicationOrderLine(offLabelOrder.id, 1, ItemConstants.GEMZAR_200mg_00002_7501_01.getDrugFamilyId(), 100)

        inventoryAdjustmentService.setOnhandInventory(InventorySegment.OFFLABEL.id(),
                Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(),
                ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, offLabelPatient.id)

        createMedicationOrderLinePlan(siteId, InventorySegment.OFFLABEL.id(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 100, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), offLabelOrderLine, offLabelPatient.id)
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
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "I can dispense a samples order"() {
        given: "I have a planned samples medication order for today and I am on the dispense home page"
        setupSamplesOrder()
        goToDispensePage()
        clickRefreshButton()

        when: 'Select medication order for dispense'
        selectFirstCardByMrn(samplesPatient.accountNumber)

        then: 'I am on the dispense planning page'
        at DispensePlanPage

        and: 'I start to pick the line'
        clickPlanPickButton()

        then: 'I am at the plan pick page'
        at PlanPickPage

        and: 'I confirm the pick'
        confirmPlanPickItemByNdc(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc)

        then: 'I am at the dispense planning page'
        at DispensePlanPage

        and: 'the line is complete'
        isLineComplete(1) == true

        and: "I only see the samples card"
        def planItems = getPlanItems()
        planItems.size() == 1
        def firstCard = planItems[0]
        isNonEmptyNavigator(firstCard.find(".fuse-sample-pills"))

        and: "I see the samples badge on the order line card"
        def firstOrderLine = medicationOrderPlan.orderLineContainer[0]
        isNonEmptyNavigator(firstOrderLine.find(".fuse-sample-pills"))

        when: "I go home"
        clickHomeButton()
        at DispenseHomePage
        clickRefreshButton()

        then: "The medication order card displays the samples badge as well"
        def firstOrder = getFirstCardByMrn(samplesPatient.accountNumber)
        isNonEmptyNavigator(firstOrder.find(".fuse-sample-pills"))
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE)
            ])
    def "I can dispense an off label order"() {
        given: "I have a planned samples medication order for today and I am on the dispense home page"
        setupOffLabelOrder()
        goToDispensePage()
        clickRefreshButton()

        when: 'Select medication order for dispense'
        selectFirstCardByMrn(offLabelPatient.accountNumber)

        then: 'I am on the dispense planning page'
        at DispensePlanPage

        and: 'I start to pick the line'
        clickPlanPickButton()

        then: 'I am at the plan pick page'
        at PlanPickPage

        and: 'I confirm the pick'
        confirmPlanPickItemByNdc(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc)

        then: 'I am at the dispense planning page'
        at DispensePlanPage

        and: 'the line is complete'
        isLineComplete(1) == true

        and: "I only see the off label card"
        def planItems = getPlanItems()
        planItems.size() == 1
        def firstCard = planItems[0]
        isNonEmptyNavigator(firstCard.find(".fuse-off-label"))

        and: "I see the off label badge on the order line card"
        def firstOrderLine = medicationOrderPlan.orderLineContainer[0]
        isNonEmptyNavigator(firstOrderLine.find(".fuse-off-label"))

        when: "I go home"
        clickHomeButton()
        at DispenseHomePage
        clickRefreshButton()

        then: "The medication order card displays the off label badge as well"
        def firstOrder = getFirstCardByMrn(offLabelPatient.accountNumber)
        isNonEmptyNavigator(firstOrder.find(".fuse-off-label"))
    }
}

