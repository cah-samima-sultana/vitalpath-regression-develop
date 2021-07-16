package com.cardinalhealth.vitalpath.cabinet.modules.components

import com.cardinalhealth.vitalpath.cabinet.modules.BaseModule


class TreatmentDaysModule extends BaseModule {

    static content = {
        wrapper(wait: true) { $('data-id': 'treatment-days-notification') }

        notificationText { wrapper.find('[data-id="treatment-days-text"]').text() }

        continueButton { wrapper.find('[data-id="treatment-days-continue"]') }

        cancelButton { wrapper.find('[data-id="treatment-days-cancel"]') }
    }

}

