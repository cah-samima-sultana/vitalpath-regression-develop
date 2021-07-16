package com.cardinalhealth.vitalpath.extensions.feature

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.MethodInfo

class LoginFeatureSetupExtension  extends AbstractAnnotationDrivenExtension<Login> {
    @Override
    void visitFeatureAnnotation(Login annotation, FeatureInfo feature) {
        def interceptor = new LoginInterceptor(site: annotation.site(),
                cabinet: annotation.cabinet(),
                siteSettings: annotation.siteSettings())

        feature.addInterceptor(interceptor)
    }

}
