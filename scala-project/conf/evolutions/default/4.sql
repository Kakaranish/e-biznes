-- !Ups

CREATE TABLE "Payment" (
	"Id"	TEXT NOT NULL UNIQUE,
	"OrderId"	TEXT NOT NULL,
	"MethodCode"	TEXT NOT NULL,
	"DateCreated"	TEXT NOT NULL,
	"DateUpdated"	TEXT NOT NULL,
	"State"	TEXT NOT NULL,
	"AmountOfMoney"	REAL NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("OrderId") REFERENCES "Order"("Id")
);

-- !Downs

DROP TABLE IF EXISTS "Payment";