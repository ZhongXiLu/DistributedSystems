
-- Create tables

CREATE TABLE CHANNEL (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	"NAME" VARCHAR(255) NOT NULL UNIQUE,
	IS_PUBLIC BOOLEAN NOT NULL DEFAULT TRUE,
	IS_ACTIVE BOOLEAN NOT NULL DEFAULT TRUE,
	PRIMARY KEY (ID)
);

CREATE TABLE CHAT_USER (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	"NAME" VARCHAR(255) NOT NULL UNIQUE, 
	PASSWORD VARCHAR(255) NOT NULL,
	IS_ONLINE BOOLEAN NOT NULL, 
	IS_MODERATOR BOOLEAN NOT NULL, 
	PRIMARY KEY (ID)
);

CREATE TABLE CHANNEL_USER (
	USER_ID INTEGER NOT NULL,
	CHANNEL_ID INTEGER NOT NULL,
	FOREIGN KEY (USER_ID) REFERENCES CHAT_USER (ID),
	FOREIGN KEY (CHANNEL_ID) REFERENCES CHANNEL (ID),
	PRIMARY KEY (USER_ID, CHANNEL_ID)
);

CREATE TABLE MESSAGE (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
	CONTENT VARCHAR(2000), 
	"TIMESTAMP" TIMESTAMP NOT NULL, 
	CHANNEL_ID INTEGER NOT NULL, 
	USER_ID INTEGER NOT NULL, 
	FOREIGN KEY (USER_ID) REFERENCES CHAT_USER (ID),
	FOREIGN KEY (CHANNEL_ID) REFERENCES CHANNEL (ID),
	PRIMARY KEY (ID)
);

-- Create default channel

INSERT INTO CHANNEL("NAME") VALUES('Welcome');
