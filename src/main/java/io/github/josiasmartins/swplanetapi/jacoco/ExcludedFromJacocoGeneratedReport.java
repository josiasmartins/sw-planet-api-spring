package io.github.josiasmartins.swplanetapi.jacoco;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // vai ser aplicado em tempo de execução
@Target(ElementType.METHOD) // utilizada em metodos
public @interface ExcludedFromJacocoGeneratedReport {

}