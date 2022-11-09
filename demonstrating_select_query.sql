-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlCheckUsingColumnsForFile

-- noinspection SqlResolveForFile

SELECT o.org_id, o.name AS org_name,
       w.waybill_id, w.waybill_date, 
       w_a.price, w_a.amount, w_a.price * w_a.amount AS total,
       p.product_id, p.name, p.internal_code
  FROM organization         AS o
       JOIN waybill         AS w   ON o.org_id = w.org_sender_id
       JOIN waybill_article AS w_a USING(waybill_id)
       JOIN product         AS p   USING(product_id)
ORDER BY o.org_id, w.waybill_date, w.waybill_id, total DESC, p.product_id;