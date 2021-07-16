package com.cardinalhealth.vitalpath.client.specs.inventory.orderpreferences
import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class OrderPreferences extends BaseSpec {

    def setupSpec(){
        drugService.findNonItemDrug()
        employeeService.updateOrderPreferences(true)

        preventDefaultRefresh = true
    }

    def setup(){
        EnvironmentProperties env = EnvironmentProperties.instance
        def browserSite1 = env.browserVersion() + " " + PracticeConstants.SITE_1
        def browserSite1Id = locationService.getLocationId(browserSite1)

        def pars = cabinetPartParService.findParsByLocation(browserSite1Id)
        cabinetPartParService.deletePars(pars)

        distributorPartNumberService.deletePreferredParts(browserSite1Id)

        pars = cabinetPartParService.findParsByLocation(Session.site2Id)
        cabinetPartParService.deletePars(pars)

        distributorPartNumberService.deletePreferredParts(Session.site2Id)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)

    def "Save Copy From & Paste To & Keep Preferred Item"() {
        given: "I am on the Order Preferences Page"
        def part1 = distributorPartNumberService.findPartByLocationNdc(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        def part2 = distributorPartNumberService.findPartByLocationNdc(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc)
        distributorPartNumberService.setPartAsPreferred(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        cabinetPartParService.createCabinetPartPar(Session.loggedInSiteId, Session.site1CabinetAId, ItemConstants.GEMZAR_1000mg_16729_0117_11.digId, 1, 10, part1.id)
        goToOrderPreferencesPage()

        and: "I select the dig"
        digSelector.openSearchAndSelect("Gemzar")
        pageIsLoaded()

        and: "I select Copy PARs"
        clickCopyPars()

        and: "I see the copy pars component"
        waitFor({$("[data-id='copy-pars']").size() > 0})

        and: "I select Copy From"
        findPartByNDC(ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc).find("[data-id='copy-from-button']").click()

        and: "I select Paste To"
        findPartByNDC(ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc).find("[data-id='paste-to-button']").click()

        when: "I click the Save button"
        clickSave()

        then: "I see a dialog which prompts me to choose to discard or keep preferred & pars"
        waitFor(3, {$(".order-preferences-confirmation").size() == 1})

        and: "I choose to Keep Item Preferred"
        $("[data-id='confirmation-close-button']").click()

        and: "I see that my Copy From part remains preferred"
        waitFor({findPartByNDC(ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc).find(".fuse-checkbox-checked").size() == 1})

        and: "I see the copy from pars remain"
        cabinetPartParService.findParsByDistributorPartNumberId(part1.id).size() == 1

        and: "I see that my Paste To part is preferred"
        waitFor({findPartByNDC(ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc).find(".fuse-checkbox-checked").size() == 1})

        and: "I see that pars are added to Paste To part"
        def pars = cabinetPartParService.findParsByDistributorPartNumberId(part2.id)
        pars.size() == 1
        pars[0].min == 1
        pars[0].max == 10
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "Save Copy From & Paste To & discard Preferred Item and its pars"() {
        given: "I am on the Order Preferences Page"
        def part1 = distributorPartNumberService.findPartByLocationNdc(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        def part2 = distributorPartNumberService.findPartByLocationNdc(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc)
        distributorPartNumberService.setPartAsPreferred(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        cabinetPartParService.createCabinetPartPar(Session.loggedInSiteId, Session.site1CabinetAId, ItemConstants.GEMZAR_1000mg_16729_0117_11.digId, 1, 10, part1.id)
        goToOrderPreferencesPage()

        and: "I select the dig"
        digSelector.openSearchAndSelect("Gemzar")
        pageIsLoaded()

        and: "I select Copy PARs"
        clickCopyPars()

        and: "I see the copy pars component"
        waitFor({$("[data-id='copy-pars']").size() > 0})

        and: "I select Copy From"
        findPartByNDC(ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc).find("[data-id='copy-from-button']").click()

        and: "I select Paste To"
        findPartByNDC(ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc).find("[data-id='paste-to-button']").click()

        when: "I click the Save button"
        clickSave()

        then: "I see a dialog which prompts me to choose to discard or keep preferred & pars"
        waitFor(3, {$(".order-preferences-confirmation").size() == 1})

        and: "I choose to Discard Preferred and remove its pars"
        $("[data-id='confirmation-continue-button']").click()

        and: "I see that my Copy From part is not preferred"
        waitFor({findPartByNDC(ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc).find(".fuse-checkbox-blank").size() == 1})

        and: "I see the copy from pars are deleted"
        cabinetPartParService.findParsByDistributorPartNumberId(part1.id).size() == 0

        and: "I see that my Paste To part is preferred"
        waitFor({findPartByNDC(ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc).find(".fuse-checkbox-checked").size() == 1})

        and: "I see that pars are added to Paste To part"
        def pars = cabinetPartParService.findParsByDistributorPartNumberId(part2.id)
        pars.size() == 1
        pars[0].min == 1
        pars[0].max == 10
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "All Sites - Save Copy From & Paste To & Keep Preferred Item"() {
        given: "I am on the Order Preferences Page"
        def itemId = itemService.findItemByNDC(ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc).id
        def part1Site1 = distributorPartNumberService.findPartByLocationNdc(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        def part1Site2 = distributorPartNumberService.findPartByLocationNdc(Session.site2Id, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)

        def part2Site1 = distributorPartNumberService.findPartByLocationNdc(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc)
        def part2Site2 = distributorPartNumberService.addPart(Session.site2Id, ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc, itemId)
        distributorPartNumberService.setPartAsPreferred(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        distributorPartNumberService.setPartAsPreferred(Session.site2Id, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        cabinetPartParService.createCabinetPartPar(Session.loggedInSiteId, Session.site1CabinetAId, ItemConstants.GEMZAR_1000mg_16729_0117_11.digId, 1, 10, part1Site1.id)
        cabinetPartParService.createCabinetPartPar(Session.site2Id, Session.site2CabinetAId, ItemConstants.GEMZAR_1000mg_16729_0117_11.digId, 2, 20, part1Site2.id)
        goToOrderPreferencesPage()

        and: "I select all sites"
        orderPrefSiteSelector.openSearchAndSelect("All")

        and: "I select the dig"
        digSelector.openSearchAndSelect("Gemzar")
        pageIsLoaded()

        and: "I select Copy PARs"
        clickCopyPars()

        and: "I see the copy pars component"
        waitFor({$("[data-id='copy-pars']").size() > 0})

        and: "I select Copy From"
        findPartByPartNumber(part1Site1.partNumber).find("[data-id='copy-from-button']").click()

        and: "I select Paste To"
        findPartByPartNumber(part2Site1.partNumber).find("[data-id='paste-to-button']").click()

        when: "I click the Save button"
        clickSave()

        then: "I see a dialog which prompts me to choose to discard or keep preferred & pars"
        waitFor(3, {$(".order-preferences-confirmation").size() == 1})

        and: "I choose to Keep Item Preferred"
        $("[data-id='confirmation-close-button']").click()

        and: "I see that my Copy From part remains preferred"
        waitFor({findPartByPartNumber(part1Site1.partNumber).find(".preferred-order-text").hasClass("checked")})

        and: "I see the copy from pars remain"
        cabinetPartParService.findParsByDistributorPartNumberId(part1Site1.id).size() == 1
        cabinetPartParService.findParsByDistributorPartNumberId(part1Site2.id).size() == 1

        and: "I see that my Paste To part is preferred"
        waitFor({findPartByPartNumber(part2Site1.partNumber).find(".selected.preferred-count").text() == "2"})

        and: "I see that pars are added to Paste To part in Site 1"
        def pars = cabinetPartParService.findParsByDistributorPartNumberId(part2Site1.id)
        pars.size() == 1
        pars[0].min == 1
        pars[0].max == 10

        and: "I see that pars are added to Paste To part in Site 2"
        def site2Pars = cabinetPartParService.findParsByDistributorPartNumberId(part2Site2.id)
        site2Pars.size() == 1
        site2Pars[0].min == 2
        site2Pars[0].max == 20

        cleanup:"Delete the added distributor part number"
        distributorPartNumberService.deletePartById(part2Site2.id)
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A)
    def "All Sites - Save Copy From & Paste To & discard Preferred Item and its pars"() {
        given: "I am on the Order Preferences Page"
        def itemId = itemService.findItemByNDC(ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc).id
        def part1Site1 = distributorPartNumberService.findPartByLocationNdc(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        def part1Site2 = distributorPartNumberService.findPartByLocationNdc(Session.site2Id, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)

        def part2Site1 = distributorPartNumberService.findPartByLocationNdc(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc)
        def part2Site2 = distributorPartNumberService.addPart(Session.site2Id, ItemConstants.GEMZAR_1000mg_00069_3858_10.ndc, itemId)
        distributorPartNumberService.setPartAsPreferred(Session.loggedInSiteId, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        distributorPartNumberService.setPartAsPreferred(Session.site2Id, ItemConstants.GEMZAR_1000mg_16729_0117_11.ndc)
        cabinetPartParService.createCabinetPartPar(Session.loggedInSiteId, Session.site1CabinetAId, ItemConstants.GEMZAR_1000mg_16729_0117_11.digId, 1, 10, part1Site1.id)
        cabinetPartParService.createCabinetPartPar(Session.site2Id, Session.site2CabinetAId, ItemConstants.GEMZAR_1000mg_16729_0117_11.digId, 2, 20, part1Site2.id)
        goToOrderPreferencesPage()

        and: "I select all sites"
        orderPrefSiteSelector.openSearchAndSelect("All")

        and: "I select the dig"
        digSelector.openSearchAndSelect("Gemzar")
        pageIsLoaded()

        and: "I select Copy PARs"
        clickCopyPars()

        and: "I see the copy pars component"
        waitFor({$("[data-id='copy-pars']").size() > 0})

        and: "I select Copy From"
        findPartByPartNumber(part1Site1.partNumber).find("[data-id='copy-from-button']").click()

        and: "I select Paste To"
        findPartByPartNumber(part2Site1.partNumber).find("[data-id='paste-to-button']").click()

        when: "I click the Save button"
        clickSave()

        then: "I see a dialog which prompts me to choose to discard or keep preferred & pars"
        waitFor(3, {$(".order-preferences-confirmation").size() == 1})

        and: "I choose to Discard Preferred and delete its pars"
        $("[data-id='confirmation-continue-button']").click()

        and: "I see that my Copy From part is not preferred"
        waitFor({findPartByPartNumber(part1Site1.partNumber).find(".preferred-order-text").hasClass("checked") == false})

        and: "I see the copy from pars are deleted"
        cabinetPartParService.findParsByDistributorPartNumberId(part1Site1.id).size() == 0
        cabinetPartParService.findParsByDistributorPartNumberId(part1Site2.id).size() == 0

        and: "I see that my Paste To part is preferred"
        waitFor({findPartByPartNumber(part2Site1.partNumber).find(".selected.preferred-count").text() == "2"})

        and: "I see that pars are added to Paste To part in Site 1"
        def pars = cabinetPartParService.findParsByDistributorPartNumberId(part2Site1.id)
        pars.size() == 1
        pars[0].min == 1
        pars[0].max == 10

        and: "I see that pars are added to Paste To part in Site 2"
        def site2Pars = cabinetPartParService.findParsByDistributorPartNumberId(part2Site2.id)
        site2Pars.size() == 1
        site2Pars[0].min == 2
        site2Pars[0].max == 20

        cleanup:"Delete the added distributor part number"
        distributorPartNumberService.deletePartById(part2Site2.id)
    }
}