package io.github.imecuadorian.vitalmed.util;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemForm {

    String name() default "";

    String description() default "";

    String[] tags() default {};
}
