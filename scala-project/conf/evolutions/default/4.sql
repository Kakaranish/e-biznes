-- !Ups

CREATE TABLE "Payment" (
	"Id"	TEXT NOT NULL UNIQUE,
	"MethodCode"	TEXT NOT NULL,
	"DateTime"	TEXT NOT NULL,
	PRIMARY KEY("Id")
);

-- !Downs

DROP TABLE IF EXISTS "Payment";