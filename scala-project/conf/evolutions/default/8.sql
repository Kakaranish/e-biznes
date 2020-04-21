-- !Ups

BEGIN TRANSACTION;
INSERT INTO "WishlistItem" VALUES ('b4e51752-ba51-450b-bc46-b989ae50dfae','3965ea13-169a-4fe9-8a8e-cca06a110132','2945ace4-c782-4448-8b14-e8b942688662');
INSERT INTO "Cart" VALUES ('d7a23fe3-6e69-451d-ad72-584e4e7e3c8e',0,'2020-04-19T20:00:00Z');
INSERT INTO "Cart" VALUES ('bb5aab63-4f1d-47f2-83cd-98f91c09645e',1,'2020-04-19T20:00:00Z');
INSERT INTO "Order" VALUES ('298b7b44-4cf6-4de4-90a1-0d4c5ea8b205','d7a23fe3-6e69-451d-ad72-584e4e7e3c8e','3965ea13-169a-4fe9-8a8e-cca06a110132','24baf1fd-de1b-4b6c-b96a-d529cd23139a','2020-04-03T09:00Z');
INSERT INTO "Order" VALUES ('e125aa0d-be02-4cfc-a571-1940c6a6fdbe','bb5aab63-4f1d-47f2-83cd-98f91c09645e','2b0491f9-e733-49d3-8930-0f77199714bc','9c13dcd9-cfa7-4342-b9ad-c2cad87c0036','2020-04-10T21:30.000Z');
INSERT INTO "Payment" VALUES ('7501579c-c1f3-4f91-9b99-31c5ab594903','298b7b44-4cf6-4de4-90a1-0d4c5ea8b205','BLIK','2020-04-10T20:00:00Z','2020-04-10T20:00:00Z','PAID',22.33);
INSERT INTO "Payment" VALUES ('6396961c-6ba4-4de5-856c-6b48aa9678fd','e125aa0d-be02-4cfc-a571-1940c6a6fdbe','TRANSFER','2020-03-21T21:00:37Z','2020-03-21T21:20:37Z','NOT_PAID',21.37);
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
INSERT INTO "CartItem" VALUES ('994d0712-d8e6-4ba8-bbe0-463137d7bdd6','d7a23fe3-6e69-451d-ad72-584e4e7e3c8e','b004cdd4-1016-4776-bb9a-f708b398ac6f',1);
INSERT INTO "CartItem" VALUES ('4fdf9de7-db97-488c-b2a2-987e9340b2d0','d7a23fe3-6e69-451d-ad72-584e4e7e3c8e','2945ace4-c782-4448-8b14-e8b942688662',1);
INSERT INTO "CartItem" VALUES ('30b86ad1-74ae-46b6-be14-1d79636c769a','bb5aab63-4f1d-47f2-83cd-98f91c09645e','78cb2bb2-2f99-4f7c-811a-3df44bc6198a',2);
COMMIT;

-- !Downs