package com.cardinalhealth.vitalpath.services

import groovy.json.JsonSlurper

class DrugFamilyGroupService extends BaseService {

    private String SERVICE_END_POINT = '/masterdata/drugfamilygroups'

    DrugFamilyGroupService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def findById(id){

        def restEndPoint = "${SERVICE_END_POINT}?id=${id}"

        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities.get(0)
    }

    def findByName(name){

        def encodedName = URLEncoder.encode(name,'UTF-8')

        def restEndPoint = "${SERVICE_END_POINT}?name=${encodedName}"

        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities.get(0)
    }

}
