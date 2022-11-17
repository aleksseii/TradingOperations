-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlCheckUsingColumnsForFile

-- noinspection SqlResolveForFile

SELECT o.org_id, o.inn, o.name, o.bank_account
  FROM organization         AS  o
       JOIN waybill         AS  w  ON w.org_sender_id = o.org_id
       JOIN waybill_article AS w_a USING(waybill_id)
 GROUP BY o.org_id
 ORDER BY SUM(w_a.amount) DESC
 LIMIT 10;
