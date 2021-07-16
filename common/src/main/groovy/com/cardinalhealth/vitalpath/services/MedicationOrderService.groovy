package com.cardinalhealth.vitalpath.services

import groovy.json.JsonSlurper


class MedicationOrderService extends BaseService{
    def orderServiceName = "/clinical/clinicalorders"
    def orderLineServiceName = "/clinical/clinicalorderlines"
    def orderLineSourceServiceName = "/clinical/clinicalorderlinesources";
    def orderPlanLineServiceName = "/clinical/clinicalorderlineplans"

    public MedicationOrderService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd){
        super(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    def makeMedicationOrder(patient, physician, dateTimeInMilli, siteId){
        def response = post(orderServiceName){
            json clinicalOrderDate: dateTimeInMilli.toString(),
                    created:null,
                    deleted:false,
                    locationId: siteId,
                    modified:null,
                    patientId:patient.id,
                    physicianId:physician.id,
                    version:null
        }

        return response.json
    }

    def makeExternalMedicationOrder(patient, physician, externalId, dateTimeInMilli, siteId){
        def response = post(orderServiceName){
            json clinicalOrderDate: dateTimeInMilli.toString(),
                    created:null,
                    deleted:false,
                    locationId: siteId,
                    modified:null,
                    patientId:patient.id,
                    physicianId:physician.id,
                    version:null,
                    externalId: externalId
        }

        return response.json
    }

    def addMedicationLine(drugFamilyId, lineNumber, quantity, route = 'IV', orderId, oculusId = null){

        def response = post(orderLineServiceName){
            json clinicalOrderId: orderId,
                    created:null,
                    deleted:false,
                    drugFamilyGroupId:null,
                    drugFamilyId:drugFamilyId,
                    firstBillingDate:null,
                    itemId:null,
                    lineNumber:lineNumber,
                    modified:null,
                    quantity:quantity,
                    reasonCodeId:null,
                    routeId:route,
                    version:null,
                    oculusId:oculusId
        }

        return response.json
    }

    def addMedicationLineNonDrug(itemId, lineNumber, quantity, orderId, oculusId = null){

        def response = post(orderLineServiceName){
            json clinicalOrderId: orderId,
                    created:null,
                    deleted:false,
                    drugFamilyGroupId:null,
                    drugFamilyId:null,
                    firstBillingDate:null,
                    itemId:itemId,
                    lineNumber:lineNumber,
                    modified:null,
                    quantity:quantity,
                    reasonCodeId:null,
                    routeId: null,
                    version:null,
                    oculusId:oculusId
        }

        return response.json
    }

    def addMedicationOrderLineSource(shouldCreateInError, clinicalOrderId, clinicalOrderExternalId, locationId, quantity){
        def uomSourceCode = shouldCreateInError ? "mg" : "mcg";

        def response = post(orderLineSourceServiceName){
            json sequence: "1",
                locationId: locationId,
                drugMappingCategoryId: "varian",
                orderExternalId: clinicalOrderExternalId,
                drugSourceCode: "Aloxi",
                drugSourceDescription: "Aloxi Desc",
                routeMappingCategoryId: "varian",
                routeSourceCode: "IVP",
                routeSourceDescription: "IV Push",
                quantitySource: quantity,
                uomMappingCategoryId: "varian",
                uomSourceCode: uomSourceCode,
                uomSourceDescription: "milligrams",
                source: "restclient",
                notes: "Give IVP prior to Infed",
                additionalInstructions: "test addl instruct",
                orderLineExternalId: clinicalOrderId
        }

        return response.json
    }

    def addMedicationOrderPlanLineFromOrderLine(itemId,inventoryLocationId,inventorySegmentId,patientId,quantity,locationId,orderLine){

        def response = post(orderPlanLineServiceName){
            json clinicalOrderId: orderLine.clinicalOrderId,
                    clinicalOrderLineId: orderLine.id,
                    locationId:locationId,
                    inventorySegmentId:inventorySegmentId,
                    itemId:itemId,
                    inventoryLocationId:inventoryLocationId,
                    patientId : patientId,
                    plannedQuantity:quantity
        }

        return response.json
    }

    def getMedicationOrderLine(orderId, drugFamilyId, inventorySegmentId){
        def restEndPoint = orderLineServiceName + "?clinicalOrderId=${orderId}"

        if(drugFamilyId != null){
            restEndPoint+="&drugFamilyId=${drugFamilyId}"
        }
        if(inventorySegmentId != null){
            restEndPoint+="&drugFamilyGroupId=${inventorySegmentId}"
        }

        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities;
    }

    def getClinicalOrdersByDate(locationId, startDate, endDate){
        def restEndPoint = orderServiceName + "?locationId=${locationId}&clinicalOrderDate=@between(${startDate},${endDate})"

        def response = restClient.get(path: restEndPoint, sslTrustAllCerts: true)

        def entities = new JsonSlurper().parseText(response.json.toString())

        return entities;
    }

    def putClinicalOrder(clinicalOrder){
        def putURL = orderServiceName + "/" + clinicalOrder.id
        def response = put(putURL){
            json id: clinicalOrder.id,
                    version: clinicalOrder.version,
                    tenantId: clinicalOrder.tenantId,
                    locationId: clinicalOrder.locationId,
                    clinicalOrderDate: clinicalOrder.clinicalOrderDate,
                    physicianId: clinicalOrder.physicianId,
                    externalId: clinicalOrder.externalId,
                    deleted: clinicalOrder.deleted,
                    patientId: clinicalOrder.patientId
        }

        return response.json
    }

    def deletePlannedLineByOrder(orderId){
        return delete("/clinical/clinicalorders/" + orderId + "/plans");
    }
    def deleteMedicationOrderLineForOrder(orderId){
        def lines = getMedicationOrderLine(orderId)
        for(line in lines){
            delete(orderLineServiceName + "/" + line.id)
        }
    }

    def deleteAllPlansForSite(siteId, cabinetId){

        def startDate = new Date()
        startDate.clearTime()
        def endDate = (startDate + 1);

        def response = get("${orderServiceName}/${siteId}/plannedorders?startDate=${startDate.time}&endDate=${endDate.time}&cabinetId=${cabinetId}")
        def plannedOrders = new JsonSlurper().parseText(response.json.toString())
        plannedOrders.each {
            deletePlannedLineByOrder(it)
        }
    }

    def deleteMedicationOrder(orderId){
        deletePlannedLineByOrder(orderId)
        deleteMedicationOrderLineForOrder(orderId)
        delete(orderServiceName + "/" + orderId)
    }
}
