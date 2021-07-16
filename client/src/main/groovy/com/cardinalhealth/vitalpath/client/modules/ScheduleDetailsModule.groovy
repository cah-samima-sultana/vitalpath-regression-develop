package com.cardinalhealth.vitalpath.client.modules

import com.cardinalhealth.vitalpath.common.modules.ModalConfirmationModule
import com.cardinalhealth.vitalpath.common.modules.PdfViewerModule
import com.cardinalhealth.vitalpath.common.modules.TabModule
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

class ScheduleDetailsModule extends BaseModule {

    static content = {
        wrapper(wait: true) { $("data-id": "schedule-details") }

        clinicalOrderHeader(required: true, wait: true) { module(new ClinicalOrderHeaderModule()) }
        clinicalOrderLinesGrid(required: true, wait: true) { module(new ClinicalOrderLinesGridModule()) }

        historyCardByDataid(wait: true) { value ->
            $(dataId(value))
        }

        tabWidget(required: true, wait: true) { module(new TabModule(tabsMap: [dispenseReceipt: "Dispense Receipt"]))}
        pdfViewer { module(new PdfViewerModule())}

        confirmationDialog { module(new ModalConfirmationModule()) }
        batchPlan(required: false, wait: true) { module(new BatchPlanModule()) }
    }


    def findRowByLineNumber = {lineNumber ->
        wrapper.find(dataId("line-${lineNumber}"))
    }

    def getRowByLineNumber(lineNumber) {
        clinicalOrderLinesGrid.getRowByLineNumber(lineNumber)
    }


    @Override
    def findElement(Object attr, Object attrValue) {
        return super.findElement(attr, attrValue)
    }

    def clone(sourceId){
        WebElement target = browser.driver.findElement(By.className("order-line-drag-target"));

        def hCard = historyCardByDataid(sourceId)
        WebElement source = hCard.firstElement()


        (new Actions(browser.driver)).dragAndDrop(source, target).perform();
    }

    def clickAddLine() {
        clinicalOrderLinesGrid.clickAddButton()
    }

    def openDispenseReceipt() {
        tabWidget.openTab("dispenseReceipt")
    }

    def pdfContainsValue(value) {
        pdfViewer.doesContainValue(value)
    }

    def batchPlanIsPresent() {
        batchPlan.displayed
    }

    def verifyNoItemsMessageExists() {
        isNonEmptyNavigator($("data-id": "no-items-message"))
    }
}