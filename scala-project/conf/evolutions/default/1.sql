-- !Ups

CREATE TABLE "User" (
	"Id"	TEXT NOT NULL UNIQUE,
	"Email"	TEXT NOT NULL,
	"FirstName"	TEXT,
	"LastName"	TEXT,
    "Role"	TEXT NOT NULL,
	PRIMARY KEY("Id")
);

CREATE TABLE "LoginInfo" (
	"Id"	TEXT NOT NULL UNIQUE,
	"ProviderId"	TEXT NOT NULL,
	"ProviderKey"	TEXT NOT NULL
);

CREATE TABLE "UserLoginInfo" (
	"UserId"	TEXT NOT NULL,
	"LoginInfoId"	TEXT NOT NULL,
	FOREIGN KEY("UserId") REFERENCES "User"("Id"),
	FOREIGN KEY("LoginInfoId") REFERENCES "LoginInfo"("Id")
);

CREATE TABLE "PasswordInfo" (
	"Hasher"	TEXT NOT NULL,
	"Password"	TEXT NOT NULL,
	"Salt"	TEXT,
	"LoginInfoId"	TEXT NOT NULL,
	FOREIGN KEY("LoginInfoId") REFERENCES "LoginInfo"("Id")
);

CREATE TABLE "OAuth2Info" (
	"Id"	TEXT NOT NULL UNIQUE,
	"AccessToken"	TEXT NOT NULL,
	"TokenType"	TEXT,
	"ExpiresIn"	INTEGER,
	"RefreshToken"	TEXT,
	"LoginInfoId"	TEXT NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("LoginInfoId") REFERENCES "LoginInfo"("Id")
);

CREATE TABLE IF NOT EXISTS "Category" (
	"Id"	TEXT NOT NULL UNIQUE,
	"Name"	TEXT NOT NULL,
	"IsDeleted"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("Id")
);

CREATE TABLE "Notification" (
	"Id"	TEXT NOT NULL UNIQUE,
	"UserId"	TEXT NOT NULL,
	"Content"	TEXT NOT NULL,
	"IsRead"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("Id"),
	FOREIGN KEY("UserId") REFERENCES "User"("Id")
);

CREATE TABLE "Product" (
	"Id"	TEXT NOT NULL UNIQUE,
	"Name"	TEXT NOT NULL,
	"Description" TEXT NOT NULL,
	"Price"	REAL,
	"PicUrl"	TEXT,
	"Quantity"	INTEGER NOT NULL DEFAULT 0,
	"CategoryId"	TEXT NOT NULL,
	"IsDeleted"	INTEGER NOT NULL DEFAULT 0,
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

CREATE TABLE "ShippingInfo" (
	"Id"	TEXT NOT NULL UNIQUE,
	"Country"	TEXT NOT NULL,
	"City"	TEXT NOT NULL,
	"Address"	TEXT NOT NULL,
	"ZipOrPostcode"	TEXT NOT NULL,
	PRIMARY KEY("Id")
);

CREATE TABLE "Payment" (
	"Id"	TEXT NOT NULL UNIQUE,
	"OrderId"	TEXT NOT NULL,
	"MethodCode"	TEXT NOT NULL,
	"DateCreated"	TEXT NOT NULL,
	"AmountOfMoney"	REAL NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("OrderId") REFERENCES "Order"("Id")
);

CREATE TABLE "Order" (
	"Id"	TEXT NOT NULL UNIQUE,
	"CartId"	TEXT NOT NULL,
	"UserId"	TEXT NOT NULL,
	"ShippingInfoId"	TEXT,
	"DateCreated"	TEXT NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("CartId") REFERENCES "Cart"("Id"),
	FOREIGN KEY("UserId") REFERENCES "User"("Id"),
	FOREIGN KEY("ShippingInfoId") REFERENCES "ShippingInfo"("Id")
);

CREATE TABLE "Cart" (
	"Id"	TEXT NOT NULL UNIQUE,
	"UserId"	TEXT NOT NULL,
	"IsFinalized"	INTEGER NOT NULL,
	"UpdateDate"	TEXT NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("UserId") REFERENCES "User"("Id")
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

CREATE TABLE "WishlistItem" (
	"Id"	TEXT NOT NULL UNIQUE,
	"UserId"	TEXT NOT NULL,
	"ProductId"	TEXT NOT NULL,
	PRIMARY KEY("Id"),
	FOREIGN KEY("UserId") REFERENCES "User"("Id"),
	FOREIGN KEY("ProductId") REFERENCES "Product"("Id")
);

-- !Downs

DROP TABLE IF EXISTS "Category";

DROP TABLE IF EXISTS "User";

DROP TABLE IF EXISTS "Notification";

DROP TABLE IF EXISTS "Product";

DROP TABLE IF EXISTS "Opinion";

DROP TABLE IF EXISTS "ShippingInfo";

DROP TABLE IF EXISTS "Payment";

DROP TABLE IF EXISTS "Cart";

DROP TABLE IF EXISTS "CartItem";

DROP TABLE IF EXISTS "Order";

DROP TABLE IF EXISTS "WishlistItem";

DROP TABLE IF EXISTS "LoginInfo";

DROP TABLE IF EXISTS "UserLoginInfo";

DROP TABLE IF EXISTS "OAuth2Info";