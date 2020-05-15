-- !Ups

CREATE TABLE "LoginInfo" (
	"Id"	TEXT NOT NULL UNIQUE,
	"ProviderId"	TEXT NOT NULL,
	"ProviderKey"	TEXT NOT NULL
);

CREATE TABLE "AppUser" (
	"Id"	TEXT NOT NULL UNIQUE,
	"FirstName"	TEXT NOT NULL,
	"LastName"	TEXT NOT NULL,
	"Email"	TEXT NOT NULL,
	"Password"	TEXT NOT NULL
);

CREATE TABLE "AppUserLoginInfo" (
	"UserId"	TEXT NOT NULL,
	"LoginInfoId"	TEXT NOT NULL,
	FOREIGN KEY("UserId") REFERENCES "AppUser"("Id"),
	FOREIGN KEY("LoginInfoId") REFERENCES "LoginInfo"("Id")
);

-- !Downs

DROP TABLE IF EXISTS "LoginInfo";

DROP TABLE IF EXISTS "AppUser";

DROP TABLE IF EXISTS "AppUserLoginInfo";