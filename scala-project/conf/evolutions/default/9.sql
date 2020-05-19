-- !Ups

CREATE TABLE "LoginInfo" (
	"Id"	TEXT NOT NULL UNIQUE,
	"ProviderId"	TEXT NOT NULL,
	"ProviderKey"	TEXT NOT NULL
);

CREATE TABLE "AppUser" (
	"Id"	TEXT NOT NULL UNIQUE,
	"Email"	TEXT NOT NULL,
	"FirstName"	TEXT NOT NULL,
	"LastName"	TEXT NOT NULL,
	"Role"	TEXT NOT NULL
);

CREATE TABLE "AppUserLoginInfo" (
	"UserId"	TEXT NOT NULL,
	"LoginInfoId"	TEXT NOT NULL,
	FOREIGN KEY("UserId") REFERENCES "AppUser"("Id"),
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

-- !Downs

DROP TABLE IF EXISTS "LoginInfo";

DROP TABLE IF EXISTS "AppUser";

DROP TABLE IF EXISTS "AppUserLoginInfo";

DROP TABLE IF EXISTS "OAuth2Info";