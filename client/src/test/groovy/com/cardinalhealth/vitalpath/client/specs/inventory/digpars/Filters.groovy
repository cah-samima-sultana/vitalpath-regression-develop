package com.cardinalhealth.vitalpath.client.specs.inventory.digpars
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.extensions.feature.Login
import org.openqa.selenium.Keys

class Filters extends DigParBase {


    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Select Drug Family & TAI"() {

        given: 'I am on inventory drug pars page'
        and: 'have selected Inventory Drug Pars'
        distributorPartNumberService.setPartAsPreferred(Session.loggedInSiteId, ItemConstants.FLUOROURACIL_500mg_16729_0276_68.ndc)
        goToDigParPage()

        and: 'I select a drug family (5-FU : Fluorouracil)'
        drugFamilySelector.openSearchAndSelect("Fluorouracil")

        when: 'I select the TAI'
        then: "the values I see displayed only contain valid TAIs for that drug family selected (i.e. 500 MG, 1000 MG, 2500 MG, 5000 MG)"
        and: "I see a value for All"
        drugFamilyTais.optionsContains("All")
        drugFamilyTais.optionsContains("500 MG")
        drugFamilyTais.optionsContains("1000 MG")
        drugFamilyTais.optionsContains("2500 MG")
        drugFamilyTais.optionsContains("5000 MG")


        and: "I select the TAI of All"
        drugFamilyTais.selectedValueEquals("All")

        and: "The results are displayed"
        and: "The results contain the pars for all TAIs of the drug family selected"
        waitForDigHeaders()
        verifyDig("Fluorouracil", "500", "MG", "INJ")
        verifyDig("Fluorouracil", "1000", "MG", "INJ")
        verifyDig("Fluorouracil", "2500", "MG", "INJ")
        verifyDig("Fluorouracil", "5000", "MG", "INJ")


        and: "The results for Best Price display the cabinet name, on hand, Min, and Max for the drug family/TAI selected"
        def firstBestPrice = getFirstBestPrice()
        clickShowAllShowLess(firstBestPrice)
        waitFor(3, { firstBestPrice.find('[data-id="par-cabinet-header"]').size() > 0 })
        firstBestPrice.find('[data-id="par-on-hand-header"]').size() > 0
        firstBestPrice.find('[data-id="par-min-header"]').size() > 0
        firstBestPrice.find('[data-id="par-max-header"]').size() > 0

        and: "The results for Preferred display the cabinet name, on hand, Min, and Max for the drug family/TAI selected"
        def firstPreferred = getFirstPreferred()
        clickShowAllShowLess(firstPreferred)
        waitFor(3, { firstPreferred.find('[data-id="par-cabinet-header"]').size() > 0 })
        firstPreferred.find('[data-id="par-on-hand-header"]').size() > 0
        firstPreferred.find('[data-id="par-min-header"]').size() > 0
        firstPreferred.find('[data-id="par-max-header"]').size() > 0
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Select Drug Family & only 1 TAI for Drug"() {

        given: "I have selected the Inventory tab"
        and: "Have selected Inventory Drug Pars"
        goToDigParPage()

        when: "I enter a drug family that only has 1 TAI (ALOXI : Palonosetron HCl)"
        drugFamilySelector.openSearchAndSelect("Aloxi")

        then: "The TAI filter is defaulted to the TAI for that drug (250 MG)"
        drugFamilyTais.selectedValueEquals("250 MCG")

        and: "The results are displayed"
        waitForDigHeaders()
        verifyDig("Aloxi", "250", "MCG", "INJ")
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Site Filter & Select All Sites"() {

        given: "I have selected the Inventory tab"
        and: "have selected Inventory Drug Pars"
        goToDigParPage()

        and: "I have selected a drug family (5-FU : Fluorouracil)"
        drugFamilySelector.openSearchAndSelect("Fluorouracil")

        and: "I have selected a TAI (All)"
        drugFamilyTais.openSearchAndSelect("All")

