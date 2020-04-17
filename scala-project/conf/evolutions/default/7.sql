-- !Ups

CREATE TABLE "WishlistedProduct" (
	"Id"	TEXT NOT NULL UNIQUE,
	"UserId"	TEXT NOT NULL,
	"ProductId"	TEXT NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("UserId") REFERENCES "User"("Id"),
	FOREIGN KEY("ProductId") REFERENCES "Product"("Id")
);

-- !Downs

DROP TABLE IF EXISTS "WishlistedProduct";