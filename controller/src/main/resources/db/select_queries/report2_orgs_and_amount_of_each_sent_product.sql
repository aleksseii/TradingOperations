-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlCheckUsingColumnsForFile

-- noinspection SqlResolveForFile

SELECT o.org_id, o.inn, o.name, o.bank_account,
       w_a.product_id, SUM(w_a.amount) AS total_amount
  FROM organization         AS  o
       JOIN waybill         AS  w  ON o.org_id = w.org_sender_id
       JOIN waybill_article AS w_a USING(waybill_id)
 GROUP BY o.org_id, w_a.product_id
 ORDER BY o.org_id, w_a.product_id
 