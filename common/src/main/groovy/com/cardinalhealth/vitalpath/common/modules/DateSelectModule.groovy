package com.cardinalhealth.vitalpath.common.modules

import org.openqa.selenium.Keys

class DateSelectModule extends CommonBaseModule {

    def dataId = ""

    static content = {
        wrapper(wait: true) { $(".dateSelectWidget") }

        previousButton(wait: true) { $("name" : "button-left")}
        pikaday(wait: true) { $("name" : "pikaday")}
        nextButton(wait: true) { $("name" : "button-right")}

        pikaCalender(required:false) { $(".pika-lendar")}
    }


    def previousDate(){
        previousButton.click()
    }

    def nextDate(){
        nextButton.click()
    }

    def open(){
        if(pikaday.displayed) {
           pikaday.click()
        }

        waitFor { pikaCalender.displayed }
    }

    def selectCurrentDate(){
        waitFor { $(":focus") }

        $(":focus") << Keys.ENTER
    }

    def clickCurrentDate(){
        waitFor { $(":focus") }

        $(":focus").click()
    }

}