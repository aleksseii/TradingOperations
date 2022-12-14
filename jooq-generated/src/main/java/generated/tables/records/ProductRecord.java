/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;


import generated.tables.Product;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ProductRecord extends UpdatableRecordImpl<ProductRecord> implements Record3<Integer, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.product.product_id</code>.
     */
    public ProductRecord setProductId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.product.product_id</code>.
     */
    public Integer getProductId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.product.name</code>.
     */
    public ProductRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.product.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.product.internal_code</code>.
     */
    public ProductRecord setInternalCode(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.product.internal_code</code>.
     */
    public String getInternalCode() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Integer, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Product.PRODUCT.PRODUCT_ID;
    }

    @Override
    public Field<String> field2() {
        return Product.PRODUCT.NAME;
    }

    @Override
    public Field<String> field3() {
        return Product.PRODUCT.INTERNAL_CODE;
    }

    @Override
    public Integer component1() {
        return getProductId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String component3() {
        return getInternalCode();
    }

    @Override
    public Integer value1() {
        return getProductId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String value3() {
        return getInternalCode();
    }

    @Override
    public ProductRecord value1(Integer value) {
        setProductId(value);
        return this;
    }

    @Override
    public ProductRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public ProductRecord value3(String value) {
        setInternalCode(value);
        return this;
    }

    @Override
    public ProductRecord values(Integer value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ProductRecord
     */
    public ProductRecord() {
        super(Product.PRODUCT);
    }

    /**
     * Create a detached, initialised ProductRecord
     */
    public ProductRecord(Integer productId, String name, String internalCode) {
        super(Product.PRODUCT);

        setProductId(productId);
        setName(name);
        setInternalCode(internalCode);
    }

    /**
     * Create a detached, initialised ProductRecord
     */
    public ProductRecord(generated.tables.pojos.Product value) {
        super(Product.PRODUCT);

        if (value != null) {
            setProductId(value.getProductId());
            setName(value.getName());
            setInternalCode(value.getInternalCode());
        }
    }
}
