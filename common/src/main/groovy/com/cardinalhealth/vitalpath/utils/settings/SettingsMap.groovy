package com.cardinalhealth.vitalpath.utils.settings

class SettingsMap {

    def settingsToSet = []

    def SettingsMap(List settings) {
        settings.each {
            assert SettingBase.isAssignableFrom(it.getClass())
            addSetting(it)
        }
    }

    def addSetting(setting){

        def obj = [
                settingDefinitionId: setting.getSettingDefinitionId(),
                currentValue: setting.getValue(),
                tenantId: setting.getTenantId(),
                locationId: setting.getLocationId(),
                keyId: setting.getKeyId()
        ]

        settingsToSet.push(obj)
    }
}