        when: "I select a site"
        and: "the results are displayed"
        then: "the values in the filter are valid for the practice"
        waitForDigHeaders()
        verifyDig("Fluorouracil", "500", "MG", "INJ")
        verifyDig("Fluorouracil", "1000", "MG", "INJ")
        verifyDig("Fluorouracil", "2500", "MG", "INJ")
        verifyDig("Fluorouracil", "5000", "MG", "INJ")

        and: "option for ‘View All Sites’ is displayed in the filter"
        siteSelector.optionsContains("View All Sites")

        and: "I select ‘View All Sites’ to view all sites"
        siteSelector.openSearchAndSelect("View All Sites")

        and: "the filter for pars changes to ‘All Locations’"
        parTypes.selectedValueEquals("All Locations")
    }


    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Change Drug Filter & Clear TAI Filter"() {

        given: "I have selected Inventory Drug Pars"
        goToDigParPage()

        and: "I have generated the results"
        drugFamilySelector.openSearchAndSelect("Fluorouracil")
        drugFamilyTais.openSearchAndSelect("500 MG")

        when: "I select a different drug family (NEUPOGEN : Filgrastim)"
        drugFamilySelector.openSearchAndSelect("NEUPOGEN : Filgrastim")

        then: "the current value in the TAI filter is cleared"
        drugFamilyTais.selectedValueEquals("All")

        and: "there is more than 1 TAI for the new drug family selected"
        drugFamilyTais.optionsContains("300 MCG")
        drugFamilyTais.optionsContains("480 MCG")

        and: "the TAI filter changes to 'All'"
        drugFamilyTais.selectedValueEquals("All")
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Filters Remain Visible when Scrolling"() {

        given: "I am on the Inventory Drug Pars page"
        goToDigParPage()

        and: "I have generated pars for a drug family/TAI"
        drugFamilySelector.openSearchAndSelect("TYLENOL : Acetaminophen")
        drugFamilyTais.openSearchAndSelect("All")

        when: "I scroll"
        $(".dig-pars-list").last().click()

        then: "the filters at the top remain visible"
        siteSelector.displayed
        drugFamilySelector.displayed
        drugFamilyTais.displayed
        parTypes.displayed
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Select Tab to Switch Between Filters"() {

        given: "I have selected Inventory Drug Pars"
        goToDigParPage()

        and: "I have selected the site filter"
        siteSelector.open()

        when: "I select Tab"
        actions.sendKeys(Keys.TAB).build().perform()

        then: "I am brought to the Drug Family filter"
        drugFamilySelector.isOpen()

        and: "I select Tab"
        actions.sendKeys(Keys.TAB).build().perform()

        and: "I am brought to the TAI filter"
        drugFamilyTais.isOpen()

        and: "I select Tab"
        actions.sendKeys(Keys.TAB).build().perform()

        and: "I am brought to the Par filter"
        parTypes.isOpen()
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Select All Sites & Locations w/ PAR Levels Set"() {

        clearParts()
        cabinetPartParService.createCabinetPartPar(Session.loggedInSiteId, Session.site1CabinetBId, "2130003410-200/MG-INJ", 1, 10, null)

        given: "I have selected Inventory Drug Pars"
        goToDigParPage()

        and: "I select View All Sites"
        siteSelector.openSearchAndSelect("View All Sites")

        and: "All Locations is selected by default"
        parTypes.optionsContains("All Locations")

        and: "I select Locations w/ PAR Levels Set"
        parTypes.openSearchAndSelect("Locations w/ PAR Levels Set")

        and: "I select a drug family (Gemzar)"
        drugFamilySelector.openSearchAndSelect("GEMZAR")

        when: "I select a TAI (200 MG)"
        drugFamilyTais.openSearchAndSelect("200 MG")

        then: "the results are displayed"
        waitForDigHeaders()
        waitForColumnHeaders()
        def cabinetB = getCabinetRow(Session.loggedInSiteId, PracticeConstants.CABINET_B)

        cabinetB.verifyMinEquals("1")
        cabinetB.verifyMaxEquals("10")
        cabinetB.isNotAssigned()

        and: "the results are expanded to show only Locations w/ PAR Levels Set for the drug/TAI selected (Site 1/Cabinet B)"
        getCabinetRow(Session.loggedInSiteId, PracticeConstants.CABINET_A) == null

        and: "I can select Show all..."
        clickFirstShowAllShowLess()
        waitFor(3, {$(".filtered-list-side").first().text() == "Show less..."})

        and: "all locations display (Site 1/Cabinet A & Cabinet B)"
        waitForColumnHeaders()
        def cabinetA = getCabinetRow(Session.loggedInSiteId, PracticeConstants.CABINET_A)

        cabinetA.verifyMinEquals("0")
        cabinetA.verifyMaxEquals("0")
        cabinetA.isAssigned()

        and: "the results are sorted by site name"
        def locationNames = $("[data-id='par-location-name']")
        def prevLocation = null
        for(def locationName : locationNames){

            def locationText = locationName.text()

            if(prevLocation == null){
                prevLocation = locationText
                continue
            }

            prevLocation <= locationText

            prevLocation = locationText
        }

        and: "I can select Show less.."
        clickFirstShowAllShowLess()
        waitFor(3, {$(".filtered-list-side").first().text() == "Show all..."})

        and: "the results are minimized to show only Locations w/ PAR Levels Set for the drug/TAI selected (Site 1/Cabinet B)"
        def cabinetB2 = getCabinetRow(Session.loggedInSiteId, PracticeConstants.CABINET_B)

        cabinetB2.verifyMinEquals("1")
        cabinetB2.verifyMaxEquals("10")
        cabinetB2.isNotAssigned()

        getCabinetRow(Session.loggedInSiteId, PracticeConstants.CABINET_A) == null
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Select All Sites & Locations w/ Drug Assigned"() {

        clearParts()
        cabinetPartParService.createCabinetPartPar(Session.loggedInSiteId, Session.site1CabinetBId, "2130003410-200/MG-INJ", 1, 10, null)

        given: "I have selected Inventory Drug Pars"
        goToDigParPage()

        and: "I select View All Sites"
        siteSelector.openSearchAndSelect("View All Sites")

        and: "All Locations is selected by default"
        parTypes.optionsContains("All Locations")

        and: "I select Locations w/ Drug Assigned"
        parTypes.openSearchAndSelect("Locations w/ Drug Assigned")

        and: "I select a drug family (Gemzar)"
        drugFamilySelector.openSearchAndSelect("GEMZAR")

        when: "I select a TAI (200 MG)"
        drugFamilyTais.openSearchAndSelect("200 MG")

        then: "the results are displayed"
        waitForDigHeaders()

        and: "the results are expanded to show only Locations w/ Drug Assigned for the drug/TAI selected (Site 1/Cabinet A)"
        def notAssigned = cabinetPartPars.findAll { it.notAssigned.text() == "not assigned" }
        notAssigned.size() == 0

        and: "I can select Show all..."
        waitForColumnHeaders()
        clickFirstShowAllShowLess()
        waitFor({$(".filtered-list-side").first().text() == "Show less..."})

        and: "all locations display (Site 1/Cabinet A & Cabinet B)"
        ($("[data-id='par-location-name']").findAll { it.text() == Session.site1}).size() == 1
        ($("[data-id='par-location-name']").findAll { it.text() == Session.site2}).size() == 1

        and: "I can select Show less.."
        clickFirstShowAllShowLess()
        waitFor(3, {$(".filtered-list-side").first().text() == "Show all..."})

        and: "the results are minimized to show only Locations w/ Drug Assigned for the drug/TAI selected (Site 1/Cabinet A)"
        def notAssigned2 = cabinetPartPars.findAll { it.notAssigned.text() == "not assigned" }
        notAssigned2.size() == 0

    }
}
