package se.patrikbergman.service.test.fixture.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectTestResource {
	Class getFactory();
	String getMethod();
}

--------------------

package com.wildex999.schematicbuilder.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigEntry {
	String name();
	String category() default ConfigCategory.GENERAL;
	String comment() default "";
	boolean canReload() default true;
	boolean sendToClient() default true;
	ConfigEntryType type() default ConfigEntryType.UNKNOWN;
}

--------------------

package com.marklogic.junit.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Convenience annotation for specifying multiple paths from which to load MarkLogic modules into a modules database.
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModulesPaths {

    ModulesPath[] paths();
}

--------------------

