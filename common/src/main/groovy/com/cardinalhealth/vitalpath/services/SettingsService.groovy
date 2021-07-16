package com.cardinalhealth.vitalpath.services

import com.cardinalhealth.vitalpath.utils.settings.SettingsMap
import groovy.json.JsonSlurper
import org.apache.commons.lang.StringUtils

class SettingsService extends BaseService{


    SettingsService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd){
      super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def removeAllSiteSettings(siteId) {
        def settings = get("/masterdata/settings?locationId=${siteId}")
        settings.json.each { setting ->
            removeSetting(setting)
        }
    }

    def removeSetting(def setting) {
        delete("/masterdata/settings/$setting.id")
    }

    def setSetting(settingDefinitionId, value, tenantId, locationId=null, keyId=null) {
        return post('/masterdata/settings') {
            json 'settingDefinitionId': settingDefinitionId,
                    'tenantId': tenantId,
                    'keyId': keyId,
                    'locationId':locationId,
                    'currentValue':value
        }
    }

    def updateSetting(existingSetting, value) {
        return put("/masterdata/settings/${existingSetting.id}") {
            json 'settingDefinitionId': existingSetting.settingDefinitionId,
                    'tenantId': existingSetting.tenantId,
                    'keyId': existingSetting.keyId,
                    'locationId': existingSetting.locationId,
                    'currentValue':value,
                    'id': existingSetting.id,
                    'version': existingSetting.version
        }
    }


    def getSetting(settingDefinitionId, tenantId, locationId=null, keyId=null){
        def url = "/masterdata/settings?tenantId=${tenantId}&settingDefinitionId=${settingDefinitionId}"

        if(StringUtils.isNotEmpty(locationId)){
            url += "&locationId=${locationId}"
        }

        if(StringUtils.isNotEmpty(keyId)){
            url += "&keyId=${keyId}"
        }

        def response = restClient.get(path: url, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities.size > 0 ? entities.get(0) : null;
    }

    def postSettings(List settings) {
        def settingsMap = new SettingsMap(settings)
        settingsMap.getSettingsToSet().each {
            setSetting(
                    it['settingDefinitionId'],
                    it['currentValue'],
                    it['tenantId'],
                    it['locationId'],
                    it['keyId']
            )
        }

    }

    def createOrUpdateSetting(settingDefinitionId, value, tenantId, locationId=null, keyId=null){
        def setting = getSetting(settingDefinitionId, tenantId, locationId, keyId)
        // Setting doesn't exist, create it
        if(setting == null){
            setSetting(settingDefinitionId, value, tenantId, locationId, keyId)
        } else {
            // Make sure the value is what we want it to be
            if(value.equals(setting.currentValue) == false){
                updateSetting(setting, value)
            }
        }
    }

    def turnCentralDispenseOffAllLocations(tenantId){
        def url = "/masterdata/settings?tenantId=${tenantId}&settingDefinitionId=centraldispense&currentValue=true"

        def response = restClient.get(path: url, sslTrustAllCerts: true)
        def entities = new JsonSlurper().parseText(response.json.toString())

        for(def entity : entities){
            removeSetting(entity)
        }
    }

    def turnSuggestedDispenseOffAllLocations(tenantId){
        def url = "/masterdata/settings?tenantId=${tenantId}&settingDefinitionId=suggesteddispense&currentValue=true"

        def response = restClient.get(path: url, sslTrustAllCerts: true)
        def entities = new JsonSlurper().parseText(response.json.toString())

        for(def entity : entities){
            removeSetting(entity)
        }
    }
}
