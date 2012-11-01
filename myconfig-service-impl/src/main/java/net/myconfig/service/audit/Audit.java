package net.myconfig.service.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Audit {
	
	EventCategory category();
	
	EventAction action();
	
	String collection() default "";
	
	String identifier() default "";
	
	String application() default "";
	
	String environment() default "";
	
	String version() default "";
	
	String key() default "";
	
	String message() default "";
	
	String result() default "";

}
