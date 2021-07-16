package com.cardinalhealth.vitalpath.common.modules

class PlanItemModule extends CommonBaseModule {

    static final String PLAN_ITEM_CARD_CLASS = "planItemCard"

    static content = {
        wrapper(wait: true) { $(".${PLAN_ITEM_CARD_CLASS}") }

    }

    def clickPlusButton() {
        clickDataId("plan-item-plus-button")
    }

    def clickMinusButton() {
        clickDataId("plan-item-minus-button")
    }

    def closeBottomShelf() {
        clickDataId("tap-to-close")
    }

    def getPlannedQuantity(){
        def plannedQuantityBox = $("[data-id=plan-planned-quantity]")
        def quantity = plannedQuantityBox.find("[data-id=box-quantity-formatted]")
        return quantity.text().toString()
    }

    def verifyPlannedQuantity(String quantityFormatted){
        getPlannedQuantity().contains(quantityFormatted)
    }

    def verifyItemName(value){
        assertTextContainsByDataId("inventory-name", value)
    }

    def verifyInventoryLocationName(value){
        assertTextContainsByDataId("inventory-location-name", value)
    }

    def isAtLocation(location){
        def actualElement = findElement('data-id', 'inventory-location-name')
        actualElement.text().toString().trim().equalsIgnoreCase(location)
    }
    def isNdcItem(ndc){
        def actualElement = findElement('data-id', 'inventory-ndc')
        actualElement.text().toString().contains(ndc)
    }

    def verifyNdc(value){
        assertTextContainsByDataId("inventory-ndc", value)
    }

    def verifyTaiFormatted(value){
        assertTextContainsByDataId("inventory-tai-formatted", value)
    }

    def verifyOnHandFormatted(value) {
        def onHand = $(dataId("plan-on-hand-quantity")).find(dataId("box-quantity-formatted")).text()
        onHand == value
    }

    def verifyInventorySegment(value){
        assertTextContainsByDataId("inventory-segment-name", value)
    }

    def verifyOnHandQuantity(value){
        assertTextContainsByDataId("plan-on-hand-quantity", value)
    }

    def verifyBannerText(value){
        assertTextContainsByDataId("message", value)
    }

    def verifyIncEnabled(){
        $("[data-id=plan-item-plus-button]").hasClass("isDisabled") == false
    }

    def verifyIncDisabled(){
        $("[data-id=plan-item-plus-button]").hasClass("isDisabled") == true
    }

    def verifyDecEnabled(){
        $("[data-id=plan-item-minus-button]").hasClass("isDisabled") == false
    }

    def verifyDecDisabled(){
        $("[data-id=plan-item-minus-button]").hasClass("isDisabled") == true
    }
}

