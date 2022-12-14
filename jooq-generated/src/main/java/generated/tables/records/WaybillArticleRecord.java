/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;


import generated.tables.WaybillArticle;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WaybillArticleRecord extends UpdatableRecordImpl<WaybillArticleRecord> implements Record5<Integer, Long, Integer, Integer, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.waybill_article.waybill_article_id</code>.
     */
    public WaybillArticleRecord setWaybillArticleId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill_article.waybill_article_id</code>.
     */
    public Integer getWaybillArticleId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.waybill_article.price</code>.
     */
    public WaybillArticleRecord setPrice(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill_article.price</code>.
     */
    public Long getPrice() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.waybill_article.amount</code>.
     */
    public WaybillArticleRecord setAmount(Integer value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill_article.amount</code>.
     */
    public Integer getAmount() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.waybill_article.waybill_id</code>.
     */
    public WaybillArticleRecord setWaybillId(Integer value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill_article.waybill_id</code>.
     */
    public Integer getWaybillId() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.waybill_article.product_id</code>.
     */
    public WaybillArticleRecord setProductId(Integer value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill_article.product_id</code>.
     */
    public Integer getProductId() {
        return (Integer) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Integer, Long, Integer, Integer, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Integer, Long, Integer, Integer, Integer> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return WaybillArticle.WAYBILL_ARTICLE.WAYBILL_ARTICLE_ID;
    }

    @Override
    public Field<Long> field2() {
        return WaybillArticle.WAYBILL_ARTICLE.PRICE;
    }

    @Override
    public Field<Integer> field3() {
        return WaybillArticle.WAYBILL_ARTICLE.AMOUNT;
    }

    @Override
    public Field<Integer> field4() {
        return WaybillArticle.WAYBILL_ARTICLE.WAYBILL_ID;
    }

    @Override
    public Field<Integer> field5() {
        return WaybillArticle.WAYBILL_ARTICLE.PRODUCT_ID;
    }

    @Override
    public Integer component1() {
        return getWaybillArticleId();
    }

    @Override
    public Long component2() {
        return getPrice();
    }

    @Override
    public Integer component3() {
        return getAmount();
    }

    @Override
    public Integer component4() {
        return getWaybillId();
    }

    @Override
    public Integer component5() {
        return getProductId();
    }

    @Override
    public Integer value1() {
        return getWaybillArticleId();
    }

    @Override
    public Long value2() {
        return getPrice();
    }

    @Override
    public Integer value3() {
        return getAmount();
    }

    @Override
    public Integer value4() {
        return getWaybillId();
    }

    @Override
    public Integer value5() {
        return getProductId();
    }

    @Override
    public WaybillArticleRecord value1(Integer value) {
        setWaybillArticleId(value);
        return this;
    }

    @Override
    public WaybillArticleRecord value2(Long value) {
        setPrice(value);
        return this;
    }

    @Override
    public WaybillArticleRecord value3(Integer value) {
        setAmount(value);
        return this;
    }

    @Override
    public WaybillArticleRecord value4(Integer value) {
        setWaybillId(value);
        return this;
    }

    @Override
    public WaybillArticleRecord value5(Integer value) {
        setProductId(value);
        return this;
    }

    @Override
    public WaybillArticleRecord values(Integer value1, Long value2, Integer value3, Integer value4, Integer value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WaybillArticleRecord
     */
    public WaybillArticleRecord() {
        super(WaybillArticle.WAYBILL_ARTICLE);
    }

    /**
     * Create a detached, initialised WaybillArticleRecord
     */
    public WaybillArticleRecord(Integer waybillArticleId, Long price, Integer amount, Integer waybillId, Integer productId) {
        super(WaybillArticle.WAYBILL_ARTICLE);

        setWaybillArticleId(waybillArticleId);
        setPrice(price);
        setAmount(amount);
        setWaybillId(waybillId);
        setProductId(productId);
    }

    /**
     * Create a detached, initialised WaybillArticleRecord
     */
    public WaybillArticleRecord(generated.tables.pojos.WaybillArticle value) {
        super(WaybillArticle.WAYBILL_ARTICLE);

        if (value != null) {
            setWaybillArticleId(value.getWaybillArticleId());
            setPrice(value.getPrice());
            setAmount(value.getAmount());
            setWaybillId(value.getWaybillId());
            setProductId(value.getProductId());
        }
    }
}
