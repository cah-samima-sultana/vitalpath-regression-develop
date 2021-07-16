package com.cardinalhealth.vitalpath.datasetup

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.services.LocationService
import com.cardinalhealth.vitalpath.services.PhysicianService

class Physicians {

    def physicianService
    def environmentProperties

    def setup() {
        init()

        setUpPhysicians()
    }

    private void init() {
        environmentProperties = EnvironmentProperties.instance
        physicianService = new PhysicianService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())
    }

    def setUpPhysicians(){
        for (i in 1..4){
            def physician = PhysicianConstants.dataSet["physician${i}"]
            def existing = loadPhysician(physician.externalId)
            if(existing == null){
                physicianService.create(physician.externalId, physician.firstName, physician.lastName)
            }
        }
    }

    def loadPhysician(externalId){
        def physicians = physicianService.findByExternalId(externalId)
        physicians.size > 0 ? physicians.get(0) : null
    }

}
