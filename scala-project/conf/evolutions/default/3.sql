-- !Ups

CREATE TABLE "ShippingInfo" (
	"Id"	TEXT NOT NULL UNIQUE,
	"UserId"	TEXT NOT NULL,
	"Country"	TEXT NOT NULL,
	"City"	TEXT NOT NULL,
	"Address"	TEXT NOT NULL,
	"ZipOrPostcode"	TEXT NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("UserId") REFERENCES "User"("Id")
);

-- !Downs

DROP TABLE IF EXISTS "ShippingInfo";