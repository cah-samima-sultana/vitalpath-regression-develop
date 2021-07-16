package com.cardinalhealth.vitalpath.services

import com.cardinalhealth.vitalpath.utils.NameGenerator
import groovy.json.JsonSlurper

class PhysicianService extends BaseService {

    def serviceName = "/masterdata/physicians"

    def PhysicianService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd){
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def makePhysician(
                externalId=System.currentTimeMillis().toString(),
                lastName=NameGenerator.getLastName(),
                firstName=NameGenerator.getFirstName()) {
        def response =  post(serviceName) {
            json externalId:externalId,
                    firstName: firstName,
                    lastName: lastName,
                    created:null,
                    modified:null,
                    version:null
        }

        return response.json
    }

    def create(externalId, firstName, lastName){
        def response =  post(serviceName) {
            json externalId:externalId,
                    firstName: firstName,
                    lastName: lastName,
                    created:null,
                    modified:null,
                    version:null
        }

        return response.json
    }

    def findByExternalId(externalId){
        def response = get("${serviceName}?externalId=${externalId}")
        return new JsonSlurper().parseText(response.json.toString())
    }

    def deletePhysician(entity) {
        if ( ! entity) {
            return null
        }
        return delete(serviceName) {
            entity.id
        }
    }
    
}
