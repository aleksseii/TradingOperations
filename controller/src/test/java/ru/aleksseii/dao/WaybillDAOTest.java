package ru.aleksseii.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aleksseii.database.DataSourceManager;
import ru.aleksseii.database.FlywayInitializer;
import ru.aleksseii.model.Waybill;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class WaybillDAOTest {

    private static final int EXISTING_ID1 = 1;
    private static final int EXISTING_ID2 = 2;

    private static final int EXISTING_ORG_ID1 = 1;
    private static final int EXISTING_ORG_ID2 = 2;
    private static final int EXISTING_ORG_ID3 = 3;

    private static final @NotNull Waybill EXISTING_WAYBILL1 =
            new Waybill(EXISTING_ID1, Date.valueOf("2022-11-04"), EXISTING_ORG_ID1);
    private static final @NotNull Waybill EXISTING_WAYBILL2 =
            new Waybill(EXISTING_ID2, Date.valueOf("2022-11-05"), EXISTING_ORG_ID2);

    private static final @NotNull List<@NotNull Waybill> ALL_WAYBILLS = List.of(
            EXISTING_WAYBILL1,
            EXISTING_WAYBILL2,
            new Waybill(3, Date.valueOf("2022-11-06"), EXISTING_ORG_ID3),
            new Waybill(4, Date.valueOf("2022-11-07"), EXISTING_ORG_ID1),
            new Waybill(5, Date.valueOf("2022-11-08"), EXISTING_ORG_ID2),
            new Waybill(6, Date.valueOf("2022-11-09"), EXISTING_ORG_ID3)
    );


    private static final @NotNull HikariDataSource DATA_SOURCE = DataSourceManager.getHikariDataSource();

    private final @NotNull WaybillDAO waybillDAO = new WaybillDAO(DATA_SOURCE);

    @AfterAll
    static void closeConnection() {

        FlywayInitializer.initDB();
        DATA_SOURCE.close();
    }

    @BeforeEach
    void initDB() {
        FlywayInitializer.initDB();
    }

    @Test
    @DisplayName("Should get single Waybill instance by provided id")
    void shouldGetById() {

        Waybill waybill = waybillDAO.get(EXISTING_ID1);
        assertEquals(EXISTING_WAYBILL1, waybill);
    }

    @Test
    @DisplayName("Should get list of all Waybill instances")
    void shouldGetAllWaybills() {
        System.out.println(ALL_WAYBILLS);
        List<@NotNull Waybill> waybills = waybillDAO.all();
        System.out.println(waybills);
        MatcherAssert.assertThat(ALL_WAYBILLS, Matchers.containsInAnyOrder(waybills.toArray()));
    }

    @Test
    @DisplayName("Should update Waybill instance")
    void shouldUpdateWaybill() {

        int id = EXISTING_ID2;
        Waybill expectedWaybill = new Waybill(id, Date.valueOf("2022-01-01"), EXISTING_ORG_ID3);

        int sizeBefore = waybillDAO.all().size();
        waybillDAO.update(expectedWaybill);
        int sizeAfter = waybillDAO.all().size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(expectedWaybill, waybillDAO.get(id));
    }

    @Test
    @DisplayName("Should insert new Waybill instance")
    void shouldSaveNewWaybill() {

        int sizeBefore = waybillDAO.all().size();
        int nextId = sizeBefore + 1;
        Waybill newWaybill = new Waybill(nextId, Date.valueOf("2022-12-31"), EXISTING_ORG_ID3);

        waybillDAO.save(newWaybill);
        int sizeAfter = waybillDAO.all().size();

        assertEquals(sizeBefore + 1, sizeAfter);
        assertEquals(newWaybill, waybillDAO.get(nextId));
    }

    @Test
    @DisplayName("Should delete Waybill instance by provided id")
    void shouldDeleteWaybill() {

        int id = EXISTING_ID1;
        int sizeBefore = waybillDAO.all().size();
        waybillDAO.delete(id);
        int sizeAfter = waybillDAO.all().size();

        assertEquals(sizeBefore - 1, sizeAfter);
        assertTrue(waybillDAO.get(id).isEmpty());
    }

    @Test
    @DisplayName("Should delete all instances from Waybill table")
    void shouldDeleteAllWaybills() {

        assertFalse(waybillDAO.all().isEmpty());
        waybillDAO.deleteAll();
        assertTrue(waybillDAO.all().isEmpty());
    }
}
