package com.stitch.gateway.security.model;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that will switch off security check for a given method.
 * Works only for methods defined in GraphQL controllers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Unsecured {
}
