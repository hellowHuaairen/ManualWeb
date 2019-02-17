package annotation;

import java.lang.annotation.*;

/**
 * Created by wangzhiguo on 2019/2/13 0013.
 * controller方法的注解
 */
@Documented
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    public String value() default "";
}
