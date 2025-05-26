package com.dio.urlshortener;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Test;

 class ArchitectureTest {
    @Test
    void noCyclicDependenciesBetweenPackages() {
        //TODO: Corregir dependencias cíclicas, este test se caerá hasta que no se corrijan.
        JavaClasses classes = new ClassFileImporter()
                .importPackages("com.dio.urlshortener");

        SlicesRuleDefinition.slices()
                .matching("com.dio.urlshortener.(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }
}
