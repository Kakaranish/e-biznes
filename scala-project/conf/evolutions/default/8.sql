-- !Ups

BEGIN TRANSACTION;
INSERT INTO "WishlistedProduct" VALUES ('b4e51752-ba51-450b-bc46-b989ae50dfae','3965ea13-169a-4fe9-8a8e-cca06a110132','2945ace4-c782-4448-8b14-e8b942688662');
INSERT INTO "OrderedProduct" VALUES ('00fce47c-5ad6-4be4-a578-e8b0872db9d6','298b7b44-4cf6-4de4-90a1-0d4c5ea8b205','b004cdd4-1016-4776-bb9a-f708b398ac6f',1);
INSERT INTO "OrderedProduct" VALUES ('53de0062-3532-4e1c-a757-a3e15b34275b','298b7b44-4cf6-4de4-90a1-0d4c5ea8b205','2945ace4-c782-4448-8b14-e8b942688662',1);
INSERT INTO "OrderedProduct" VALUES ('ec5a1a24-0ec2-472c-99ba-8837fe897ac3','e125aa0d-be02-4cfc-a571-1940c6a6fdbe','78cb2bb2-2f99-4f7c-811a-3df44bc6198a',2);
INSERT INTO "Order" VALUES ('298b7b44-4cf6-4de4-90a1-0d4c5ea8b205','3965ea13-169a-4fe9-8a8e-cca06a110132','24baf1fd-de1b-4b6c-b96a-d529cd23139a','3e9ab294-188c-43c8-972a-d03f9e6e1a98','2020-04-03T09:00Z');
INSERT INTO "Order" VALUES ('e125aa0d-be02-4cfc-a571-1940c6a6fdbe','2b0491f9-e733-49d3-8930-0f77199714bc','9c13dcd9-cfa7-4342-b9ad-c2cad87c0036','e905640c-680d-47a2-a32c-e0793c4e8320','2020-04-10T21:30.000Z');
INSERT INTO "Payment" VALUES ('3e9ab294-188c-43c8-972a-d03f9e6e1a98','TRANSFER','2020-04-10T:10:00:00Z');
INSERT INTO "Payment" VALUES ('e905640c-680d-47a2-a32c-e0793c4e8320','BLIK','2020-04-12T:21:37:00Z');
INSERT INTO "ShippingInfo" VALUES ('24baf1fd-de1b-4b6c-b96a-d529cd23139a','Poland','Krakow','Jana Pawla 21/37','30-347');
INSERT INTO "ShippingInfo" VALUES ('9c13dcd9-cfa7-4342-b9ad-c2cad87c0036','Poland','Wroclaw','Chopina 33','22-221');
INSERT INTO "Opinion" VALUES ('43004268-0533-49d2-a1f9-802a05e2734e','3965ea13-169a-4fe9-8a8e-cca06a110132','b004cdd4-1016-4776-bb9a-f708b398ac6f','NICE!');
INSERT INTO "Opinion" VALUES ('2b0491f9-e733-49d3-8930-0f77199714bc','2b0491f9-e733-49d3-8930-0f77199714bc','78cb2bb2-2f99-4f7c-811a-3df44bc6198a','Killer game');
INSERT INTO "Product" VALUES ('b004cdd4-1016-4776-bb9a-f708b398ac6f','Red Dead Redemption 2','Some nice game',200.0,NULL,22,'4be58f4f-2685-4310-8d87-63c10a7c797d');
INSERT INTO "Product" VALUES ('78cb2bb2-2f99-4f7c-811a-3df44bc6198a','Horizon: Zero Dawn','Some better game',100.0,NULL,11,'4be58f4f-2685-4310-8d87-63c10a7c797d');
INSERT INTO "Product" VALUES ('2945ace4-c782-4448-8b14-e8b942688662','Skateboard','Nice to be fit',200.0,NULL,5,'83b12577-2e71-4869-9172-441bd4e4bc78');
INSERT INTO "Notification" VALUES ('4a77adec-fe4d-4c75-a591-2cdd0fce8118','3965ea13-169a-4fe9-8a8e-cca06a110132','Notification 1',0);
INSERT INTO "Notification" VALUES ('7ceb2f57-d7d6-4812-9ae8-2910c081b0c3','3965ea13-169a-4fe9-8a8e-cca06a110132','Notification2',1);
INSERT INTO "User" VALUES ('3965ea13-169a-4fe9-8a8e-cca06a110132','jan.kowalski@gmail.com','Jan','Kowalski','NotEncryptedYet123');
INSERT INTO "User" VALUES ('2b0491f9-e733-49d3-8930-0f77199714bc','anna.nowak@mail.com','Anna','Nowak','NotEncryptedYet123');
INSERT INTO "Category" VALUES ('83b12577-2e71-4869-9172-441bd4e4bc78','Sport');
INSERT INTO "Category" VALUES ('4be58f4f-2685-4310-8d87-63c10a7c797d','Games');
COMMIT;

-- !Downs