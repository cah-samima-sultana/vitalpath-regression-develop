package com.cardinalhealth.vitalpath

import com.esotericsoftware.yamlbeans.YamlReader


@Singleton(strict=false)
class EnvironmentProperties {

    private String ENVIRONMENT_RESOURCE = '/defaultenvironment.yml'
    private Map<String, String> defaultEnvProperties

    EnvironmentProperties() {
        YamlReader reader = new YamlReader(this.getClass().getResource(ENVIRONMENT_RESOURCE).text)
        defaultEnvProperties = reader.read()
        reader.close()
    }

    def servicesUrl(){
        readProperty('servicesUrl')
    }

    def restClientAuthId(){
        readProperty('restClientAuthId')
    }

    def restClientAuthPwd(){
        readProperty('restClientAuthPwd')
    }

    def loginId(){
        readProperty('loginId')
    }

    def loginPassword(){
        readProperty('loginPassword')
    }

    def approverId(){
        readProperty('approverId')
    }

    def approverPassword(){
        readProperty('approverPassword')
    }

    def readProperty(name){
        System.getProperty(name) != null ? System.getProperty(name) : defaultEnvProperties.get(name)
    }

    def browserVersion(){
        readProperty('browserVersion')
    }

    def tenantId(){
        readProperty('tenantId')
    }
}
