
create sequence INFORMATION_SCHEMA.hibernate_sequence;
-- USERS
DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS(ID BIGINT(3) PRIMARY KEY AUTO_INCREMENT, -- IDENTITY
                      FIRST_NAME VARCHAR(60),
                      LAST_NAME VARCHAR(60),
                      ROLE VARCHAR(10),
                      EMAIL VARCHAR(60),
                      ADDRESS_ID BIGINT(3),
                      PHONE VARCHAR(30)
                  );
INSERT INTO USERS(FIRST_NAME, LAST_NAME, ROLE, ADDRESS_ID, EMAIL) VALUES
('Smith', 'TUKA',  'ADMIN', 1, 'smithtuka@gmail.com'),
('Brendah', 'Ashabahebwa', 'ACCOUNTANT', 2, 'rms.galbern@gmail.com'),
('Alice', 'Nantunga',  'ADMIN', 1, 'nantunga@gmail.com'),
('Edward', 'Muhoozi', 'CUSTOMER',4, 'rms.galbern@gmail.com'),
('Edwin', 'Aruho',  'CUSTOMER', 2, 'rms.galbern@gmail.com'),
('Brenda', 'Nyenga',  'CUSTOMER', 3, 'rms.galbern@gmail.com'),
('Lydia', 'Akite',  'CUSTOMER', 4, 'rms.galbern@gmail.com'),
('SuperDeal HW', 'Ltd', 'SUPPLIER', 1, 'rms.galbern@gmail.com'),
('Shamim and', 'Shifa', 'SUPPLIER', 2, 'rms.galbern@gmail.com'),
('Hardware World', 'Ltd, NTINDA', 'SUPPLIER', 1, 'rms.galbern@gmail.com');

-- USERCREDENTIALS
DROP TABLE IF EXISTS USERCREDENTIALS;
CREATE TABLE USERCREDENTIALS(
                   USERNAME VARCHAR(30) PRIMARY KEY,
                   PASSWORD VARCHAR(120),
                   ACTIVE BOOLEAN,
                   ROLE VARCHAR(10)
);
INSERT INTO USERCREDENTIALS( USERNAME, PASSWORD, ROLE, ACTIVE ) VALUES
( 'smithtuka', 'abcd1234567',  'ADMIN', TRUE),
( 'breash', 'abcd1234',  'USER', FALSE),
( 'nantunga', '$2a$10$xdruRph9m4o7upnWq7Mu8OXH/3jw8edKG/uc1vOCM.tZ06jGjwKpG', 'ADMIN', TRUE);


-- COMMENTS
DROP TABLE IF EXISTS COMMENTS;
CREATE TABLE COMMENTS(
                        ID BIGINT(3)  NOT NULL AUTO_INCREMENT PRIMARY KEY , --IDENTITY
                        NARRATION VARCHAR(255),
                        REQUISITION_ID BIGINT(3),
                        COMMENTER_ID BIGINT(3),
                        COMMENT_DATE TIMESTAMP);
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
            REQUIRED_DATE TIMESTAMP,
            REQUESTER_ID BIGINT(3),
            CREATED_AT TIMESTAMP
);
INSERT INTO REQUISITIONS(APPROVAL_STATUS, STAGE_ID, REQUIRED_DATE, REQUESTER_ID, CREATED_AT ) VALUES
('AUTHORIZED',  1, now(), 1, now()),
('APPROVED',  2, now(), 1, now()),
('REJECTED',  3, now(), 1, now());

-- STAGES
DROP TABLE IF EXISTS STAGES;
CREATE TABLE STAGES (
                              ID BIGINT(3) PRIMARY KEY   AUTO_INCREMENT, -- IDENTITY
                              NAME VARCHAR(60),
                              BUDGET DECIMAL,
                              _ID BIGINT(3),
                              IS_ACTIVE BOOLEAN,
                              START_DATE TIMESTAMP,
                              PLANNED_END_DATE TIMESTAMP,
                              PROJECT_ID BIGINT not null
);
INSERT INTO STAGES(NAME, BUDGET, PROJECT_ID, IS_ACTIVE, START_DATE, PLANNED_END_DATE) VALUES
('FOUNDATION',  4500000, 2, TRUE, '2021-02-04', '2021-05-14'),
('SUBSTRUCTURE',  1000000, 1, TRUE, '2021-02-14', '2021-07-14' ),
('ROOFING',  3500000, 1, FALSE, '2021-03-14', '2021-12-14'),
('FINISHES',  2500000, 3, FALSE, '2021-08-14', '2022-02-14');


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

-- PROJECTS_APPROVERS
DROP TABLE IF EXISTS PROJECTS_APPROVERS;
CREATE TABLE PROJECTS_APPROVERS(
    PROJECT_ID BIGINT(3) NOT NULL ,
    	APPROVERS_ID BIGINT(3) NOT NULL
);

