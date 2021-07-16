package com.cardinalhealth.vitalpath.services

import com.cardinalhealth.vitalpath.EnvironmentProperties
import groovy.json.JsonSlurper

class CabinetPartParService extends BaseService {

    private String SERVICE_END_POINT = '/inventory/cabinetpartpars'

    CabinetPartParService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def fetchItems(restEndPoint) {
        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)
        return new JsonSlurper().parseText(response.json.toString())
    }
    
    def findParsByLocation(locationId){
        return fetchItems ("${SERVICE_END_POINT}?locationId=${locationId}")
    }

    def findParsByDistributorPartNumberId(distributorPartNumberId){
        return fetchItems ("${SERVICE_END_POINT}?distributorPartNumberId=${distributorPartNumberId}")
    }

    def createCabinetPartPar(locationId, inventoryLocationId, digId, min, max, distributorPartNumberId){
        EnvironmentProperties env = EnvironmentProperties.instance
        def tenantId = env.tenantId()

        def response = post(SERVICE_END_POINT){
            json tenantId: tenantId,
                    min: min,
                    max: max,
                    locationId: locationId,
                    inventoryLocationId: inventoryLocationId,
                    digId: digId,
                    distributorPartNumberId: distributorPartNumberId
        }

        return response.json
    }

    def deletePar(id){
        return delete("${SERVICE_END_POINT}/${id}")
    }

    def deletePars(pars){
        for(def par : pars){
            deletePar(par.id)
        }
    }

}
