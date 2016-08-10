package com.kbmsfx.annotations;

/**
 * Created by Alex on 08.08.2016.
 */

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public  @interface QAEvent {
    QAТуре value();
    enum QAТуре { CATEGORY, NOTICE }
}
