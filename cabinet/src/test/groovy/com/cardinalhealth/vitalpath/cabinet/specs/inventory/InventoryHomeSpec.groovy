package com.cardinalhealth.vitalpath.cabinet.specs.inventory

import com.cardinalhealth.vitalpath.cabinet.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting

class InventoryHomeSpec extends BaseSpec {

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CABINET_TO_CABINET_TRANSFER, value = SettingsConstant.FALSE)])
    def "I do not see the transfer buttons "(){
        when:
            gotoInventory()

        then: "I do not see the transfer buttons"
            navItemDoesNotExist('a[id="transferButton"]')
            navItemDoesNotExist('a[id="receivingButton"]')
    }

    @Login(site=PracticeConstants.SITE_1, cabinet=PracticeConstants.CABINET_A,
            siteSettings=[@Setting(name = SettingsConstant.CABINET_TO_CABINET_TRANSFER, value = SettingsConstant.TRUE)])
    def "I can see the transfer buttons "(){
        when:
            gotoInventory()

        then: "I do not see the transfer buttons"
            navItemExists('a[id="transferButton"]')
            navItemExists('a[id="receivingButton"]')
    }

}