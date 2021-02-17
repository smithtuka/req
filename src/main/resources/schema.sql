-- USERS
DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS(ID BIGINT(3) PRIMARY KEY AUTO_INCREMENT, -- IDENTITY
                      FIRST_NAME VARCHAR(60),
                      LAST_NAME VARCHAR(60),
                      ROLE VARCHAR(10),
                      ADDRESS_ID BIGINT(3)

                  );
INSERT INTO USERS(FIRST_NAME, LAST_NAME, ROLE, ADDRESS_ID) VALUES
('Smith', 'TUKA',  'ADMIN', 1),
('Brendah', 'Ashabahebwa', 'ACCOUNTANT', 2),
('Alice', 'Nantunga',  'ADMIN', 1),
('Edward', 'Muhoozi', 'CUSTOMER',4),
('Edwin', 'Aruho',  'CUSTOMER', 2),
('Brenda', 'Nyenga',  'CUSTOMER', 3),
('Lydia', 'Akite',  'CUSTOMER', 4),
('SuperDeal HW', 'Ltd', 'SUPPLIER', 1),
('Shamim and', 'Shifa', 'SUPPLIER', 2),
('Hardware World', 'Ltd, NTINDA', 'SUPPLIER', 1);

-- USERCREDENTIALS
DROP TABLE IF EXISTS USERCREDENTIALS;
CREATE TABLE USERCREDENTIALS(
                   USERNAME VARCHAR(30) PRIMARY KEY,
                   PASSWORD VARCHAR(30),
                   IS_ACTIVE BOOLEAN,
                   USER_ID BIGINT(3)
);
INSERT INTO USERCREDENTIALS( USERNAME, PASSWORD, USER_ID, IS_ACTIVE ) VALUES
( 'smithtuka', 'abcd1234',  1, TRUE),
( 'breash', 'abcd1234',  2, FALSE),
( 'nantunga', 'abcd1234', 3, TRUE);


-- COMMENTS
DROP TABLE IF EXISTS COMMENTS;
CREATE TABLE COMMENTS(
                        ID BIGINT(3)  NOT NULL AUTO_INCREMENT PRIMARY KEY , --IDENTITY
                        NARRATION VARCHAR(255),
                        REQUISITION_ID BIGINT(3),
                        COMMENTER_ID BIGINT(3));
INSERT INTO  COMMENTS (NARRATION, REQUISITION_ID, COMMENTER_ID) values
('please review and resubmit',1,2),
('cement is above required, 20 bags are enough',1,2),
('this can wait',2, 1),
('we need to agree on this, please call me first',2,3);


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
                           ADDRESS_ID BIGINT(3),
                           CUSTOMER_ID BIGINT(3)
);
INSERT INTO PROJECTS(NAME, IS_ACTIVE, ADDRESS_ID, CUSTOMER_ID) VALUES --,  ADDRESS_ID
('KIIRA-BULINDO', TRUE, 1, 4),
('LMT-GLENVILLE', FALSE,  2, 5),
('AE-EBB RD', TRUE,  1, 5),
('KIIRA-BULINDO2', FALSE,  4, 4);

-- REQUISITIONS;
DROP TABLE IF EXISTS REQUISITIONS;
CREATE TABLE REQUISITIONS (
            ID BIGINT(3) PRIMARY KEY   AUTO_INCREMENT, -- IDENTITY
            APPROVAL_STATUS VARCHAR(30),
            STAGE_ID BIGINT(3),
            REQUEST_DATE TIMESTAMP,
            REQUESTER_ID BIGINT(3)
);
INSERT INTO REQUISITIONS(APPROVAL_STATUS, STAGE_ID, REQUEST_DATE, REQUESTER_ID ) VALUES
('PENDING',  1, now(), 1),
('APPROVED',  2, now(), 1),
('REJECTED',  3, now(), 1);

-- STAGES
DROP TABLE IF EXISTS STAGES;
CREATE TABLE STAGES (
                              ID BIGINT(3) PRIMARY KEY   AUTO_INCREMENT, -- IDENTITY
                              NAME VARCHAR(60),
                              BUDGET DECIMAL,
                              PROJECT_ID BIGINT(3),
                              IS_ACTIVE BOOLEAN,
                              START_DATE TIMESTAMP,
                              PLANNED_END_DATE TIMESTAMP
);
INSERT INTO STAGES(NAME, BUDGET, PROJECT_ID, IS_ACTIVE, START_DATE, PLANNED_END_DATE) VALUES
('FOUNDATION',  4500000, 2, TRUE, '2021-02-04 17:00', '2021-05-14'),
('SUBSTRUCTURE',  1000000, 1, TRUE, '2021-02-14 17:00', '2021-07-14' ),
('ROOFING',  3500000, 1, FALSE, '2021-03-14 17:00', '2021-12-14'),
('FINISHES',  2500000, 3, FALSE, '2021-08-14 17:00', '2022-02-14');


--  ADDRESSES
DROP TABLE IF EXISTS ADDRESSES;
CREATE TABLE  ADDRESSES(
                           ID BIGINT(3) PRIMARY KEY   AUTO_INCREMENT, -- IDENTITY
                           ZIP VARCHAR (30),
                           STREET VARCHAR(60),
                           DISTRICT VARCHAR(30),
                           STATE VARCHAR(30)
);
INSERT INTO ADDRESSES(ZIP, STREET, DISTRICT, STATE)
VALUES ( '1042', 'PLOT 30, NTINDA ROAD', 'KAMPALA', 'UGANDA'),
       ( '585438', 'PLOT 33, BIHARWE', 'MBARARA', 'UGANDA'),
       ( '330', 'PLOT 102, AGENDA', 'WAKISO', 'UGANDA'),
       ( '8154', '1042, Todd Farm Dr', 'ELGIN, IL', 'USA');

