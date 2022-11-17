package ru.aleksseii.dao;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aleksseii.database.ConnectionManager;
import ru.aleksseii.database.FlywayInitializer;
import ru.aleksseii.model.Organization;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class OrganizationDAOTest {

    private static final int EXISTING_ID1 = 1;
    private static final int EXISTING_ID2 = 2;

    private static final long EXISTING_INN1 = 111L;
    private static final long EXISTING_INN2 = 222L;

    private static final @NotNull String EXISTING_NAME1 = "org_1";

    private static final @NotNull String EXISTING_BANK_ACCOUNT1 = "acc_1";
    private static final @NotNull String EXISTING_BANK_ACCOUNT2 = "acc_2";

    private static final @NotNull Organization EXISTING_ORG1 =
            new Organization(EXISTING_ID1, EXISTING_INN1, EXISTING_NAME1, EXISTING_BANK_ACCOUNT1);
    private static final @NotNull Organization EXISTING_ORG2 =
            new Organization(EXISTING_ID2, EXISTING_INN2, EXISTING_NAME1, EXISTING_BANK_ACCOUNT2);

    private static final @NotNull List<@NotNull Organization> ALL_ORGANIZATIONS = List.of(
            EXISTING_ORG1,
            EXISTING_ORG2,
            new Organization(3, 333L, "org_3", "acc_3")
    );


    private static final @NotNull Connection CONNECTION = ConnectionManager.getConnectionOrThrow();

    private final @NotNull OrganizationDAO organizationDAO = new OrganizationDAO(CONNECTION);


    @AfterAll
    static void closeConnection() {

        FlywayInitializer.initDB();
        try {
            CONNECTION.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void initDB() {
        FlywayInitializer.initDB();
    }

    @Test
    @DisplayName("Should get single Organization instance by provided id")
    void shouldGetById() {

        Organization organization = organizationDAO.get(EXISTING_ID1);
        assertEquals(EXISTING_ORG1, organization);
    }

    @Test
    @DisplayName("Should get list of Organization instances by provided name")
    void shouldGetByName() {

        List<@NotNull Organization> expected = List.of(EXISTING_ORG1, EXISTING_ORG2);
        List<@NotNull Organization> organizations = organizationDAO.get(EXISTING_NAME1);

        MatcherAssert.assertThat(expected, Matchers.containsInAnyOrder(organizations.toArray()));
    }

    @Test
    @DisplayName("Should get single Organization instance by provided inn")
    void shouldGetByInn() {

        Organization organization = organizationDAO.getByInn(EXISTING_INN2);
        assertEquals(EXISTING_ORG2, organization);
    }

    @Test
    @DisplayName("Should get list of all Organization instances")
    void shouldGetAllOrganizations() {

        List<@NotNull Organization> organizations = organizationDAO.all();

        MatcherAssert.assertThat(ALL_ORGANIZATIONS, Matchers.containsInAnyOrder(organizations.toArray()));
    }

    @Test
    @DisplayName("Should update Organization instance")
    void shouldUpdateOrganization() {

        int id = EXISTING_ID2;
        long inn = 999;
        Organization expectedOrg = new Organization(id, inn, "new org_2", "new acc_2");

        int sizeBefore = organizationDAO.all().size();
        organizationDAO.update(expectedOrg);
        int sizeAfter = organizationDAO.all().size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(expectedOrg, organizationDAO.getByInn(inn));
        assertEquals(expectedOrg, organizationDAO.get(id));
    }

    @Test
    @DisplayName("Should insert new Organization instance")
    void shouldSaveNewOrganization() {

        int sizeBefore = organizationDAO.all().size();
        int nextId = sizeBefore + 1;
        Organization newOrganization =
                new Organization(nextId, 444, "new org_4", "new acc_4");

        organizationDAO.save(newOrganization);
        int sizeAfter = organizationDAO.all().size();

        assertEquals(sizeBefore + 1, sizeAfter);
        assertEquals(newOrganization, organizationDAO.get(nextId));
    }

    @Test
    @DisplayName("Should delete Organization instance by provided id")
    void shouldDeleteOrganization() {

        int id = EXISTING_ID1;
        int sizeBefore = organizationDAO.all().size();
        organizationDAO.delete(id);
        int sizeAfter = organizationDAO.all().size();

        assertEquals(sizeBefore - 1, sizeAfter);
        assertTrue(organizationDAO.get(id).isEmpty());
    }

    @Test
    @DisplayName("Should delete all instances from Organization table")
    void shouldDeleteAllOrganizations() {

        assertFalse(organizationDAO.all().isEmpty());
        organizationDAO.deleteAll();
        assertTrue(organizationDAO.all().isEmpty());
    }
}
