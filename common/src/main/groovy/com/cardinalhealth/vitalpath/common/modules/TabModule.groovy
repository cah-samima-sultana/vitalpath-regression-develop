package com.cardinalhealth.vitalpath.common.modules

class TabModule extends CommonBaseModule {

    def tabWrapperClassName = "tab-widget"
    def tabRowClassName = "tab-row"
    def tabsMap = [:]

    static content = {
        wrapper(required: true, wait: true) { $(className(tabWrapperClassName)) }
        tabRow(required: true, wait: true) { wrapper.find(className(tabRowClassName))}
    }

    def addTab = { tabName, tabValue ->
        tabsMap.put(tabName, tabValue)
    }

    def doesHaveTab = {tab ->
        tabsMap.get(tab) != null
    }

    def openTab = { tab ->
        if (doesHaveTab(tab)) {
            def dataId = "[data-id='" + tab + "']"
            waitFor{ $(dataId).size() > 0 }
            waitForAnimationToComplete()
            $(dataId).click()
            //tabRow.find(className("tab-button")).find("span", text: contains(tabsMap.get(tab))).click()
            waitForAnimationToComplete()
        } else {
            false
        }
    }
}