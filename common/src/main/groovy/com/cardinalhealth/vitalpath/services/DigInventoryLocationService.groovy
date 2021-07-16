package com.cardinalhealth.vitalpath.services

import groovy.json.JsonSlurper

class DigInventoryLocationService extends BaseService {

    private String REST_END_POINT = '/masterdata/diginventorylocations'

    DigInventoryLocationService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def setUseSdvWaste(tenantId, siteId, cabinetId, digId, useSdvWaste = true){
        def existingEntry = getDigInventoryLocation(cabinetId, digId)

        println "------> ${digId}"

        def response
        if(existingEntry){
            response = put("${REST_END_POINT}/${existingEntry.id}"){
                json digId: digId,
                        inventoryLocationId: existingEntry.inventoryLocationId,
                        locationId: existingEntry.locationId,
                        tenantId: existingEntry.tenantId,
                        useSDVWaste: useSdvWaste,
                        id: existingEntry.id,
                        version: existingEntry.version
            }
        }else{
            response = post(REST_END_POINT) {
               json digId: digId,
                    inventoryLocationId: cabinetId,
                    locationId: siteId,
                    tenantId: tenantId,
                    useSDVWaste: useSdvWaste
            }
        }

        return response.json
    }

    def getDigInventoryLocation(cabinetId, digId){
        def encodedDigId = URLEncoder.encode(digId,'UTF-8')
        def restEndPoint = "${REST_END_POINT}?inventoryLocationId=${cabinetId}&digId=${encodedDigId}"


        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities.size > 0 ? entities.get(0) : null;
    }
}
