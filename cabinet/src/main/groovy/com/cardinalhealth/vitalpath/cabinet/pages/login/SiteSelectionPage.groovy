package com.cardinalhealth.vitalpath.cabinet.pages.login

import com.cardinalhealth.vitalpath.cabinet.pages.BasePage

class SiteSelectionPage extends BasePage {

    static url = "#/cabinet/select"
    static at = { waitFor {title == "IMS | Cabinet – Select" } }

    static content = {
    	sites { $(SCROLL_CONTAINER_CLASS)}
        firstDemoSite { sites.children()[0] }
    	secondDemoSite { sites.children()[1] }
        dispenseHeaderLink { $('a', id:'dispenseHeaderLink')}
    }

    def selectCabinet(site, cabinet) {
    	$('data-id': "${site}-${cabinet}").click()
        waitFor { dispenseHeaderLink.displayed }
    }



    def selectSiteByName(name){
        enterTextByAttribute("type","text", name)
    }
}