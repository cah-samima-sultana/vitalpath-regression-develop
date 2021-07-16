package com.cardinalhealth.vitalpath.extensions.feature

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.FieldInfo
import org.spockframework.runtime.model.MethodInfo
import org.spockframework.runtime.model.SpecInfo

class VPInitExtension extends AbstractAnnotationDrivenExtension<VPInit> {

    @Override
    void visitSpecAnnotation(VPInit annotation, SpecInfo spec) {
    }

    @Override
    void visitFeatureAnnotation(VPInit annotation, FeatureInfo feature) {
    }

    @Override
    void visitFixtureAnnotation(VPInit annotation, MethodInfo fixtureMethod) {
    }

    @Override
    void visitFieldAnnotation(VPInit annotation, FieldInfo field) {
    }

    @Override
    void visitSpec(SpecInfo spec) {
        spec.addListener(new VPInitRunListener())
    }
}
