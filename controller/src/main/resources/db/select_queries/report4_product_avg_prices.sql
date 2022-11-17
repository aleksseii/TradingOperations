-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlCheckUsingColumnsForFile

-- noinspection SqlResolveForFile

SELECT p.product_id, p.name, p.internal_code,
       ROUND(AVG(w_a.price), 4) AS avg_price
  FROM product              AS  p
       JOIN waybill_article AS w_a USING(product_id)
       JOIN waybill         AS  w  USING(waybill_id)
 WHERE    w.waybill_date BETWEEN ? AND ?
 GROUP BY p.product_id
 ORDER BY p.product_id;
 