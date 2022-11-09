-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlResolveForFile

INSERT INTO product(name, internal_code)
VALUES ('product_1', 'code_1'),
       ('product_1', 'code_2'),
       ('product_3', 'code_3'),
       ('product_4', 'code_4'),
       ('product_5', 'code_5');
       

INSERT INTO organization(inn, name, bank_account)
VALUES (111, 'org_1', 'acc_1'),
       (222, 'org_1', 'acc_2'),
       (333, 'org_3', 'acc_3');
       