package ru.aleksseii.model;

public record WaybillArticle(int waybillArticleId,
                             long price,
                             int amount,
                             int waybillId,
                             int productId) {

    public WaybillArticle(long price, int amount, int waybillId, int productId) {
        this(0, price, amount, waybillId, productId);
    }

    public WaybillArticle(int waybillArticleId, long price, int waybillId, int productId) {
        this(waybillArticleId, price, 1, waybillId, productId);
    }

    public WaybillArticle(long price, int waybillId, int productId) {
        this(0, price, waybillId, productId);
    }

    public WaybillArticle() {
        this(0, 0, 0, 0, 0);
    }

    public boolean isEmpty() {
        return this.equals(new WaybillArticle());
    }
}
