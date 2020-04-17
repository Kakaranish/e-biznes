-- !Ups

CREATE TABLE "ShippingInfo" (
	"Id"	TEXT NOT NULL UNIQUE,
	"Country"	TEXT NOT NULL,
	"City"	TEXT NOT NULL,
	"Address"	TEXT NOT NULL,
	"ZipOrPostcode"	TEXT NOT NULL,
	PRIMARY KEY("Id")
);

-- !Downs

DROP TABLE IF EXISTS "ShippingInfo";