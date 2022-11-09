package ru.aleksseii.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;



public final class OrganizationTest {

    @Test
    void shouldReturnIfInstanceIsEmpty() {

        Organization emptyOrganization = new Organization();
        assertTrue(emptyOrganization.isEmpty());

        Organization organization = new Organization(1, 111, "name", "acc");
        assertFalse(organization.isEmpty());
    }
}