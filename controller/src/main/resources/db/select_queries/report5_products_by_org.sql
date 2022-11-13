-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlCheckUsingColumnsForFile

-- noinspection SqlResolveForFile

SELECT o.org_id, o.inn, o.name AS org_name, o.bank_account,
       p.product_id, p.name AS product_name, p.internal_code
  FROM organization AS o
       LEFT JOIN waybill         AS  w  ON w.org_sender_id = o.org_id
       LEFT JOIN waybill_article AS w_a USING(waybill_id)
       LEFT JOIN product         AS  p  USING(product_id)
 WHERE w.waybill_date BETWEEN ? AND ?
    OR w.waybill_id IS NULL
 GROUP BY o.org_id, p.product_id;