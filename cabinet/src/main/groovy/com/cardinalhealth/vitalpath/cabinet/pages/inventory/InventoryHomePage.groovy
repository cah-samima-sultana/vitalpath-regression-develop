package com.cardinalhealth.vitalpath.cabinet.pages.inventory
import com.cardinalhealth.vitalpath.cabinet.modules.components.InventoryAdjustmentModule

class InventoryHomePage extends BaseInventoryPage {

    static url = "#/cabinet/inventory"
    static at = { waitFor {title == "IMS | Cabinet – Inventory – Index" } }


    static content = {
        addButton { $("div", name: "ADDButton") }
        searchInventoryCards(wait: true) { $("div .baseCard") }
        adjustment { module(new InventoryAdjustmentModule()) }

        transferOutButton(wait: true){ $("#transferButton") }
        transferInButton(wait: true){ $("#receivingButton") }
    }

    def clickAddButton(){
        clickId("addButton")
    }

    def clickTransferOutButton(){
        waitForAnimationToComplete()
        transferOutButton.click()
    }

    def verifySearchResult(searchText) {
        //This sleep is required
        sleep(1500)
        searchInventoryCards.size().times { idx ->
            try {
                def cardAsString = searchInventoryCards[idx].text().toString().toLowerCase()
                if(cardAsString.length() > 0) {
                    assert cardAsString.contains(searchText.toLowerCase()): "Could not find correct search text for '${searchText}'. Card data: [${cardAsString}]"
                }
            } catch (Exception e) {
            }
        }
        return true
    }
}
