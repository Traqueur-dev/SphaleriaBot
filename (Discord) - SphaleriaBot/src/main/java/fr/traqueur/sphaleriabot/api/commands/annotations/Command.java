package fr.traqueur.sphaleriabot.api.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String[] name() default {};
    String[] permittedRoles() default {};
    String[] permittedUsers() default {};
    String description() default "";
}
