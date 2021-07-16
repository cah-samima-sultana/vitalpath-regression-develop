package com.cardinalhealth.vitalpath.client.pages.inventory

import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.client.modules.CabinetPartParModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule

class DigParPage extends BasePage{

    static url = "#/client/inventory/digPars"
    static at = { waitFor { $(".cabinet-pars") } }

    static content = {
        siteSelector(required: true, wait: true) { module(new SelectorModule(selector: $('data-id': 'site-selector'))) }
        drugFamilySelector(required: true, wait: true) { module(new SelectorModule(selector: $('data-id': 'drug-family-selector'))) }
        drugFamilyTais(required: true, wait: true) { module(new SelectorModule(selector: $('data-id': 'drug-family-tais'))) }
        parTypes(required: true, wait: true) { module(new SelectorModule(selector: $('data-id': 'par-types'))) }

        cabinetPartPars {
            $(".filtered-list-detail-row").moduleList(CabinetPartParModule)
        }
    }

    def waitForDigHeaders(){
        waitFor(5, { $("[data-id='dig-header']").size() > 0 })
    }

    def orderPreferencesLinkCount(){
        $("[data-id='order-preferences']").size()
    }

    def preferredCount(){
        $(".par.preferred").size()
    }

    def digHeaderCount(){
        $("[data-id='dig-par-header']").size()
    }

    def bestPriceHeaderCount(){
        $(".price-widget.best-price").size()
    }

    def preferredHeaderCount(){
        $(".price-widget.preferred").size()
    }

    def getFirstBestPrice(){
        $(".par.best-price")[0]
    }

    def getFirstPreferred(){
        $(".par.preferred")[0]
    }

    def clickFirstShowAllShowLess(){
        $(".filtered-list-side").first().click()
    }

    def clickShowAllShowLess(navigator){
        navigator.find(".filtered-list-side").click()
    }

    def clickAndEditMin(navigator, min){
        navigator.find(".par-text-min")[0].click()
        navigator.find(".par-text-min input")[0] << min
    }

    def clickAndEditMax(navigator, max){
        navigator.find(".par-text-max")[0].click()
        navigator.find(".par-text-max input")[0] << max
    }

    def getPartNumberCount(){
        $(".part-number-row").size()
    }

    def getCabinetRow(locationId, cabinetName){
        def foundCabinet = null
        for(def cabinetPartPar : cabinetPartPars){
            if(cabinetPartPar.dataId == locationId && cabinetPartPar.cabinetName == cabinetName){
                foundCabinet = cabinetPartPar
                break
            }
        }

        return foundCabinet
    }

    def containsIgnoreCase(a, b){
        if(a == null || b == null){
            false
        } else {
            a.toUpperCase().contains(b.toUpperCase())
        }
    }

    def verifyDig(drugFamily, tai, uom, routeFamily){
        def headers = $("[data-id='dig-header']")

        boolean found = false
        for(def i=0; i<headers.size(); i++){
            def header = headers[i]

            def headerDrugFamily = header.find("[data-id='drug-family']").text()
            def headerTaiUom = header.find("[data-id='tai']").text()
            def headerRouteFamily = header.find("[data-id='route-family']").text()

            if(containsIgnoreCase(headerDrugFamily, drugFamily) && containsIgnoreCase(headerTaiUom, tai + " " + uom) && containsIgnoreCase(headerRouteFamily, routeFamily)){
                found = true
                break
            }
        }

        return found
    }

    def isShowingGrowler(message){
        def toastContainer = $("#toast-container")
        waitFor(5, { toastContainer.size() == 1 });
        toastContainer.find(".toast-message").text() == message
    }

    def waitForColumnHeaders(){
        waitFor({ $("[data-id='par-cabinet-header']").size() > 0 })
    }

    def findPartByNDC(ndc){
        def parts = $(".par.preferred")

        for(def part : parts){
            def partNDC = part.find("[data-id='ndc']").text()
            if(partNDC == ndc){
                return part
            }
        }
        return null
    }
}
