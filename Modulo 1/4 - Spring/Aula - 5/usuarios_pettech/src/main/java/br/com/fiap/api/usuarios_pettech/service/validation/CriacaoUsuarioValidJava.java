package br.com.fiap.api.usuarios_pettech.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})          // Informa que posso modificar essa classe
@Retention(RetentionPolicy.RUNTIME)  // Essa validação estaráa diponível em tempo de execução
@Constraint(validatedBy = CriacaoUsuarioValidator.class) // Vai referenciar a classe que vai implementar essa validação
public @interface CriacaoUsuarioValidJava {
    String message() default "Validation error";

    Class<?>[] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
