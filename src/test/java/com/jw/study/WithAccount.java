package com.jw.study;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = withAccountSecurityContextFactory.class)
public @interface WithAccount {

    String value();
}
