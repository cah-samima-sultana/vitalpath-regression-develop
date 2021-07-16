package com.cardinalhealth.vitalpath.cabinet.specs

import com.cardinalhealth.vitalpath.cabinet.pages.BasePage
import com.cardinalhealth.vitalpath.cabinet.pages.login.SiteSelectionPage
import com.cardinalhealth.vitalpath.common.CommonBaseSpec
import spock.lang.Ignore

@Ignore
class BaseSpec extends CommonBaseSpec{

    def initConfig() {
        homePage = BasePage
        siteSelectionPage = SiteSelectionPage

    }

    def goToDispensePage(){
        at BasePage
        gotoDispense()
    }

    def goToInventoryPage(){
        at BasePage
        gotoInventory()
    }

    def goToMessagesPage() {
        at BasePage
        gotoMessages()
    }

    def refreshPage(){
        clickRefresh()
        waitFor ({at BasePage})
    }
}