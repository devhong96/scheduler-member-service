package com.scheduler.memberservice.testSet.student;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithStudentSecurityContextFactory.class)
public @interface WithStudent {
    String username() default "";
    String studentId() default "";
}
