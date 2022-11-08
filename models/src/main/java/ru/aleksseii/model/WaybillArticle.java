package ru.aleksseii.model;

public record WaybillArticle(int waybillArticleId,
                             long price,
                             int amount,
                             int waybillId,
                             int productId) {

    public WaybillArticle(int waybillArticleId, long price, int waybillId, int productId) {
        this(waybillArticleId, price, 1, waybillId, productId);
    }
}
