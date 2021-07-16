package com.cardinalhealth.vitalpath.cabinet.pages.inventory

import com.cardinalhealth.vitalpath.common.modules.Select2Module

class TransferPage extends BaseInventoryPage {

    static url = "#/cabinet/inventory/transferOrder"
    static at = { waitFor {title == "IMS | Cabinet – Inventory – TransferOrder – Index" } }


    static content = {
        transferBanner{ $(".fuse-trolley-send") }
        addTransferButton{ $("id":"addTransferButton")}
        //siteSelector{ $("id":"select2-drop-mask")}

        locationDropDown { module(new Select2Module(selectId: "s2id_siteSelectId")) }

        selectModule { module Select2Module}
    }

    def clickAddTransfer(){
        waitForAnimationToComplete()
        addTransferButton.click()
    }

    def searchAndSelectSite(site){
        waitForAnimationToComplete()
        locationDropDown.selectItem(site)
    }
}