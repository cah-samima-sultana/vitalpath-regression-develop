package com.cardinalhealth.vitalpath.extensions.feature

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.services.SettingsService

class Settings {

    String defaultSite
    String defaultCabinet
    Setting[] siteSettings

    def env = EnvironmentProperties.instance

    void apply() {
        applySiteSettings()
    }

    def applySiteSettings() {

        def settingsService = new SettingsService(env.servicesUrl(), env.restClientAuthId(), env.restClientAuthPwd())

        for (setting in siteSettings) {
            def siteId = setting.level().equalsIgnoreCase('practice') ? null : getSiteIdForSetting(setting)

            def existingSetting = settingsService.getSetting(setting.name(), env.tenantId(), siteId)

            if (existingSetting) {
                settingsService.updateSetting(existingSetting, setting.value())
            } else {
                settingsService.setSetting(setting.name(), setting.value(),
                        env.tenantId(), siteId)
            }
        }
    }

    def getSiteIdForSetting(setting){
        setting.location().isEmpty() ? (Session.site1.endsWith(defaultSite) ? Session.site1Id : Session.site2Id) :
                (Session.site1.endsWith(setting.location()) ? Session.site1Id : Session.site2Id)
    }

}
