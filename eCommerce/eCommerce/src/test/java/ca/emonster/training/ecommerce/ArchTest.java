package ca.emonster.training.ecommerce;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ca.emonster.training.ecommerce");

        noClasses()
            .that()
            .resideInAnyPackage("ca.emonster.training.ecommerce.service..")
            .or()
            .resideInAnyPackage("ca.emonster.training.ecommerce.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..ca.emonster.training.ecommerce.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
