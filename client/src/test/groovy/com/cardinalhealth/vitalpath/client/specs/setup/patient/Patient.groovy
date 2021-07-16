package com.cardinalhealth.vitalpath.client.specs.setup.patient

import com.cardinalhealth.vitalpath.client.specs.BaseSpec
import com.cardinalhealth.vitalpath.datasetup.PracticeConstants
import com.cardinalhealth.vitalpath.datasetup.SettingsConstant
import com.cardinalhealth.vitalpath.extensions.feature.Login
import com.cardinalhealth.vitalpath.extensions.feature.Setting
import geb.navigator.Navigator
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement

class Patient extends BaseSpec {

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Duplicate MRNs Can't Be Saved"() {
        given: "I am on the Patient screen"
        goToPatientsPage()

        when: "I click add"
        waitFor(3, {$("tbody > tr").size() > 1})
        def firstMRN = $("tbody > tr > td").eq(3).text().trim()
        addButton.click()

        and: "The patient screen displays"
        waitFor(3, { patientModal.size() > 0 })

        and: "I fill in the required fields"
        firstName << "FirstName"
        lastName << "LastName"
        def mrn = String.valueOf((new Date()).getTime())
        accountNumber << firstMRN
        gender.openSearchAndSelect("Female")
        dob << "01/01/1969"
        actions.sendKeys(Keys.TAB)
       // firstName.click()
        saveButton.click()

        then: "An error is thrown preventing me from creating a duplicate MRN"
        waitFor(3, {$(".error-message").size() == 1})

        and: "I cancel the save"
        cancelButton.click()
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Search for Patient after adding"() {
        given: "I am on the Patient screen"
        goToPatientsPage()

        when: "I click add"
        addButton.click()

        and: "The patient screen displays"
        waitFor(3, { patientModal.size() > 0 })

        and: "I fill in the required fields"
        firstName << "FirstName"
        lastName << "LastName"
        def mrn = String.valueOf((new Date()).getTime())
        accountNumber << mrn
        gender.openSearchAndSelect("Female")
        dob << "01/01/1969"
        //firstName.isEnabled()
        //firstName.click()
        saveButton.click()

        then: "I can search for the patient I just created"
        waitFor(3, { $(".toast-message").text().trim() == "Patient Saved" })
        waitFor(3, {mrnSearch << mrn})
        waitFor(3, {$("tbody > tr > td").eq(3).text().trim() == mrn})
    }

    @Login(site = PracticeConstants.SITE_1, cabinet = PracticeConstants.CABINET_A)
    def "Infinite Scroll loads more data"() {
        given: "I am on the Patient screen"
        goToPatientsPage()

        when: "I scroll to the bottom"
        waitFor(3, {$("tbody > tr").size() > 1})
        def listSize = $("tbody > tr").size()
        $(".infinite-scroll").init()
        //({ scrollTop: $(".infinite-scroll").height() }, 50)
        //print "I am done till here"
        //$("tbody > tr")[listSize - 1].scrollIntoView()
        then: "More results are loaded"
        //waitFor(3, {$("tbody > tr").size() > 50})

    }

}