-- !Ups

CREATE TABLE "Cart" (
	"Id"	TEXT NOT NULL UNIQUE,
	"IsFinalized"	INTEGER NOT NULL,
	"UpdateDate"	TEXT NOT NULL,
	PRIMARY KEY("Id")
);

CREATE TABLE "CartItem" (
	"Id"	TEXT NOT NULL UNIQUE,
	"CartId"	TEXT NOT NULL,
	"ProductId"	TEXT NOT NULL,
	"Quantity"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("Id"),
	FOREIGN KEY("CartId") REFERENCES "Cart"("Id"),
	FOREIGN KEY("ProductId") REFERENCES "Product"("Id")
);

-- !Downs

DROP TABLE IF EXISTS "Cart";

DROP TABLE IF EXISTS "CartItem";