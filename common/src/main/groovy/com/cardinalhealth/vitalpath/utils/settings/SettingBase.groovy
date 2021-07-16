package com.cardinalhealth.vitalpath.utils.settings


abstract class SettingBase {
    abstract String settingDefinitionId
    public String tenantId
    public String keyId
    public String locationId

    public String getSettingDefinitionId() {
        return settingDefinitionId;
    }

    abstract public String getValue()

    String getTenantId() {
        return this.tenantId ? this.tenantId : 1
    }

    String getKeyId() {
        return this.keyId ? this.keyId : null
    }

    String getLocationId() {
        return this.locationId ? this.locationId : null
    }

}
