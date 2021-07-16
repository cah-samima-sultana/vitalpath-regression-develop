package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule


class PlanPickItemModule extends BaseModule {

    static final String PLAN_PICK_ITEM_CARD_CLASS = "planPickItemCard"

    static content = {
        wrapper(wait: true) { $(".${PLAN_PICK_ITEM_CARD_CLASS}") }
    }

    def cardByText(attr, textValue) {
        $("${attr}", text: iContains(textValue) )
    }

    def clickEditButton() {
        clickName("editButton")
    }

    def clickLotNumberButton() {
        clickName("lotNumButton")
    }

    def clickCancelButton() {
        clickName("cancelButton")
    }

    def clickConfirmButton() {
        clickName("confirmButton")
    }

    def findPickItemByNdc(ndc){
        cardByText(dataId("ndc"), ndc)
    }

    def findPickItemByHandQuantity(value){
        cardByText(className("onHandQty"), value.toString())
    }

    def verifyPlannedQuantity(quantity){
        cardByText(dataId("quantity-box-widget"), quantity)
    }

    def verifyDispenseDetailLotNumber(lotNumber){
        cardByText(dataId("lot-number-detail"), lotNumber)
    }

    def verifyDispenseDetailExpirationDate(date){
        cardByText(dataId("expiration-date-detail"), date)
    }
}

