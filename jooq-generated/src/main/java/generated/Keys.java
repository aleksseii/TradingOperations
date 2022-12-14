/*
 * This file is generated by jOOQ.
 */
package generated;


import generated.tables.Organization;
import generated.tables.Product;
import generated.tables.Waybill;
import generated.tables.WaybillArticle;
import generated.tables.records.OrganizationRecord;
import generated.tables.records.ProductRecord;
import generated.tables.records.WaybillArticleRecord;
import generated.tables.records.WaybillRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<OrganizationRecord> ORGANIZATION_INN_KEY = Internal.createUniqueKey(Organization.ORGANIZATION, DSL.name("organization_inn_key"), new TableField[] { Organization.ORGANIZATION.INN }, true);
    public static final UniqueKey<OrganizationRecord> ORGANIZATION_PK = Internal.createUniqueKey(Organization.ORGANIZATION, DSL.name("organization_pk"), new TableField[] { Organization.ORGANIZATION.ORG_ID }, true);
    public static final UniqueKey<ProductRecord> PRODUCT_PK = Internal.createUniqueKey(Product.PRODUCT, DSL.name("product_pk"), new TableField[] { Product.PRODUCT.PRODUCT_ID }, true);
    public static final UniqueKey<WaybillRecord> WAYBILL_PK = Internal.createUniqueKey(Waybill.WAYBILL, DSL.name("waybill_pk"), new TableField[] { Waybill.WAYBILL.WAYBILL_ID }, true);
    public static final UniqueKey<WaybillArticleRecord> WAYBILL_ARTICLE_PK = Internal.createUniqueKey(WaybillArticle.WAYBILL_ARTICLE, DSL.name("waybill_article_pk"), new TableField[] { WaybillArticle.WAYBILL_ARTICLE.WAYBILL_ARTICLE_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<WaybillRecord, OrganizationRecord> WAYBILL__WAYBILL_ORG_SENDER_ID_FKEY = Internal.createForeignKey(Waybill.WAYBILL, DSL.name("waybill_org_sender_id_fkey"), new TableField[] { Waybill.WAYBILL.ORG_SENDER_ID }, Keys.ORGANIZATION_PK, new TableField[] { Organization.ORGANIZATION.ORG_ID }, true);
    public static final ForeignKey<WaybillArticleRecord, ProductRecord> WAYBILL_ARTICLE__WAYBILL_ARTICLE_PRODUCT_ID_FKEY = Internal.createForeignKey(WaybillArticle.WAYBILL_ARTICLE, DSL.name("waybill_article_product_id_fkey"), new TableField[] { WaybillArticle.WAYBILL_ARTICLE.PRODUCT_ID }, Keys.PRODUCT_PK, new TableField[] { Product.PRODUCT.PRODUCT_ID }, true);
    public static final ForeignKey<WaybillArticleRecord, WaybillRecord> WAYBILL_ARTICLE__WAYBILL_ARTICLE_WAYBILL_ID_FKEY = Internal.createForeignKey(WaybillArticle.WAYBILL_ARTICLE, DSL.name("waybill_article_waybill_id_fkey"), new TableField[] { WaybillArticle.WAYBILL_ARTICLE.WAYBILL_ID }, Keys.WAYBILL_PK, new TableField[] { Waybill.WAYBILL.WAYBILL_ID }, true);
}
