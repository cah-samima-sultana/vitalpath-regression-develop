package com.cardinalhealth.vitalpath.client.pages.inventory

import com.cardinalhealth.vitalpath.client.BasePage
import com.cardinalhealth.vitalpath.client.modules.PartEditorModule
import com.cardinalhealth.vitalpath.common.modules.SelectorModule

import java.text.NumberFormat

class OrderPreferencesPage extends BasePage{

    static url = "#/client/ordering/order-preferences"
    //static url = "#/client/inventory/order-preferences"

    //http://localhost:8080/latest/client/#/client/ordering/order-preferences?locationId=7150f0b4-14c7-4ecd-8865-140d69822e2f
    static at = { waitFor { $(".order-preferences") } }

    static content = {
        orderPrefSiteSelector(required: true, wait: true) { module(new SelectorModule(selector: $('data-id': 'site-selector'))) }
        digSelector(required: true, wait: true) { module(new SelectorModule(selector: $('data-id': 'dig-selector'))) }
        partEditorModule(required: true, wait: true) { module(new PartEditorModule()) }
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

    def clickSave(){
        $(".save-button").click()
    }

    def clickAddPart(){
        $(".add-part").click()
    }

    def clickCopyPars(){
        $("[data-id='copy']").click()
    }

    def isSaveDisabled(){
        $(".save-button").hasClass('isDisabled')
    }

    def isSaveExitDisabled(){
        $(".save-exit-button").hasClass('isDisabled')
    }

    def isCancelDisabled(){
        $(".cancel-button").hasClass('isDisabled')
    }

    def digTitleContains(String val){
        $("[data-id='dig-title']").text().toUpperCase().contains(val.toUpperCase())
    }

    def findBestPrice(){
        def unitCosts = $(".part-number-row").find("[data-id='unit-cost']")

        Float bestPrice = null
        for(def unitCost : unitCosts){
            float price = Float.parseFloat(unitCost.text().replace('$', ''));

            if(bestPrice == null || price < bestPrice){
                bestPrice = price
            }
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(bestPrice);
    }

    def findPartByNDC(ndc){
        def parts = $(".part-number-row")

        for(def part : parts){
            def partNDC = part.find("[data-id='ndc']").text()
            if(partNDC == ndc){
                return part
            }
        }
        return null
    }

    def findPartByPartNumber(searchPartNumber){
        def parts = $(".part-number-row")

        for(def part : parts){
            def partPartNumber = part.find("[data-id='part-number']").text()
            if(partPartNumber == searchPartNumber){
                return part
            }
        }
        return null
    }

    def getPartNumberCount(){
        $(".part-number-row").size()
    }

    def findPartNumber(searchPartNumber){
        def partNumbers = $("[data-id='part-number']")

        boolean found = false
        for(def i=0; i<partNumbers.size(); i++) {
            def partNumber = partNumbers[i].text()

            if(partNumber == searchPartNumber){
                found = true
                break
            }
        }
        return found
    }

    def switchOrderPrefSite(site){
        orderPrefSiteSelector.openSearchAndSelect(site)
        sleep(1250)
        true
    }

    def pageIsLoaded(){
        waitFor { $("[data-id='digpar-price-widget']")[0].displayed }
    }
}
