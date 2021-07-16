package com.cardinalhealth.vitalpath.extensions.feature

import com.cardinalhealth.vitalpath.EnvironmentProperties
import com.cardinalhealth.vitalpath.Session
import com.cardinalhealth.vitalpath.common.pages.LoginPage
import com.cardinalhealth.vitalpath.common.pages.LogoutPage
import com.cardinalhealth.vitalpath.datasetup.PhysicianConstants
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.services.InventoryLocationService
import com.cardinalhealth.vitalpath.services.LocationService
import com.cardinalhealth.vitalpath.services.MedicationOrderService
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation

class LoginInterceptor extends AbstractMethodInterceptor {
    String site
    String cabinet
    Setting[] siteSettings

    def currentlyRunningSpec

    def browserSite
    def env = EnvironmentProperties.instance

    def medicationOrderService

    @Override
    void interceptFeatureExecution(IMethodInvocation invocation) throws Throwable {

        browserSite = "${env.browserVersion()} ${site}"
        currentlyRunningSpec = invocation.sharedInstance


        def settings = new Settings(defaultSite: browserSite,
                defaultCabinet: cabinet,
                siteSettings: siteSettings)

        settings.apply()

        invocation.feature.addIterationInterceptor({
            login()
            it.proceed()
        })

        invocation.proceed()
    }


    def login() {
        if(needToLogout()) {
            logoutUser();
        }

        if (needToLogin()) {
            loginUser()
            selectLocation()
            cacheSession()
        } else if (siteSettings.length > 0) {
            currentlyRunningSpec.browser.driver.navigate().refresh();
            currentlyRunningSpec.at currentlyRunningSpec.homePage
        }
    }

    def needToLogin(){
        def browser = currentlyRunningSpec.browser
        atLoginPage(browser) ||
        !Session.loggedInSite.equalsIgnoreCase(browserSite) ||
        !Session.loggedInCabinet.equalsIgnoreCase(cabinet)
    }

    def needToLogout() {
        def browser = currentlyRunningSpec.browser
        !atLoginPage(browser) &&
        (!Session.loggedInSite.equalsIgnoreCase(browserSite) || !Session.loggedInCabinet.equalsIgnoreCase(cabinet))
    }

    private boolean atLoginPage(browser) {
        browser.getCurrentUrl().equals("data:,") || browser.getCurrentUrl().endsWith("#/login")
    }


    def loginUser(){
        currentlyRunningSpec.to LoginPage
        currentlyRunningSpec.login.loginWith env.loginId(), env.loginPassword()
        currentlyRunningSpec.at currentlyRunningSpec.homePage
    }

    def logoutUser() {
        currentlyRunningSpec.to LogoutPage
    }

    def selectLocation(){
        currentlyRunningSpec.to currentlyRunningSpec.siteSelectionPage
        currentlyRunningSpec.selectCabinet(browserSite, cabinet)
    }

    def cacheSession(){
        if(Session.site1.equalsIgnoreCase(browserSite)){
            Session.loggedInSite = Session.site1
            Session.loggedInSiteId = Session.site1Id
            Session.loggedInCabinet = cabinet.equalsIgnoreCase(Session.site1CabinetA) ? Session.site1CabinetA :  Session.site1CabinetB
            Session.loggedInCabinetId = cabinet.equalsIgnoreCase(Session.site1CabinetA) ? Session.site1CabinetAId :  Session.site1CabinetBId
        }else{
            Session.loggedInSite = Session.site2
            Session.loggedInSiteId = Session.site2Id
            Session.loggedInCabinet = cabinet.equalsIgnoreCase(Session.site2CabinetA) ? Session.site2CabinetA :  Session.site2CabinetB
            Session.loggedInCabinetId = cabinet.equalsIgnoreCase(Session.site2CabinetA) ? Session.site2CabinetAId :  Session.site2CabinetBId
        }
    }
}
