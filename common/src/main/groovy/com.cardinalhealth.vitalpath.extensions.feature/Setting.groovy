package com.cardinalhealth.vitalpath.extensions.feature

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.METHOD, ElementType.TYPE])

@interface Setting {
    String name()
    String value()
    String location() default ''
    String level() default 'location'
}
