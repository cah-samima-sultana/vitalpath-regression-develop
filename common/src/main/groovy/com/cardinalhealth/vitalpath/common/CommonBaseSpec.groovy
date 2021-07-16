package com.cardinalhealth.vitalpath.common

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.extensions.feature.VPInit
import com.cardinalhealth.vitalpath.services.*
import geb.Page
import geb.spock.GebReportingSpec
import org.openqa.selenium.interactions.Actions
import spock.lang.Ignore
import spock.lang.Shared

@Ignore
@VPInit
abstract class CommonBaseSpec extends GebReportingSpec {

    @Shared def homePage
    @Shared def siteSelectionPage

    @Shared SettingsService settingsService
    @Shared PatientService patientService
    @Shared PhysicianService physicianService

    @Shared MedicationOrderService medicationOrderService
    @Shared ItemService itemService
    @Shared DrugService drugService
    @Shared InventoryLocationService inventoryLocationService
    @Shared LocationService locationService
    @Shared InventoryAdjustmentService inventoryAdjustmentService
    @Shared InventoryService inventoryService
    @Shared DigInventoryLocationService digInventoryLocationService
    @Shared DispenseService dispenseService

    @Shared EmployeeService employeeService
    @Shared DistributorPartNumberService distributorPartNumberService
    @Shared CabinetPartParService cabinetPartParService

    @Shared def preventDefaultRefresh = true

    @Shared def actions = new Actions(driver)

    def setupSpec() {
        initConfig()
        def enviornmentProperties = EnvironmentProperties.instance
        def servicesUrl = enviornmentProperties.servicesUrl()
        def userManagementServiceUrl = "http://localhost:8080/usermanagement/"
        def restClientAuthBaseAuthId = enviornmentProperties.restClientAuthId()
        def restClientAuthBaseAuthPwd = enviornmentProperties.restClientAuthPwd()

        patientService = new PatientService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        physicianService = new PhysicianService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        settingsService = new SettingsService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        medicationOrderService = new MedicationOrderService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        itemService = new ItemService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        drugService = new DrugService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        inventoryLocationService = new InventoryLocationService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        locationService = new LocationService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        inventoryAdjustmentService = new InventoryAdjustmentService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        inventoryService = new InventoryService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        digInventoryLocationService = new DigInventoryLocationService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        dispenseService = new DispenseService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        employeeService = new EmployeeService(userManagementServiceUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        distributorPartNumberService = new DistributorPartNumberService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
        cabinetPartParService = new CabinetPartParService(servicesUrl,restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }

    abstract initConfig()

    def setup() {
    }

    def cleanup() {
        return true
    }

    def postSettings(List settings) {
        settingsService.postSettings(settings)
    }

   public <T extends Page> T to(Map params = [:], Class<T> pageType, Object[] args) {
        if(preventDefaultRefresh && browser.currentUrl.endsWith(pageType.url)){
            at pageType
        } else {
            browser.to(params, pageType, args)
        }
    }

    def clickRefresh(){
        browser.driver.navigate().refresh()
    }
}
