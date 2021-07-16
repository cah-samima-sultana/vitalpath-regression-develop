package com.cardinalhealth.vitalpath.common.modules

import org.openqa.selenium.By
import org.openqa.selenium.Keys

class QuantityModule extends CommonBaseModule  {

    def dataId = ""

    static content = {
        widget(wait: true) { $("data-id" : dataId) }

        input(wait: true) { widget.find(By.name("quantityInput"))}
    }

    def enterQuantity(value){
        input << value
    }

    def pressEnter(){
        input << Keys.ENTER
    }

}