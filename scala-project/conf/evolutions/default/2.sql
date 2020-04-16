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
)

-- !Downs

DROP TABLE IF EXISTS "Product"