package com.cardinalhealth.vitalpath.client.modules

import com.cardinalhealth.vitalpath.common.modules.PlanItemModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule

class BatchPlanModule extends BaseModule {

    def siteSelectorDataId = "site-selector"
    def cabinetSelectorDataId = "cabinet-selector"
    def chooseLocationMessageDataId = "choose-location-message"

    static content = {
        wrapper(wait: true) { $("data-id": "batch-plan") }

        siteSelector(required: true, wait: true) { module(new SelectorModule(selector: $(dataId(siteSelectorDataId)))) }
        cabinetSelector(required: true, wait: true) { module(new SelectorModule(selector: $(dataId(cabinetSelectorDataId)))) }

        chooseLocationMessage(required: false, wait: true) { $(dataId(chooseLocationMessageDataId)) }

        planItemContainer(wait: true) { $(dataId("plan-items")) }
        discardButton(required: false, wait: true) { $(dataId("discard-button"))}
        doneButton(required: false, wait: true) { $(dataId("done-button"))}
    }

    def selectedSiteEquals(value) {
        waitFor( { siteSelector.selectedValueEquals(value) })
    }

    def siteSelectorIsDisabled() {
        waitFor(50, { siteSelector.isDisabled() })
    }

    def siteSelectorIsEnabled() {
        waitFor( { siteSelector.isEnabled() })
    }

    def selectedCabinetEquals(value) {
        waitFor( { cabinetSelector.selectedValueEquals(value) })
    }

    def cabinetSelectorIsEnabled() {
        waitFor( { cabinetSelector.isEnabled() })
    }

    def cabinetSelectorIsDisabled() {
        waitFor( { cabinetSelector.isDisabled() })
    }

    def getChooseLocationMessage() {
        waitFor(5, { $(dataId(chooseLocationMessageDataId)).text() })
    }

    def selectSite(site) {
        siteSelector.openSearchAndSelect(site)
    }

    def selectCabinet(cabinet) {
        waitForAnimationToComplete()

        cabinetSelector.openSearchAndSelect(cabinet)
    }

    def discardButtonIsEnabled() {
        waitFor({ $(dataId("discard-button")).size() > 0 && $(dataId("discard-button") + ".isDisabled").size() == 0 })
    }

    def discardButtonIsDisabled() {
        waitFor({ $(dataId("discard-button") + ".isDisabled").size() == 1 })
    }

    def clickDiscardAllButton() {
        waitFor{ $(dataId("discard-button"))}.click()
    }

    def doneButtonIsEnabled() {
        waitFor( { $(dataId("done-button")).size() > 0 && $(dataId("done-button") + ".isDisabled").size() == 0 })

    }

    def doneButtonIsDisabled() {
        waitFor( { $(dataId("done-button") + ".isDisabled").size() == 1 })
    }

    def clickDoneButton() {
        waitFor( {doneButton.click()})
    }

    def hasPlanItems() {
        getPlanItemCount() > 0
    }

    def getSiteOptions() {
        waitFor( { siteSelector.getOptions() })
    }

    def getCabinetOptions() {
        waitFor( { cabinetSelector.getOptions() })
    }

    def Integer getPlanItemCount(){
        getPlanItems().size()
    }

    def ArrayList<PlanItemModule> getPlanItems(){
        getModules(planItemContainer, ".${PlanItemModule.PLAN_ITEM_CARD_CLASS}", PlanItemModule.class)
    }

    def isShowingGrowlMessage() {
        waitFor(5, { isNonEmptyNavigator($(".batch-plan-toast")) });
    }

    def isNotShowingGrowlMessage() {
        $('[data-id="patient-name"]').click() // Make sure toast is not being hovered over because of the last click on plus/minus
        waitFor(5, { isEmptyNavigator($(".batch-plan-toast")) });
    }
}