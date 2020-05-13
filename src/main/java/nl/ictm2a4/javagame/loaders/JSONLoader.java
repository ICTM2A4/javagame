package nl.ictm2a4.javagame.loaders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface JSONLoader {

    String JSONString();
    boolean withExtra() default false;
    boolean inArray() default true;
}
