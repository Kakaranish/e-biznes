-- !Ups

CREATE TABLE "Order" (
	"Id"	TEXT NOT NULL UNIQUE,
	"UserId"	TEXT NOT NULL,
	"ShippingInfoId"	TEXT NOT NULL,
	"DateCreated"	TEXT NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("UserId") REFERENCES "User"("Id"),
	FOREIGN KEY("ShippingInfoId") REFERENCES "ShippingInfo"("Id"),
);

-- !Downs

DROP TABLE IF EXISTS "Order";