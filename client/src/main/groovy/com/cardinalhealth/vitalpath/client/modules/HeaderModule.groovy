package com.cardinalhealth.vitalpath.client.modules

import com.cardinalhealth.vitalpath.common.modules.SelectorModule


class HeaderModule extends BaseModule {

    def scheduleLink = 'schedule-link'
    def inventoryLink = 'inventory-link'
    def orderingLink = 'ordering-link'
    def billingLink = 'billing-link'
    def reportsLink = 'reports-link'
    def setupLink = 'setup-link'
    def setup2Link = 'setup2-link'
    def clinicalTrialLink = 'clinical-trial-link'
    def messagingLink = 'messaging-link'
    def settingsLink = 'settings-link'
    def siteSwitcherDataId = 'site-switcher'

    static content = {
        logo(wait: true) { $('id': 'cardinal-logo') }
        logoutLink(wait: true) { $('id': 'logout-link') }
        siteSwitcher(wait: true) { module(new SelectorModule(selector: $("[data-id='${siteSwitcherDataId}']"))) }
    }

    def isPresentScheduleLink(){
        assertTextValueById(scheduleLink, "SCHEDULE")
        assertHrefById(scheduleLink, 'href', '#/client/schedule')
    }

    def isPresentInventoryLink(){
        assertTextValueById(inventoryLink, "INVENTORY")
        assertHrefById(inventoryLink, 'href', '#/client/inventory')
    }

    def isPresentOrderingLink(){
        assertTextValueById(orderingLink, "ORDERING")
        assertHrefById(orderingLink, 'href', '#/client/ordering')
    }

    def isPresentBillingLink(){
        assertTextValueById(billingLink, "BILLING")
        assertHrefById(billingLink, 'href', '#/client/billing')
    }

    def isPresentReportsLink(){
        assertTextValueById(reportsLink, "REPORTS")
        assertHrefById(reportsLink, 'href', '#/client/reports')
    }

    def isPresentSetupLink(){
        assertTextValueById(setupLink, "SETUP")
        assertHrefById(setupLink, 'href', '/pcclient/setup/')
    }

    def isPresentSetup2Link(){
        assertTextValueById(setup2Link, "SETUP2")
        assertHrefById(setup2Link, 'href', '#/client/setup')
    }

    def isPresentClinicalTrialLink(){
        assertTextValueById(clinicalTrialLink, "CLINICAL TRIALS")
        assertHrefById(clinicalTrialLink, 'href', '/pcclient/clinicaltrial/')
    }

    def isPresentMessagingLink(){
        assertHrefById(messagingLink, 'href', '#/client/messages')
    }

    def isPresentSettingLink(){
        assertHrefById(settingsLink, 'href', '/pcclient/settings/')
    }

    def clickSchedule() {
        waitForAnimationToComplete()
        clickId(scheduleLink)
    }

    def clickInventory() {
        waitForAnimationToComplete()
        clickId(inventoryLink)
    }

    def selectSite(site) {
        siteSwitcher.openSearchAndSelect(site)
    }

    def clickSetup() {
        waitForAnimationToComplete()
        clickId(setup2Link)
    }

}