-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlCheckUsingColumnsForFile

-- noinspection SqlResolveForFile

SELECT w.waybill_date,
       p.product_id, p.name, p.internal_code,
       SUM(w_a.amount)             AS amount_per_day,
       SUM(w_a.amount * w_a.price) AS proceeds_per_day
  FROM product              AS  p
       JOIN waybill_article AS w_a USING(product_id)
       JOIN waybill         AS  w  USING(waybill_id)
 WHERE    w.waybill_date BETWEEN ? AND ?
 GROUP BY w.waybill_date, p.product_id
 ORDER BY w.waybill_date, p.product_id;
 