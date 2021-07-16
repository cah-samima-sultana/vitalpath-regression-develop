package com.cardinalhealth.vitalpath.services

import com.cardinalhealth.vitalpath.EnvironmentProperties
import groovy.json.JsonSlurper

class EmployeeService extends BaseService {

    private String SERVICE_END_POINT = '/employees'

    EmployeeService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd) {
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def fetchItems(restEndPoint) {
        def response = super.get( restEndPoint, true)

        return new JsonSlurper().parseText(response.json.toString())
    }

    def findEmployeeById(){
        EnvironmentProperties env = EnvironmentProperties.instance

        def tenantId = env.tenantId()
        def loginId = env.loginId()

        def employees = fetchItems ("${SERVICE_END_POINT}")
        
        return employees.size > 0 ? employees.find {it.logonName == loginId && it.tenantId == tenantId}: null
    }
    def updateOrderPreferences(orderPrefVal){
        def employee = findEmployeeById()
        
        def response = put("${SERVICE_END_POINT}/${employee.id}"){
            json id: employee.id,
                    version: employee.version,
                    created: employee.created,
                    modified: employee.modified,
                    createdBy: employee.createdBy,
                    modifiedBy: employee.modifiedBy,
                    tenantId: employee.tenantId,
                    userId: employee.userId,
                    logonName: employee.logonName,
                    //password: employee.password,
                    roleId: employee.roleId,
                    firstName: employee.firstName,
                    middleName: employee.middleName,
                    lastName: employee.lastName,
                    employeeNumber: employee.employeeNumber,
                    emailAddress: employee.emailAddress,
                    notes: employee.notes,
                    sortOrder: employee.sortOrder,
                    active: employee.active,
                    forcePasswordChange: employee.forcePasswordChange,
                    lastPasswordChange: employee.lastPasswordChange,
                    admin: employee.admin,
                    approveControlledSubstance: employee.approveControlledSubstance,
                    allowOrderPreferences: orderPrefVal,
                    requireLoginReason: employee.requireLoginReason,
                    fullName: employee.fullName,
                    properName: employee.properName,
                    systemUser: employee.systemUser,
                    vpsuser: employee.vpsuser
        }

        return response.json
    }
}
