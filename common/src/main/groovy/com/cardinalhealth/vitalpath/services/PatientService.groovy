package com.cardinalhealth.vitalpath.services

import com.cardinalhealth.vitalpath.utils.DateUtils
import com.cardinalhealth.vitalpath.utils.NameGenerator

class PatientService extends BaseService {
    def serviceName = "/clinical/patients"

    public PatientService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd){
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def makePatient(
                mrn=System.currentTimeMillis().toString(),
                lastName=NameGenerator.getLastName(),
                firstName=NameGenerator.getFirstName(),
                middleName=NameGenerator.getMiddleName()) {
        def response =  post(serviceName) {
            def dob = DateUtils.DateGenerator.getDate()

            json dateOfBirth:dob,
                    gender:"M",
                    accountNumber:mrn,
                    firstName: firstName,
                    middleName:middleName,
                    lastName: lastName,
                    created:null,
                    modified:null,
                    version:null
        }

        return response.json
    }

    def deletePatient(patient) {
        if ( ! patient) {
            return null
        }
        return delete(serviceName) {
            patient.id
        }
    }
    
}
