-- !Ups

CREATE TABLE "OrderedProduct" (
	"Id"	TEXT NOT NULL UNIQUE,
	"OrderId"	TEXT NOT NULL,
	"ProductId"	TEXT NOT NULL,
	"Quantity"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("Id"),
	FOREIGN KEY("ProductId") REFERENCES "Product"("Id"),
	FOREIGN KEY("OrderId") REFERENCES "Order"("Id")
);

-- !Downs

DROP TABLE IF EXISTS "OrderedProduct";