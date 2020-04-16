-- !Ups

CREATE TABLE "Product" (
	"Id"	TEXT NOT NULL UNIQUE,
	"Name"	TEXT NOT NULL,
	"Description" TEXT NOT NULL,
	"Price"	REAL,
	"PicUrl"	TEXT,
	"Quantity"	INTEGER NOT NULL DEFAULT 0,
	"CategoryId"	TEXT NOT NULL,
	PRIMARY KEY("Id")
	FOREIGN KEY("CategoryId") REFERENCES "Category"("Id")
);

CREATE TABLE "Opinion" (
	"Id"	TEXT NOT NULL UNIQUE,
	"UserId"	TEXT NOT NULL,
	"ProductId"	TEXT NOT NULL,
	"Content"	TEXT NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("UserId") REFERENCES "User"("Id"),
	FOREIGN KEY("ProductId") REFERENCES "Product"("Id")
);

-- !Downs

DROP TABLE IF EXISTS "Product";

DROP TABLE IF EXISTS "Opinion";