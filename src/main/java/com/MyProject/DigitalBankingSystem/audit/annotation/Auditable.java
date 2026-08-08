package com.MyProject.DigitalBankingSystem.audit.annotation;

import com.MyProject.DigitalBankingSystem.audit.entity.AuditableAction;
import com.MyProject.DigitalBankingSystem.audit.entity.EntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    AuditableAction action();
    EntityType entityType();
}
