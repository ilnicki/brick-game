package me.ilnicki.bg.engine.system.container;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD, CONSTRUCTOR, METHOD})
public @interface Inject {
    boolean optional() default false;
}
