package com.microsoft.xuetang.aspect;

import java.lang.annotation.*;

/**
 * Created by shijianguang on 3/19/16.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiRequest {
}
