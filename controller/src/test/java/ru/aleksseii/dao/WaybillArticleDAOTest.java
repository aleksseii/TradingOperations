package ru.aleksseii.dao;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import ru.aleksseii.database.ConnectionManager;
import ru.aleksseii.database.FlywayInitializer;
import ru.aleksseii.model.WaybillArticle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class WaybillArticleDAOTest {

    private static final int EXISTING_ID1 = 1;
    private static final int EXISTING_ID2 = 2;

    private static final int EXISTING_WAYBILL_ID1 = 1;
    private static final int EXISTING_WAYBILL_ID2 = 2;

    private static final int EXISTING_PRODUCT_ID1 = 1;
    private static final int EXISTING_PRODUCT_ID2 = 2;

    private static final @NotNull WaybillArticle EXISTING_WAYBILL_ARTICLE1 =
            new WaybillArticle(EXISTING_ID1, 1000, 3, EXISTING_WAYBILL_ID1, EXISTING_PRODUCT_ID1);

    private static final @NotNull WaybillArticle EXISTING_WAYBILL_ARTICLE2 =
            new WaybillArticle(EXISTING_ID2, 100, 7, EXISTING_WAYBILL_ID1, EXISTING_PRODUCT_ID2);

    private static final @NotNull List<@NotNull WaybillArticle> ALL_WAYBILL_ARTICLES = List.of(
            EXISTING_WAYBILL_ARTICLE1,
            EXISTING_WAYBILL_ARTICLE2,
            new WaybillArticle(3, 5000, 2, EXISTING_WAYBILL_ID2, 3),
            new WaybillArticle(4, 500, 3, 3, 4),
            new WaybillArticle(5, 300, 10, 3, 5),
            new WaybillArticle(6, 300, 3, 4, 5),
            new WaybillArticle(7, 450, 5, 5, 4),
            new WaybillArticle(8, 900, 4, 5, EXISTING_PRODUCT_ID1),
            new WaybillArticle(9, 120, 5, 6, EXISTING_PRODUCT_ID2),
            new WaybillArticle(10, 4500, 4, 6, 3),
            new WaybillArticle(11, 1200, 2, 6, 1)
    );

    private static final @NotNull Connection CONNECTION = ConnectionManager.getConnectionOrThrow();

    private final @NotNull WaybillArticleDAO waybillArticleDAO = new WaybillArticleDAO(CONNECTION);

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
    @DisplayName("Should get single WaybillArticle instance by provided id")
    void shouldGetById() {

        WaybillArticle waybillArticle = waybillArticleDAO.get(EXISTING_ID1);
        assertEquals(EXISTING_WAYBILL_ARTICLE1, waybillArticle);
    }

    @Test
    @DisplayName("Should get list of all WaybillArticles instances")
    void shouldGetAllWaybillArticles() {

        List<@NotNull WaybillArticle> waybillArticles = waybillArticleDAO.all();

        MatcherAssert.assertThat(ALL_WAYBILL_ARTICLES, Matchers.containsInAnyOrder(waybillArticles.toArray()));
    }

    @Test
    @DisplayName("Should update WaybillArticle instance")
    void shouldUpdateWaybillArticles() {

        int id = EXISTING_ID2;
        WaybillArticle expectedWaybillArticle = new WaybillArticle(id, 10000L, 2, 1, 1);

        int sizeBefore = waybillArticleDAO.all().size();
        waybillArticleDAO.update(expectedWaybillArticle);
        int sizeAfter = waybillArticleDAO.all().size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(expectedWaybillArticle, waybillArticleDAO.get(id));
    }

    @Test
    @DisplayName("Should insert new WaybillArticle instance")
    void shouldSaveNewWaybillArticle() {

        int sizeBefore = waybillArticleDAO.all().size();
        int nextId = sizeBefore + 1;
        WaybillArticle newWaybillArticle = new WaybillArticle(nextId, 10000L, 2, 1, 1);

        waybillArticleDAO.save(newWaybillArticle);
        int sizeAfter = waybillArticleDAO.all().size();

        assertEquals(sizeBefore + 1, sizeAfter);
        assertEquals(newWaybillArticle, waybillArticleDAO.get(nextId));
    }

    @Test
    @DisplayName("Should delete WaybillArticle instance by provided id")
    void shouldDeleteWaybillArticle() {

        int id = EXISTING_ID1;
        int sizeBefore = waybillArticleDAO.all().size();
        waybillArticleDAO.delete(id);
        int sizeAfter = waybillArticleDAO.all().size();

        assertEquals(sizeBefore - 1, sizeAfter);
        assertTrue(waybillArticleDAO.get(id).isEmpty());
    }

    @Test
    @DisplayName("Should delete all instances from WaybillArticle table")
    void shouldDeleteAllWaybillArticles() {

        assertFalse(waybillArticleDAO.all().isEmpty());
        waybillArticleDAO.deleteAll();
        assertTrue(waybillArticleDAO.all().isEmpty());
    }
}
