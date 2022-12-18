package me.ilnicki.container;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({FIELD, CONSTRUCTOR, METHOD, PARAMETER})
@Inherited
public @interface Inject {
  String[] value() default {};
}
