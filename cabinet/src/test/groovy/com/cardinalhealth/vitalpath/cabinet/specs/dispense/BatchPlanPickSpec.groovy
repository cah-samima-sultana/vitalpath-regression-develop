package com.cardinalhealth.vitalpath.cabinet.specs.dispense
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.PlanPickPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class BatchPlanPickSpec extends BaseSpec {

    def patient1
    def patient2
    def clinicalOrder1
    def clinicalOrder2

    def setupOrders(multiple){
        setOnHandInventory()
        medicationOrderService.deleteAllPlansForSite(Session.loggedInSiteId, Session.loggedInCabinetId)

        patient1 = patientService.makePatient()
        clinicalOrder1 = createMedicationOrder(patient1, PhysicianConstants.physician1);
        setupTwoPlannedOrderLines(clinicalOrder1)

        if(multiple) {
            patient2 = patientService.makePatient()
            clinicalOrder2 = createMedicationOrder(patient2, PhysicianConstants.physician1);
            setupTwoPlannedOrderLines(clinicalOrder2)
        }

        return true
    }

    def createMedicationOrder(patient, physician) {
        medicationOrderService.makeMedicationOrder(patient, physician, System.currentTimeMillis(), Session.loggedInSiteId)
    }

    def setOnHandInventory() {
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 15000, null, null)
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), ItemConstants.ALOXI_200mcg_62856_0797_01.getItemId(), 15000, null, null)
    }

    def setupTwoPlannedOrderLines(clinicalOrder){
        createPlannedMedicationOrderLine(clinicalOrder, 1, ItemConstants.GEMZAR_200mg_00002_7501_01, 50000, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path())
        createPlannedMedicationOrderLine(clinicalOrder, 2, ItemConstants.ALOXI_200mcg_62856_0797_01, 60000, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path())

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

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I can view, skip and exit planned medication orders which are in descending date order"() {

        given: "I have a two planned medication orders for today and I am on the dispense home page"
            setupOrders(true)
            goToDispensePage()
            clickRefreshButton()

        and: "I sort by date in descending order"
            sortByDescendingDate()
            closeMenu()

        when: "I click on batch button"
           waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
           clickBatchButton()

        then: "I am at the batch pick page"
            at PlanPickPage

        and: "I see the batch pick header"
            skipUntilPatientFound(patient2.accountNumber)
            verifyBatchPickHeader()

        and: "The next patient name is displayed"
            verifyPatientNameMrnInHeader(patientFullNameWithMrn(patient2))

        and: "I find the inventory pick cards"
            verifyPlanItems()

        when: "I click the skip patient button"
            skipUntilPatientFound(patient1.accountNumber)

        then: "The next patient name is displayed"
            verifyPatientNameMrnInHeader(patientFullNameWithMrn(patient1))

        and: "I find the inventory pick card"
            verifyPlanItems()

        when: "I click the home button"
            clickHomeButton()

        then:"I am at the dispense home page"
            at DispenseHomePage
    }

    def verifyPlanItems(){
        findInventoryCardByName(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.displayPath())
        findInventoryCardByName(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.displayPath())
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I can view planned medication orders in descending last name order"() {
        setupOrders(true)
        def firstPatient = patient1
        def secondPatient = patient2
        if(patient1.lastName.compareTo(patient2.lastName) > 0){
            firstPatient = patient2
            secondPatient = patient1
        }

        given: "I have a two planned medication orders for today and I am on the dispense home page"
            goToDispensePage()
            clickRefreshButton()

        and: "I sort by patient descending order"
            sortByDescendingPatient()
            closeMenu()

        when: "I click on batch button"
            waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
            clickBatchButton()

        then: "I am at the batch pick page"
            at PlanPickPage

        and: "I see the batch pick header"
            verifyBatchPickHeader()

        and: "The next patient name is displayed"
            skipUntilPatientFound(firstPatient.accountNumber)
            verifyPatientNameMrnInHeader(patientFullNameWithMrn(firstPatient))

        when: "I click the skip patient button"
            skipUntilPatientFound(secondPatient.accountNumber)

        then: "The next patient name is displayed"
            verifyPatientNameMrnInHeader(patientFullNameWithMrn(secondPatient))

        and: "I click home button"
            clickHomeButton()

        and:"I am at the dispense home page"
            at DispenseHomePage

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I can confirm a pick card"() {
       given: "I have a planned medication order for today and I am on the dispense home page"
            setupOrders(false)
            goToDispensePage()
            clickRefreshButton()

        when: "I click on batch button"
            waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
            clickBatchButton()

        and: "I am at the batch pick page"
            at PlanPickPage
            planPick.skipUntilPatientFound(patient1.accountNumber)

        and: "I see 2 plan pick item cards"
            verifyNumberOfPlanPickItems(2)

        then: "I confirm the first plan pick item card"
            confirmPlanPickItemByNdc(ItemConstants.ALOXI_200mcg_62856_0797_01.ndc)

        and: "I cannot see the confirmed plan pick items"
            verifyNumberOfPlanPickItems(1)

        and: "I verify the status of the line is complete"
            def lineArray =  medicationOrderService.getMedicationOrderLine(clinicalOrder1.id, ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId,null)
            def completedLine = lineArray.size > 0 ? lineArray.get(0) : null;
            completedLine.status == "Complete"

        and: "I verify the dispensed quantity"
            completedLine.dispensedTai == 60000

        and: "I verify the administered quantity"
            completedLine.adminTai == 60000

        and: "I verify the waste quantity"
            completedLine.wasteTai == 0

        and: "I click home button"
            clickHomeButton()

        and:"I am at the dispense home page"
            at DispenseHomePage
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I can confirm all pick cards"() {

        given: "I have a planned medication order for today and I am on the dispense home page"
            setupOrders(false)
            goToDispensePage()
            clickRefreshButton()

        when: "I click on batch button"
            sortByAscendingDate()
            closeMenu()
            waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
            clickBatchButton()

        then: "I am at the batch pick page"
            at PlanPickPage
            skipUntilPatientFound(patient1.accountNumber)

        when: "I click the confirm all button"
            clickConfirmAllButton()

        then: "I am at the dispense home page"
            at DispenseHomePage

    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I cannot confirm a pick card if the onhand quantity is less than the planned quantity"() {
        given: "I have a planned medication order for today and I am on the dispense home page"
            setupOrders(false)
            goToDispensePage()
            clickRefreshButton()

        and: "On hand quantity is less than planned quantity"
            inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.site1Id, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), ItemConstants.ALOXI_200mcg_62856_0797_01.getItemId(), 0, null, null)

        when: "I click on batch button"
            waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
            clickBatchButton()

        then: "I am at the batch pick page"
            at PlanPickPage
            skipUntilPatientFound(patient1.accountNumber)

        and: "I should see the planned lines"
            verifyNumberOfPlanPickItems(2)

        and: "I click the confirm all button"
            clickConfirmAllButton()

        and: "I can see the item with not enough on-hand was not confirmed"
            verifyPickItemExistByNdc(ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc())
            false == verifyPickItemExistByNdc(ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc())
    }


    def createPlanWithMultipleItems(){
        setupOrders(false)
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 3000, null, null)
        inventoryAdjustmentService.setOnhandInventory(InventorySegment.SITE_OWNED.id(), Session.loggedInSiteId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_3_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7502_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7502_01.getItemId(), 3000, null, null)
        medicationOrderService.deletePlannedLineByOrder(clinicalOrder1.id)

        def orderLines = medicationOrderService.getMedicationOrderLine(clinicalOrder1.id, ItemConstants.GEMZAR_200mg_00002_7502_01.drugFamilyId, null)

        createMedicationOrderLinePlan(Session.loggedInSiteId, InventorySegment.SITE_OWNED.id(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 25000, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), orderLines.get(0))
        createMedicationOrderLinePlan(Session.loggedInSiteId, InventorySegment.SITE_OWNED.id(), ItemConstants.GEMZAR_200mg_00002_7502_01.ndc, 25000, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_3_POCKET_1.path(), orderLines.get(0))

        return true
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I can see that the plan lines for a clinical order line is deleted if one of the plan item is confirmed"() {
        given: "I have a planned medication order for today and I am on the dispense home page"
            goToDispensePage()
            createPlanWithMultipleItems()
            clickRefreshButton()

        when: "I click on batch button"
            sortByAscendingDate()
            closeMenu()
            waitFor{ $("[data-id='batchPickButton']").hasClass("isDisabled") == false }
            clickBatchButton()

        then: "I am at the batch pick page"
            at PlanPickPage
            skipUntilPatientFound(patient1.accountNumber)

        and: "I confirm one item from plan"
            confirmPlanPickItemByNdc(ItemConstants.GEMZAR_200mg_00002_7501_01.ndc)

        and: "I see the onhand and dispensed quantity for the not confirmed line doesn't change"
            verifyPickItemOnHandQuantity(ItemConstants.GEMZAR_200mg_00002_7502_01.ndc, 3000)

        and: "I see that the line status is In Progress "
            def lineArray =  medicationOrderService.getMedicationOrderLine(clinicalOrder1.id, ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, null)
            def inProgressLine = lineArray.size > 0 ? lineArray.get(0) : null;
            "In Progress".equalsIgnoreCase(inProgressLine.status)
    }


    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)
            ])
    def "I select batch button when there are no planned orders"(){
        given: "I have a two planned medication orders for today and I am on the dispense home page"
            setupOrders(true)
            goToDispensePage()
            clickRefreshButton()

        when: "I delete plans for all order lines"
            medicationOrderService.deleteAllPlansForSite(Session.loggedInSiteId, Session.loggedInCabinetId)
            clickRefreshButton()

        then: "I see the batch button is disabled"
            verifyBatchIsDisabled()
    }

    def patientFullNameWithMrn(patient){
        if(patient.lastName == null &&  patient.firstName == null &&  patient.middleName == null){
            return;
        }
        //Need to do returns conditionally because an extra space is getting added for ones without middlename
        if (patient.middleName == null || patient.middleName == ""){
            return  patient.lastName + "," + patient.firstName;
        }
        def fullName = patient.lastName + ", " + patient.firstName + " " + patient.middleName;
        if(patient.accountNumber != null || patient.accountNumber != ""){
            fullName = fullName + " (" + patient.accountNumber + ")"
        }
        return fullName
    }

}

