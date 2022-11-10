-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlResolveForFile

INSERT INTO waybill(waybill_date, org_sender_id)
VALUES             ('2022-11-04',       1      ),
                   ('2022-11-05',       2      ),
                   ('2022-11-06',       3      ),
                   ('2022-11-07',       1      ),
                   ('2022-11-08',       2      ),
                   ('2022-11-09',       3      );
       

INSERT INTO waybill_article(price, amount, waybill_id, product_id)
VALUES                     ( 1000,    3,        1,           1   ),
                           (  100,    7,        1,           2   ),
                           ( 5000,    2,        2,           3   ),
                           (  500,    3,        3,           4   ),
                           (  300,   10,        3,           5   ),
                           (  300,    3,        4,           5   ),
                           (  450,    5,        5,           4   ),
                           (  900,    4,        5,           1   ),
                           (  120,    5,        6,           2   ),
                           ( 4500,    4,        6,           3   ),
                           ( 1200,    2,        6,           1   );
