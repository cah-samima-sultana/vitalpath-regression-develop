package com.cardinalhealth.vitalpath.cabinet.specs.dispense
import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispenseHomePage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.DispensePlanPage
import com.cardinalhealth.vitalpath.cabinet.pages.dispense.PlanPickPage
import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.*
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import spock.lang.Unroll

class TreatmentDays extends BaseSpec {

    def TREATMENTDAYS_SELECTOR = "[data-id=treatment-days-notification]"

    static def locationId,
        patient,
        dispensedOrder,
        newOrder

    def offLabelId = InventorySegment.OFFLABEL.id(),
        patientAssistId = InventorySegment.PATIENT_ASSIST.id(),
        samplesId = InventorySegment.SAMPLES.id(),
        siteOwnedId = InventorySegment.SITE_OWNED.id(),
        whiteBagId  = InventorySegment.WHITE_BAG.id()

    def setupInventory() {
        // Site owned
        inventoryAdjustmentService.setOnhandInventory(siteOwnedId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.getNdc(), ItemConstants.GEMZAR_200mg_00002_7501_01.getItemId(), 20000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwnedId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.getNdc(), ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.getItemId(), 32500, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwnedId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.FLUOROURACIL_500mg_16729_0276_68.getNdc(), ItemConstants.FLUOROURACIL_500mg_16729_0276_68.getItemId(), 50000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwnedId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_2_POCKET_1.path(), ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), ItemConstants.ALOXI_200mcg_62856_0797_01.getItemId(), 20000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwnedId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.ELOXATIN_100mg_00024_0591_20.getNdc(), ItemConstants.ELOXATIN_100mg_00024_0591_20.getItemId(), 10000, null, null)
        inventoryAdjustmentService.setOnhandInventory(siteOwnedId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.getNdc(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.getItemId(), 15000, null, null)

        // Non drug items
        inventoryAdjustmentService.setOnhandInventory(siteOwnedId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_4.path(), ItemConstants.THERMAL_PAPER.getNdc(), ItemConstants.THERMAL_PAPER.getItemId(), 100, null, null)

        // Patient assist
        inventoryAdjustmentService.setOnhandInventory(patientAssistId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1_BIN_1_POCKET_1.path(), ItemConstants.ALOXI_200mcg_62856_0797_01.getNdc(), ItemConstants.ALOXI_200mcg_62856_0797_01.getItemId(), 20000, null, patient.id)

        //Samples
        inventoryAdjustmentService.setOnhandInventory(samplesId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.getNdc(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.getItemId(), 15000, null, null)

        //Off-label
        inventoryAdjustmentService.setOnhandInventory(offLabelId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.getNdc(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.getItemId(), 15000, null, null)

        // White bag
        inventoryAdjustmentService.setOnhandInventory(whiteBagId, locationId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.getNdc(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.getItemId(), 40000, null, patient.id)
    }


    def setupSpec() {
        def environmentProperties = EnvironmentProperties.instance
        settingsService.createOrUpdateSetting(SettingsConstant.BUSINESS_TYPE,SettingsConstant.RETINA,environmentProperties.tenantId(),null,null)
    }

    def setup(){
        locationId = Session.loggedInSiteId
        
        patient = patientService.makePatient()

        // A fully dispensed order with every combination
        setupInventory()

        dispensedOrder = medicationOrderService.makeMedicationOrder(patient, PhysicianConstants.physician1, System.currentTimeMillis(), locationId)

        // Order with lines to test the treatment days notification
        newOrder = medicationOrderService.makeMedicationOrder(patient, PhysicianConstants.physician1, System.currentTimeMillis(), locationId)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.BUSINESS_TYPE, value = SettingsConstant.RETINA, level='Practice'),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'Treatment Days Notification - Same drug family dispensed within treatment days'() {
        given: 'I have dispensed a drug item'
        def gemzarRight = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, OculusConstants.RIGHT.id)
        dispenseService.dispenseItem(locationId, gemzarRight.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)

        and: 'I have planned the same drug family for the same oculus'
        def gemzarRight2 = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', newOrder.id, OculusConstants.RIGHT.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.GEMZAR_200mg_00002_7501_01.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), locationId), siteOwnedId, null, 20000, locationId, gemzarRight2)

        and: 'I am at the dispense screen'
            at DispenseHomePage
            clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'I see the treatment days notification'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() > 0
        })
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'Treatment Days Notification - Different drug family dispensed within treatment days'() {
        given: 'I have dispensed a drug item'
        def gemzarRight = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, OculusConstants.LEFT.id)
        dispenseService.dispenseItem(locationId, gemzarRight.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)

        and: 'I have dispensed a different drug for the same oculus'
        def eloxatinLeft = medicationOrderService.addMedicationLine(ItemConstants.ELOXATIN_100mg_00024_0591_20.drugFamilyId, 5, 10000, 'IV', newOrder.id, OculusConstants.LEFT.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.ELOXATIN_100mg_00024_0591_20.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), locationId), siteOwnedId, null, 10000, locationId, eloxatinLeft)

        and: 'I am on the dispense screen'
        at DispenseHomePage
        clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'I see the treatment days notification'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() > 0
        })
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    @Unroll
    def "Treatment Days Notification - Oculus combinations / site-owned inventory (dispensed: #prevOculus.description, planned: #currOculus.description)"() {
        given: "I have dispensed a drug item with oculus #prevOculus.description"
        def gemzarBilateral = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, prevOculus.id)
        dispenseService.dispenseItem(locationId, gemzarBilateral.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)

        and: "I have dispensed a different drug with oculus #currOculus.description"
        def eloxatinLeft = medicationOrderService.addMedicationLine(ItemConstants.ELOXATIN_100mg_00024_0591_20.drugFamilyId, 5, 10000, 'IV', newOrder.id, currOculus.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.ELOXATIN_100mg_00024_0591_20.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), locationId), siteOwnedId, null, 10000, locationId, eloxatinLeft)

        and: 'I am on the dispense screen'
        at DispenseHomePage
        clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'The treatment days notification should show if the following is greater than 0 (showFlyup = #flyupDisplays)'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() == flyupDisplays
        })

        where: 'Flyup displays: #flyupDisplays | site-owned inventory (dispensed: #prevOculus.description, planned: #currOculus.description)'
        prevOculus                | currOculus                  || flyupDisplays
        OculusConstants.BILATERAL | OculusConstants.BILATERAL   || 1
        OculusConstants.BILATERAL | OculusConstants.LEFT        || 1
        OculusConstants.BILATERAL | OculusConstants.RIGHT       || 1
        OculusConstants.BILATERAL | OculusConstants.NONE        || 0
        OculusConstants.LEFT      | OculusConstants.BILATERAL   || 1
        OculusConstants.LEFT      | OculusConstants.LEFT        || 1
        OculusConstants.LEFT      | OculusConstants.RIGHT       || 0
        OculusConstants.LEFT      | OculusConstants.NONE        || 0
        OculusConstants.RIGHT     | OculusConstants.BILATERAL   || 1
        OculusConstants.RIGHT     | OculusConstants.RIGHT       || 1
        OculusConstants.RIGHT     | OculusConstants.NONE        || 0
        OculusConstants.NONE      | OculusConstants.RIGHT       || 0
        OculusConstants.NONE      | OculusConstants.BILATERAL   || 0
        OculusConstants.NONE      | OculusConstants.NONE        || 0
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'Treatment Days Notification - White bag items trigger the fly up'() {
        given: 'I have dispensed a white bag drug item'
        def gemzarRight = medicationOrderService.addMedicationLine(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, OculusConstants.RIGHT.id)
        dispenseService.dispenseItem(locationId, gemzarRight.id, whiteBagId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.ndc, 20000, null, patient.id)

        and: 'I have planned a white bag drug item'
        def gemzarRight2 = medicationOrderService.addMedicationLine(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.drugFamilyId, 1, 20000, 'IV', newOrder.id, OculusConstants.RIGHT.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), locationId), whiteBagId, patient.id, 20000, locationId, gemzarRight2)

        and: 'I am on the dispense screen'
        at DispenseHomePage
        clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        then: 'I plan the drug'
        planFirstLine()

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'I see the treatment days notification'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() > 0
        })
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'Treatment Days Notification - Non-drug items items do not trigger the fly up'() {
        given: 'I have dispensed a non-drug item'
        def thermalPaperBilateral = medicationOrderService.addMedicationLineNonDrug(ItemConstants.THERMAL_PAPER.itemId, 7, 100, dispensedOrder.id, OculusConstants.BILATERAL.id)
        dispenseService.dispenseItemNonDrug(locationId, thermalPaperBilateral.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_4.path(), ItemConstants.THERMAL_PAPER.itemId, 100)

        and: 'I have planned a non-drug item with a matching oculus'
        def thermalPaperBilateral2 = medicationOrderService.addMedicationLineNonDrug(ItemConstants.THERMAL_PAPER.itemId, 7, 100, newOrder.id, OculusConstants.BILATERAL.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.THERMAL_PAPER.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_4.path(), locationId), siteOwnedId, null, 100, locationId, thermalPaperBilateral2)

        and: 'I am at the dispense screen'
        at DispenseHomePage
        clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'I do not see the treatment days notification'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() == 0
        })
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'Treatment Days Notification - Patient assist items do not trigger the fly up'() {
        given: 'I have dispensed a drug item'
        def gemzarRight = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, OculusConstants.RIGHT.id)
        dispenseService.dispenseItem(locationId, gemzarRight.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)

        and: 'I have planned a patient assist drug item with a matching oculus'
        def aloxiRight = medicationOrderService.addMedicationLine(ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 1, 20000, 'IV', newOrder.id, OculusConstants.RIGHT.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.ALOXI_200mcg_62856_0797_01.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_2_POCKET_1.path(), locationId), patientAssistId, patient.id, 20000, locationId, aloxiRight)

        and: 'I am at the dispense screen'
        at DispenseHomePage
        clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        then: 'I plan the drug'
        planFirstLine()

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'I do not see the treatment days notification'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() == 0
        })
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'Treatment Days Notification - Samples item do not trigger the fly up'() {
        given: 'I have dispensed a drug item'
        def gemzarRight = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, OculusConstants.RIGHT.id)
        dispenseService.dispenseItem(locationId, gemzarRight.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)

        and: 'I have planned a sample drug item with a matching oculus'
        def doxorubicinRight = medicationOrderService.addMedicationLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 6, 15000, 'IV', newOrder.id, OculusConstants.RIGHT.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), locationId), samplesId, null, 15000, locationId, doxorubicinRight)

        and: 'I am at the dispense screen'
        at DispenseHomePage
        clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        then: 'I plan the drug'
        planFirstLine()

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'I do not see the treatment days notification'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() == 0
        })
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    def 'Treatment Days Notification - Off-label item do not trigger the fly up'() {
        given: 'I have dispensed a drug item'
        def gemzarRight = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, OculusConstants.RIGHT.id)
        dispenseService.dispenseItem(locationId, gemzarRight.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)

        and: 'I have planned an off-label drug item with a matching oculus'
        def doxorubicinRight = medicationOrderService.addMedicationLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 6, 15000, 'IV', newOrder.id, OculusConstants.RIGHT.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), locationId), offLabelId, null, 15000, locationId, doxorubicinRight)

        and: 'I am at the dispense screen'
        at DispenseHomePage
        clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        then: 'I plan the drug'
        planFirstLine()

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'I do not see the treatment days notification'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() == 0
        })
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[
                    @Setting(name = SettingsConstant.COMPLETED_FILTER, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.PLANNED_FILTER, value = SettingsConstant.TRUE),
                    @Setting(name = SettingsConstant.TREATMENT_DAYS, value = "5"),
                    @Setting(name = SettingsConstant.CONTROLLED_SUBSTANCE_VERIFICATION, value = SettingsConstant.FALSE),
                    @Setting(name = SettingsConstant.POP_INDIVIDUAL_DOORS, value = SettingsConstant.FALSE)])
    @Unroll
    def 'Treatment Days Notification - Cancel only removes overlapping drug items'() {
        given: 'I have dispensed several drug and non-drug items'
        def gemzar = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, gemzarDispense.id)
        dispenseService.dispenseItem(locationId, gemzar.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.GEMZAR_200mg_00002_7501_01.ndc, 20000)
        def acetaminophen = medicationOrderService.addMedicationLine(ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.drugFamilyId, 2, 32500, 'IV', dispensedOrder.id, acetaminophenDispense.id)
        dispenseService.dispenseItem(locationId, acetaminophen.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.ndc, 32500)
        def fluorouracilBilateral = medicationOrderService.addMedicationLine(ItemConstants.FLUOROURACIL_500mg_16729_0276_68.drugFamilyId, 3, 50000, 'IV', dispensedOrder.id, fluorouracilDispense.id)
        dispenseService.dispenseItem(locationId, fluorouracilBilateral.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), ItemConstants.FLUOROURACIL_500mg_16729_0276_68.ndc, 50000)
        def aloxiRight = medicationOrderService.addMedicationLine(ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 4, 20000, 'IV', dispensedOrder.id, aloxiDispense.id)
        dispenseService.dispenseItem(locationId, aloxiRight.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_2_POCKET_1.path(), ItemConstants.ALOXI_200mcg_62856_0797_01.ndc, 20000)
        def eloxatinNone = medicationOrderService.addMedicationLine(ItemConstants.ELOXATIN_100mg_00024_0591_20.drugFamilyId, 5, 10000, 'IV', dispensedOrder.id, eloxatinDispense.id)
        dispenseService.dispenseItem(locationId, eloxatinNone.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.ELOXATIN_100mg_00024_0591_20.ndc, 10000)
        def doxorubicinLeft = medicationOrderService.addMedicationLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 6, 15000, 'IV', dispensedOrder.id, doxorubicinDispense.id)
        dispenseService.dispenseItem(locationId, doxorubicinLeft.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.ndc, 15000)
        def thermalPaperBilateral = medicationOrderService.addMedicationLineNonDrug(ItemConstants.THERMAL_PAPER.itemId, 7, 100, dispensedOrder.id, thermalPaperDispense.id)
        dispenseService.dispenseItemNonDrug(locationId, thermalPaperBilateral.id, siteOwnedId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_4.path(), ItemConstants.THERMAL_PAPER.itemId, 100)
        def hydromorphone = medicationOrderService.addMedicationLine(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.drugFamilyId, 1, 20000, 'IV', dispensedOrder.id, hydromorphoneDispense.id)
        dispenseService.dispenseItem(locationId, hydromorphone.id, whiteBagId, PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.ndc, 20000, null, patient.id)

        and: 'I have planned a medication order containing drug and non-drug items, where several drugs overlap a treated oculus'
        def gemzarRight2 = medicationOrderService.addMedicationLine(ItemConstants.GEMZAR_200mg_00002_7501_01.drugFamilyId, 1, 20000, 'IV', newOrder.id, gemzarPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.GEMZAR_200mg_00002_7501_01.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), locationId), siteOwnedId, null, 20000, locationId, gemzarRight2)
        def acetaminophenLeft = medicationOrderService.addMedicationLine(ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.drugFamilyId, 2, 32500, 'IV', newOrder.id, acetaminophenPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.ACETAMINOPHEN_325mg_00536_3222_01.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), locationId), siteOwnedId, null, 32500, locationId, acetaminophenLeft)
        def fluorouracilLeft = medicationOrderService.addMedicationLine(ItemConstants.FLUOROURACIL_500mg_16729_0276_68.drugFamilyId, 3, 50000, 'IV', newOrder.id, fluorouracilPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.FLUOROURACIL_500mg_16729_0276_68.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_1_POCKET_1.path(), locationId), siteOwnedId, null, 50000, locationId, fluorouracilLeft)
        def aloxiBilateral = medicationOrderService.addMedicationLine(ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 4, 20000, 'IV', newOrder.id, aloxiPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.ALOXI_200mcg_62856_0797_01.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_2_POCKET_1.path(), locationId), siteOwnedId, null, 20000, locationId, aloxiBilateral)
        def eloxatinRight = medicationOrderService.addMedicationLine(ItemConstants.ELOXATIN_100mg_00024_0591_20.drugFamilyId, 5, 10000, 'IV', newOrder.id, eloxatinPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.ELOXATIN_100mg_00024_0591_20.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), locationId), siteOwnedId, null, 10000, locationId, eloxatinRight)
        def doxorubicinNone = medicationOrderService.addMedicationLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 6, 15000, 'IV', newOrder.id, doxorubicinPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_1.path(), locationId), siteOwnedId, null, 15000, locationId, doxorubicinNone)
        // non-drug
        def thermalPaperBilateral2 = medicationOrderService.addMedicationLineNonDrug(ItemConstants.THERMAL_PAPER.itemId, 7, 100, newOrder.id, thermalPaperPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.THERMAL_PAPER.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_4.path(), locationId), siteOwnedId, null, 100, locationId, thermalPaperBilateral2)
        // off-label
        def doxorubicinOL = medicationOrderService.addMedicationLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 6, 15000, 'IV', newOrder.id, doxorubicinPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), locationId), offLabelId, null, 15000, locationId, doxorubicinOL)
        // patient assist
        def aloxiPA = medicationOrderService.addMedicationLine(ItemConstants.ALOXI_200mcg_62856_0797_01.drugFamilyId, 1, 20000, 'IV', newOrder.id, aloxiPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.ALOXI_200mcg_62856_0797_01.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_1_SHELF_2_BIN_2_POCKET_1.path(), locationId), patientAssistId, patient.id, 20000, locationId, aloxiPA)
        // sample
        def doxorubicinSA = medicationOrderService.addMedicationLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.drugFamilyId, 6, 15000, 'IV', newOrder.id, doxorubicinPlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.DOXORUBICIN_HCL_150mg_00069_3033_20.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_3.path(), locationId), samplesId, null, 15000, locationId, doxorubicinSA)
        // whitebag
        def hydromorphone2 = medicationOrderService.addMedicationLine(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.drugFamilyId, 1, 20000, 'IV', newOrder.id, hydromorphonePlan.id)
        medicationOrderService.addMedicationOrderPlanLineFromOrderLine(ItemConstants.HYDROMORPHONE_DILAUDID2mg_59011_0442_10.itemId, inventoryLocationService.findInventoryLocationIdByPath(PracticeConstants.Site_1_Layout.CABINET_A_DOOR_2_SHELF_2.path(), locationId), whiteBagId, patient.id, 20000, locationId, hydromorphone2)


        and: 'I am at the dispense screen'
        at DispenseHomePage
        clickRefreshButton()

        when: 'Select the planned order'
        selectFirstCardByMrn(patient.accountNumber)

        then: 'I am on the plan page'
        at DispensePlanPage

        then: 'I plan the patient assist drug'
        planAllLines()

        when: 'I select the pick button'
        clickPlanPickButton()

        then: 'I see the treatment days notification'
        at PlanPickPage
        waitFor(5, {
            $(TREATMENTDAYS_SELECTOR).size() > 0
        })

        when: 'I click cancel'
        waitFor(5, {
            sleep(1000)
            return true
        })
        treatmentDays.cancelButton.click()

        then: 'Only #plannedLineCount items are planned'
        planPick.numberOfPlanPickItems() == plannedLineCount

        where: 'The matrix of drug combinations produces #plannedLineCount after cancelling overlapping drugs'
        gemzarDispense        << [OculusConstants.RIGHT,     OculusConstants.BILATERAL, OculusConstants.RIGHT]
        gemzarPlan            << [OculusConstants.RIGHT,     OculusConstants.BILATERAL, OculusConstants.NONE]
        acetaminophenDispense << [OculusConstants.LEFT,      OculusConstants.BILATERAL, OculusConstants.LEFT]
        acetaminophenPlan     << [OculusConstants.RIGHT,     OculusConstants.BILATERAL, OculusConstants.RIGHT]
        fluorouracilDispense  << [OculusConstants.BILATERAL, OculusConstants.BILATERAL, OculusConstants.BILATERAL]
        fluorouracilPlan      << [OculusConstants.LEFT,      OculusConstants.BILATERAL, OculusConstants.LEFT]
        aloxiDispense         << [OculusConstants.RIGHT,     OculusConstants.BILATERAL, OculusConstants.NONE]
        aloxiPlan             << [OculusConstants.BILATERAL, OculusConstants.BILATERAL, OculusConstants.BILATERAL]
        eloxatinDispense      << [OculusConstants.NONE,      OculusConstants.BILATERAL, OculusConstants.NONE]
        eloxatinPlan          << [OculusConstants.RIGHT,     OculusConstants.BILATERAL, OculusConstants.RIGHT]
        doxorubicinDispense   << [OculusConstants.LEFT,      OculusConstants.BILATERAL, OculusConstants.LEFT]
        doxorubicinPlan       << [OculusConstants.NONE,      OculusConstants.BILATERAL, OculusConstants.NONE]
        thermalPaperDispense  << [OculusConstants.BILATERAL, OculusConstants.BILATERAL, OculusConstants.RIGHT]
        thermalPaperPlan      << [OculusConstants.BILATERAL, OculusConstants.BILATERAL, OculusConstants.LEFT]
        hydromorphoneDispense << [OculusConstants.BILATERAL, OculusConstants.BILATERAL, OculusConstants.NONE]
        hydromorphonePlan     << [OculusConstants.NONE,      OculusConstants.BILATERAL, OculusConstants.RIGHT]
        plannedLineCount      << [7,                         5,                         7]

    }

    def cleanupSpec(){
        def environmentProperties = EnvironmentProperties.instance
        settingsService.createOrUpdateSetting(SettingsConstant.BUSINESS_TYPE,SettingsConstant.ONCOLOGY,environmentProperties.tenantId(),null,null)
    }



}
