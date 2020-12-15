-- USERS
DROP TABLE IF EXISTS UZERS;
CREATE TABLE UZERS(ID BIGINT(3) PRIMARY KEY AUTO_INCREMENT, -- IDENTITY
                      FIRST_NAME VARCHAR(60),
                      LAST_NAME VARCHAR(60),
                      UZERNAME VARCHAR(30),
                      PAZZWORD VARCHAR(30),
                      EMAIL VARCHAR(30),
                      ROLE VARCHAR(10),
                      ADDRESS_ID BIGINT(3),
                      IS_ACTIVE BOOLEAN
                  );
INSERT INTO UZERS(FIRST_NAME, LAST_NAME, UZERNAME, PAZZWORD, EMAIL, ROLE, ADDRESS_ID, IS_ACTIVE) VALUES
('Smith', 'TUKA', 'smithtuka', 'abcd1234', 'smithtuka@gmail.com', 'ADMIN', 1, TRUE),
('Brendah', 'Ashabahebwa', 'breash', 'abcd1234', 'ashabahebwabrendah@gmail.com', 'ACCOUNTANT', 2, FALSE),
('Alice', 'Nantunga', 'nantunga', 'abcd1234', 'nantunga@gmail.com', 'ADMIN', 1, FALSE);

-- COMMENTS
DROP TABLE IF EXISTS COMMENTS;
CREATE TABLE COMMENTS(
                        ID BIGINT(3)  NOT NULL AUTO_INCREMENT PRIMARY KEY , --IDENTITY
                        NARRATION VARCHAR(255),
                        REQUISITION_ID BIGINT(3) );
INSERT INTO  COMMENTS (NARRATION, REQUISITION_ID) values
('please review and resubmit',1),
('cement is above required, 20 bags are enough',1),
('this can wait',2),
('we need to agree on this, please call me first',2);

-- CUSTOMERS
DROP TABLE IF EXISTS CUSTOMERS;
CREATE TABLE CUSTOMERS(ID BIGINT(3) PRIMARY KEY AUTO_INCREMENT, -- IDENTITY
                   FIRST_NAME VARCHAR(60),
                   LAST_NAME VARCHAR(60),
                   ADDRESS_ID BIGINT(3),
                   PROJECT_ID BIGINT(3)
);
INSERT INTO CUSTOMERS(FIRST_NAME, LAST_NAME, ADDRESS_ID, PROJECT_ID) VALUES --,  ADDRESS_ID, PROJECT_ID
('Edward', 'Muhoozi', 1,1),
('Edwin', 'Aruho',  2, 2),
('Brenda', 'Nyenga',  2, 3),
('Lydia', 'Akite',  1, 4);

-- ITEMS
DROP TABLE IF EXISTS ITEMS;
CREATE TABLE ITEMS(ID BIGINT(3) PRIMARY KEY AUTO_INCREMENT, -- IDENTITY
                   DESCRIPTION VARCHAR(60),
                   PRICE DECIMAL,
                   QUANTITY DECIMAL,
                   REQUISITION_ID BIGINT(3),
                   SUPPLIER_ID BIGINT(3)
);
INSERT INTO ITEMS(DESCRIPTION, PRICE,  QUANTITY, REQUISITION_ID, SUPPLIER_ID) VALUES
('Cement', 2800.00,  100, 1, 2),
('Sand', 1650000.00,  5, 1, 1),
('Plascon Paint', 405000.00,  1420, 2, 3);

-- PROJECTS
DROP TABLE IF EXISTS PROJECTS;
CREATE TABLE  PROJECTS(
                           ID BIGINT(3) PRIMARY KEY   AUTO_INCREMENT, -- IDENTITY
                           IS_ACTIVE BOOLEAN,
                           NAME VARCHAR(60),
                           ADDRESS_ID BIGINT(3)
--                            CONSTRAINT FK_PROJECTS FOREIGN KEY(ADDRESS_ID) REFERENCES ADDRESSES(ID)
--
);
INSERT INTO PROJECTS(NAME, IS_ACTIVE, ADDRESS_ID) VALUES --,  ADDRESS_ID
('KIIRA-BULINDO', TRUE, 1),
('LMT-GLENVILLE', FALSE,  2),
('AE-EBB RD RESIDENTIAL', TRUE,  1),
('KIIRA-BULINDO', TRUE,  4);

-- REQUISITIONS;
DROP TABLE IF EXISTS REQUISITIONS;
CREATE TABLE REQUISITIONS (
            ID BIGINT(3) PRIMARY KEY   AUTO_INCREMENT, -- IDENTITY
            APPROVAL_STATUS VARCHAR(30),
            STAGE_ID BIGINT(3)
);
INSERT INTO REQUISITIONS(APPROVAL_STATUS, STAGE_ID) VALUES
('PENDING',  1),
('APPROVED',  2),
('REJECTED',  3);

-- STAGES
DROP TABLE IF EXISTS STAGES;
CREATE TABLE STAGES (
                              ID BIGINT(3) PRIMARY KEY   AUTO_INCREMENT, -- IDENTITY
                              NAME VARCHAR(60),
                              BUDGET DECIMAL,
                              PROJECT_ID BIGINT(3),
                              IS_ACTIVE BOOLEAN
);
INSERT INTO STAGES(NAME, BUDGET, PROJECT_ID, IS_ACTIVE) VALUES
('FOUNDATION',  4500000, 2, TRUE),
('SUBSTRUCTURE',  1000000, 1, TRUE),
('ROOFING',  3500000, 1, FALSE),
('FOUNDATION',  2500000, 3, TRUE);

-- SUPPLIERS;
DROP TABLE IF EXISTS SUPPLIERS;
CREATE TABLE SUPPLIERS(ID BIGINT(3) PRIMARY KEY AUTO_INCREMENT, -- IDENTITY
                       FIRST_NAME VARCHAR(60),
                       LAST_NAME VARCHAR(60),
                       ADDRESS_ID BIGINT(3)
);
INSERT INTO SUPPLIERS(FIRST_NAME, LAST_NAME,  ADDRESS_ID) VALUES
('SuperDeal HW', 'Ltd',  1),
('Kevin', 'Ssekito',  2),
('Shamim and', 'Shifa',  2),
('Hardware World', 'Ltd',  1);

--  ADDRESSES
DROP TABLE IF EXISTS ADDRESSES;
CREATE TABLE  ADDRESSES(
                           ID BIGINT(3) PRIMARY KEY   AUTO_INCREMENT, -- IDENTITY
                           PHONE VARCHAR (30),
                           STREET VARCHAR(60),
                           DISTRICT VARCHAR(30),
                           STATE VARCHAR(30)
);
INSERT INTO ADDRESSES(PHONE, STREET, DISTRICT, STATE)
VALUES ( '256-706-585438', 'PLOT 30, NTINDA ROAD', 'KAMPALA', 'UGANDA'),
       ( '256-785-585438', 'PLOT 33, BIHARWE', 'MBARARA', 'UGANDA'),
       ( '256-771-414-330', 'PLOT 102, AGENDA', 'WAKISO', 'UGANDA');

