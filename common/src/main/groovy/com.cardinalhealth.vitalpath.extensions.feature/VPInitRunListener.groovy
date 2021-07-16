package com.cardinalhealth.vitalpath.extensions.feature

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.datasetup.ItemConstants
import com.cardinalhealth.vitalpath.datasetup.PhysicianConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.services.InventoryLocationService
import com.cardinalhealth.vitalpath.services.ItemService
import com.cardinalhealth.vitalpath.services.LocationService
import com.cardinalhealth.vitalpath.services.MedicationOrderService
import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.model.SpecInfo

class VPInitRunListener extends AbstractRunListener {

    static cached = false
    @Override
    void beforeSpec(SpecInfo spec) {

        if(!cached) {
            cacheSiteDetails()
            PhysicianConstants.cache()
            cacheItemIds()
            cached = true
        }

        moveClinicalOrders()
    }

    def cacheItemIds() {
        def env = EnvironmentProperties.instance
        def itemService = new ItemService(env.servicesUrl(), env.restClientAuthId(), env.restClientAuthPwd())

        ItemConstants.values().each { itemConstant ->
            def inventoryItemId
            if (itemConstant.getIsDrug()) {
                inventoryItemId = itemService.findItemIdByNDC(itemConstant.ndc)
            } else {
                inventoryItemId = itemService.findItemIdByName(itemConstant.name)
            }
            itemConstant.setItemId(inventoryItemId)
        }
    }

    def cacheSiteDetails(){
        def env = EnvironmentProperties.instance
        def locationService = new LocationService(env.servicesUrl(), env.restClientAuthId(), env.restClientAuthPwd())
        def inventoryLocationService = new InventoryLocationService(env.servicesUrl(), env.restClientAuthId(), env.restClientAuthPwd())

        Session.site1 = "${env.browserVersion()} ${PracticeConstants.SITE_1}"
        Session.site1Id = locationService.getLocationId("${env.browserVersion()} ${PracticeConstants.SITE_1}")
        Session.site1CabinetA = PracticeConstants.CABINET_A
        Session.site1CabinetAId = inventoryLocationService.findInventoryLocationId(Session.site1Id, null, 'cabinet', PracticeConstants.CABINET_A)
        Session.site1CabinetB = PracticeConstants.CABINET_B
        Session.site1CabinetBId = inventoryLocationService.findInventoryLocationId(Session.site1Id, null, 'cabinet', PracticeConstants.CABINET_B)

        Session.site2 = "${env.browserVersion()} ${PracticeConstants.SITE_2}"
        Session.site2Id = locationService.getLocationId("${env.browserVersion()} ${PracticeConstants.SITE_2}")
        Session.site2CabinetA = PracticeConstants.CABINET_A
        Session.site2CabinetAId = inventoryLocationService.findInventoryLocationId(Session.site2Id, null, 'cabinet', PracticeConstants.CABINET_A)
        Session.site2CabinetB = PracticeConstants.CABINET_B
        Session.site2CabinetBId = inventoryLocationService.findInventoryLocationId(Session.site2Id, null, 'cabinet', PracticeConstants.CABINET_B)
    }

    def moveClinicalOrders(){
        def environmentProperties = EnvironmentProperties.instance
        def servicesUrl = environmentProperties.servicesUrl()
        def restClientAuthBaseAuthId = environmentProperties.restClientAuthId()
        def restClientAuthBaseAuthPwd = environmentProperties.restClientAuthPwd()
        def medicationOrderService = new MedicationOrderService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)

        Calendar cal = Calendar.getInstance()
        cal.set(Calendar.HOUR, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        long startDate = cal.getTimeInMillis();

        cal.set(Calendar.HOUR, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 59)

        long endDate = cal.getTimeInMillis()

        def clinicalOrders = medicationOrderService.getClinicalOrdersByDate(Session.site1Id, startDate, endDate)

        cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        def previousDay = cal.getTimeInMillis()

        for(clinicalOrder in clinicalOrders){
            clinicalOrder.put("clinicalOrderDate", previousDay)
            medicationOrderService.putClinicalOrder(clinicalOrder)
        }

        clinicalOrders = medicationOrderService.getClinicalOrdersByDate(Session.site2Id, startDate, endDate)
        for(clinicalOrder in clinicalOrders){
            clinicalOrder.put("clinicalOrderDate", previousDay)

            medicationOrderService.putClinicalOrder(clinicalOrder)
        }
    }
}
