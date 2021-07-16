package com.cardinalhealth.vitalpath.cabinet.specs.dispense

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.PlanPickPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting


class BatchPlanPickControlledSubstanceSpec extends BaseSpec {

    def patient1
    def clinicalOrder

    def setupOrders(){
        setOnHandInventory()
        medicationOrderService.deleteAllPlansForSite(Session.loggedInSiteId, Session.loggedInCabinetId)

        patient1 = patientService.makePatient()
        clinicalOrder = createMedicationOrder(patient1, PhysicianConstants.physician1);
        setupThreePlannedOrderLines(clinicalOrder)

        return true
    }

    def createMedicationOrder(patient, physician) {
        medicationOrderService.makeMedicationOrder(patient, physician, System.currentTimeMillis(), Session.loggedInSiteId)
    }

    def setOnHandInventory() {
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 200000, null, null)
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), ItemConstants.ALOXI_200mcg_62856_0797_01.getItemId(), 150000, null, null)
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.path(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.getNdc(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.getItemId(), 600, null, null)
    }

    def setupThreePlannedOrderLines(clinicalOrder){
        createPlannedMedicationOrderLine(clinicalOrder, 1, ItemConstants.GEMZAR_200mg_00002_7501_01, 50000, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path())
        createPlannedMedicationOrderLine(clinicalOrder, 2, ItemConstants.ALOXI_200mcg_62856_0797_01, 60000, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path())
        createPlannedMedicationOrderLine(clinicalOrder, 2, ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10, 200, PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.path())

        return true
    }

    def createPlannedMedicationOrderLine(order, lineNumber, ItemConstants item, quantity, inventoryLocation) {
        def orderLine = createMedicationOrderLine(order.id, lineNumber, item.drugFamilyId, quantity)
        createMedicationOrderLinePlan(order.locationId, InventorySegment.SITE_OWNED.id(), item.ndc, quantity, inventoryLocation, orderLine)
    }

    def createMedicationOrderLine(orderId, lineNumber, drugFamilyId, quantity) {
        medicationOrderService.addMedicationLine(drugFamilyId, lineNumber, quantity, 'IV', orderId)
    }

    def createMedicationOrderLinePlan(siteId, inventorySegmentId, ndc, plannedQuantity, inventoryLocation, orderLine) {
        def itemId = itemService.findItemIdByNDC(ndc)
        def inventoryLocationId = inventoryLocationService.findInventoryLocationIdByPath(inventoryLocation, siteId)

        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(itemId, inventoryLocationId, inventorySegmentId, null, plannedQuantity, Session.site1Id, orderLine)
    }

    def verifyPlanItems(){
        findInventoryCardByName(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.displayPath())
        findInventoryCardByName(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath())
        findInventoryCardByName(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.displayPath())
    }

    def patientFullNameWithMrn(patient){
        if(patient.lastName == null &&  patient.firstName == null &&  patient.middleName == null){
            return;
        }
        //Need to do returns conditionally because an extra space is getting added for ones without middlename
        if (patient.middleName == null || patient.middleName == ""){
            return patient.firstName + " " + patient.lastName;
        }
        def fullName = patient.firstName + " " + patient.middleName + " "+ patient.lastName;
        if(patient.accountNumber != null || patient.accountNumber != ""){
            fullName = fullName + " (" + patient.accountNumber + ")"
        }
        return fullName
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I can witness and dispense narcotic orders when the controlled substance setting is turned on"() {
        EnvironmentProperties env = EnvironmentProperties.instance
        given: "I have a planned medication order for today and I am on the dispense home page"
        setupOrders()
        goToDispensePage()
        clickRefreshButton()

        and: "I accessed the Batch Pick Screen"
        waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
        clickBatchButton()

        and: "I am at the batch pick page"
        at PlanPickPage
        verifyBatchPickHeader()

        and: "I see the three inventory drug pick cards"
        verifyPlanItems()

        when: "I see the witness verification fly up and I witness it"
        enterApproval(env.approverId(), env.approverPassword())
        then:"I can confirm all the cards"
        clickConfirmAllButton()

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I can dispense the non narcotic drugs when I dont witness"() {
        EnvironmentProperties env = EnvironmentProperties.instance
        given: "I have a planned medication order for today and I am on the dispense home page"
        setupOrders()
        goToDispensePage()
        clickRefreshButton()

        and: "I accessed the Batch Pick Screen"
        waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
        clickBatchButton()

        and: "I am at the batch pick page"
        at PlanPickPage
        verifyBatchPickHeader()

        and: "I see the three inventory drug pick cards"
        verifyPlanItems()

        when: "The witness verification fly up displays and I cancel it"
        clickApproverCancelButton()
        then:"I remain on the batch pick screen"
        verifyBatchPickHeader()
        and: "I can confirm all cards except the narcotic"
        clickConfirmAllButton()
        verifyNumberOfPlanPickItems(1)
        findInventoryCardByName(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.displayPath())
        and: "I select Reopen"
        clickReopenButton()
        and: "The witness verification fly up displays and I cancel it"
        clickApproverCancelButton()
        and: "The inventory drug pick card with the narcotic still cannot be dispensed"
        clickConfirmAllButton()
        verifyNumberOfPlanPickItems(1)
        findInventoryCardByName(PracticeConstants.Site_1_Layout.CABINET_A_DRAWER_8_POCKET_1.displayPath())
        and: "I select Reopen again"
        clickReopenButton()
        and: "The witness verification displays and I enter valid witness credentials"
        enterApproval(env.approverId(), env.approverPassword())

        waitFor{ $("[data-id='approver-authentication-widget']").size() == 0 }

        and: "I can confirm the narcotic inventory drug pick card"
        confirmFirstPlanPickItem()


    }

}

