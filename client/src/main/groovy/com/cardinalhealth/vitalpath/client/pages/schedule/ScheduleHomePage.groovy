package com.cardinalhealth.vitalpath.client.pages.schedule
import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.client.modules.ScheduleDetailsModule
import com.cardinalhealth.vitalpath.client.modules.ScheduleSummaryModule

class ScheduleHomePage extends BasePage{

    static url = "#/client/schedule"
    static at = { waitFor { $("data-id" : "schedule-summary-component") } }

    static content = {

        scheduleSummary { module(new ScheduleSummaryModule()) }
        scheduleDetails { module(new ScheduleDetailsModule()) }

    }

    def verifyNoOrdersMessage() {
        scheduleSummary.verifyNoOrdersMessageExists()
    }

    def openClinicalOrderByPatient(patient){
        scheduleSummary.selectCardByMrn(patient)
    }

    def openAndSelectPlanSiteSelector(site) {
        scheduleDetails.batchPlan.selectSite(site)
    }

    def openAndSelectPlanCabinetSelector(cabinet) {
        scheduleDetails.batchPlan.selectCabinet(cabinet)
    }

    def verifyLinePlanSiteSelection(site){
        scheduleDetails.batchPlan.selectedSiteEquals(site)
    }

    def verifyLinePlanCabinetSelection(cabinet){
        scheduleDetails.batchPlan.selectedCabinetEquals(cabinet)
    }

    def openAndReturnCabinetSelectorChildren() {
        scheduleDetails.batchPlan.getCabinetOptions()
    }

    def verifyNoItemsMessage() {
        scheduleDetails.verifyNoItemsMessageExists()
    }
}
