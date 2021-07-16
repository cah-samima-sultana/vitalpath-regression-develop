package com.cardinalhealth.vitalpath.datasetup

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.services.PhysicianService

class PhysicianConstants {
    static dataSet = ['physician1' : ['externalId' : 'test111111', 'firstName' : 'Gregorio', 'lastName' : 'Merriman'],
                      'physician2' : ['externalId' : 'test222222', 'firstName' : 'Louis', 'lastName' : 'Doyle'],
                      'physician3' : ['externalId' : 'test333333', 'firstName' : 'Daniel', 'lastName' : 'Bingham'],
                      'physician4' : ['externalId' : 'test444444', 'firstName' : 'Crystal', 'lastName' : 'Hassel']]

    static physician1
    static physician2
    static physician3
    static physician4


    static cache(){
        def environmentProperties = EnvironmentProperties.instance
        def physicianService = new PhysicianService(environmentProperties.servicesUrl(), environmentProperties.restClientAuthId(), environmentProperties.restClientAuthPwd())

        for (i in 1..4){
            def physicians = physicianService.findByExternalId(dataSet["physician${i}"].externalId)
            PhysicianConstants."physician${i}" = physicians.size > 0 ? physicians.get(0) : null
        }
    }

}
