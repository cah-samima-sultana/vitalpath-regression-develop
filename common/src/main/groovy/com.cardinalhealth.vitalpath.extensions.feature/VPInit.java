package com.cardinalhealth.vitalpath.extensions.feature;

import org.spockframework.runtime.extension.ExtensionAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

@ExtensionAnnotation(VPInitExtension.class)
public @interface VPInit {
}
