package com.cardinalhealth.vitalpath.services

import groovy.json.JsonSlurper

class DrugService extends BaseService {

    private String SERVICE_END_POINT = '/masterdata/drugs'

    DrugService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    private fetchDrugs(restEndPoint) {
        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        return new JsonSlurper().parseText(response.json.toString())
    }

    def findById(id){

        def restEndPoint = "${SERVICE_END_POINT}?id=${id}"

        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities.get(0)
    }

    def findNonItemDrug(){

        def entities = fetchDrugs ("${SERVICE_END_POINT}/nonitemdrugs")

        return entities.size > 0 ? entities.get(0) : null
    }

    def updateDrug(ndc,samples){
        def drug = findById(ndc);
        if(drug == null){
            assert false
        }
        def putURL = SERVICE_END_POINT + "/" + ndc
        drug.sample = samples
        def response = put(putURL){
            json drug
        }
        return response.json

    }

}
