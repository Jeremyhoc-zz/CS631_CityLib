-- -----------------------------------------------------
-- Schema cityLib
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `cityLib`;
CREATE SCHEMA IF NOT EXISTS `cityLib` DEFAULT CHARACTER SET utf8;
USE `cityLib`;

-- -----------------------------------------------------
-- Table Reader
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Reader`;
CREATE TABLE Reader
(
ReaderID int auto_increment PRIMARY KEY,
RType varchar(20),
RName varchar(50),
RAddress varchar(225),
Phone_Number bigint,
MemStart date,
Fine int,
NumResDocs int,
NumBorDocs int
);

-- -----------------------------------------------------
-- Table Branch
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Branch`;
CREATE TABLE Branch
(
LibID int auto_increment PRIMARY KEY,
LName varchar(50),
LLocation varchar(225)
);

-- -----------------------------------------------------
-- Table Publisher
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Publisher`;
CREATE TABLE Publisher
(
PublisherID int auto_increment PRIMARY KEY,
PubName varchar(50),
PubAddress varchar(225)
);

-- -----------------------------------------------------
-- Table Author
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Author`;
CREATE TABLE Author
(
AuthorID int auto_increment PRIMARY KEY,
Author_Name varchar(50)
);





-- -----------------------------------------------------
-- Table Chief_Editor
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Chief_Editor`;
CREATE TABLE Chief_Editor
(
Editor_ID int auto_increment PRIMARY KEY,
EName varchar(50)
);

-- -----------------------------------------------------
-- Table Document
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Document`;
CREATE TABLE Document
(
DocID int auto_increment PRIMARY KEY,
Title varchar(50),
PDate date,
PublisherID int,
FOREIGN KEY (PublisherID) REFERENCES Publisher(PublisherID)
);

-- -----------------------------------------------------
-- Table Proceedings
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Proceedings`;
CREATE TABLE Proceedings
(
DocID int auto_increment Primary Key,
CDate date,
CLocation varchar(25),
CEditor varchar(50),
FOREIGN KEY (DocID) REFERENCES Document(DocID)
);

-- -----------------------------------------------------
-- Table Copy
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Copy`;
CREATE TABLE Copy
(
DocID int auto_increment,
CopyNo int,
LibID int,
Position varchar(6),
Primary Key(DocID, CopyNo, LibID),
FOREIGN KEY (DocID) REFERENCES Document(DocID),
FOREIGN KEY (LibID) REFERENCES Branch(LibID)
);



-- -----------------------------------------------------
-- Table Borrows
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Borrows`;
CREATE TABLE Borrows
(
BORNUMBER int PRIMARY KEY,
ReaderID int,
DocID int,
CopyNo int,
LibID int,
BDate date,
BTime time,
Foreign Key (DocID,CopyNo,LibID) REFERENCES Copy(DocID,CopyNo,LibID),
Foreign Key (ReaderID) REFERENCES Reader(ReaderID)
);


-- -----------------------------------------------------
-- Table Reserves
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Reserves`;
CREATE TABLE Reserves
(
ResNumber int PRIMARY KEY,
ReaderID int,
DocID int,
CopyNo int,
LibID int,
RDate date,
RTime time,
Foreign Key (DocID,CopyNo,LibID) REFERENCES Copy(DocID,CopyNo,LibID),
Foreign Key (ReaderID) REFERENCES Reader(ReaderID)
);


-- -----------------------------------------------------
-- Table Book
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Book`;
CREATE TABLE Book
(
DocID int,
ISBN varchar(20),
FOREIGN KEY (DocID) REFERENCES Document(DocID)
);


-- -----------------------------------------------------
-- Table Writes
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Writes`;
CREATE TABLE Writes
(
AuthorID int,
DocID int,
Primary Key (AuthorID, DocID),
Foreign Key (AuthorID) REFERENCES Author(AuthorID),
Foreign Key (DocID) REFERENCES Book(DocID)
);




-- -----------------------------------------------------
-- Table Journal_Volume
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Journal_Volume`;
CREATE TABLE Journal_Volume
(
DocID int PRIMARY KEY,
JVolume int,
Editor_ID int,
Foreign Key (DocID) REFERENCES Document(DocID),
FOREIGN KEY (Editor_ID) REFERENCES Chief_Editor(Editor_ID)
);



-- -----------------------------------------------------
-- Table Journal_Issue
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Journal_Issue`;
CREATE TABLE Journal_Issue
(
DocID int,
Issue_Number int,
CHECK (Issue_Number>=0 AND Issue_Number<=10),
Scope varchar(100),
Primary Key(DocID,Issue_Number),
FOREIGN Key (DocID) REFERENCES Journal_Volume(DocID)
);


-- -----------------------------------------------------
-- Table Inv_Editor
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Inv_Editor`;
CREATE TABLE Inv_Editor
(
DocID int,
Issue_Number int,
IE_Name varchar(50),
Foreign Key (DocID,Issue_Number) REFERENCES Journal_Issue(DocID,Issue_Number),
Primary Key(DocID,Issue_Number,IE_Name)
);



-- ------------------------------------------------------
-- Table Admin Login
-- ------------------------------------------------------

DROP TABLE IF EXISTS `AdminLogin`;
CREATE TABLE AdminLogin
(
AdminID varchar(10),
LoginPassword varchar(10),
Primary Key(AdminID, LoginPassword)
);


-- ------------------------------------------------------
-- Populated Data
-- ------------------------------------------------------

INSERT INTO `cityLib`.`Reader` (`RType`, `RName`, `RAddress`, `Phone_Number`, `MemStart`, `Fine`, `NumResDocs`, `NumBorDocs` )
VALUES
('Regular', 'Glen Howell', '648 South Oak St.', '7326975126', '2003-01-01','0','0','0'),
('Regular', 'Jan Sanders', '8044 Newbridge Road', '7321964875', '2002-05-06', '0','0','0'),
('Regular', 'Patsy Peterson', '811 Coffee Street', '7326985874', '2004-06-04','0','0', '0'),
('Employee', 'Randolph Hansen', '316 N. Indian Spring Rd.', '7327896541', '2004-04-08','0','0','1'),
('Student', 'Noah Moss', '7419C Boston St.', '7324856925', '2006-08-06', '0','0', '0'),
('Regular', 'Marcella Fields', '173 Greystone Street', '7324859632', '2009-09-04', '0','0', '0'),
('Student', 'Eula Hardy', '397 Santa Clara St.', '7321859646', '2007-01-06', '0','0', '3'),
('Employee', 'Diane Morris', '16 Highland Street', '7327892584', '2006-02-08', '0','0', '0'),
('Student', 'Betsy Weber', '72 Somerset Dr.', '7325896147', '2008-03-01', '0','0', '0'),
('Student', 'Erika Hoffman', '988 Woreaderodsman Dr.', '7324685216', '2006-06-06', '0','0', '0'),
('Employee', 'Erika Hoffman', '8718 Blue Spring St.', '7329853214', '2004-08-04', '0','0', '0'),
('Student', 'Michelle Page', '339 W. North St.', '7324896156', '2006-07-06', '0','0', '0'),
('Employee', 'Elizabeth Bush', '553 Elizabeth Street', '7328594146', '2004-09-04', '0','0', '0'),
('Student', 'Sonya Murray', '263 Fieldstone Rd.','7328945612', '2009-05-03', '0','0', '5'),
('Regular', 'Jose Alexander', '547 Peg Shop Ave.','7325268943', '2008-04-04','0','0', '0');

INSERT INTO `cityLib`.`Branch` (`LName`, `LLocation`)
VALUES
('Patrick', 'Terre Haute, IN'),
('Hawkins', 'Fort Lauderdale, FL'),
('Craig', 'Minot, ND'),
('Morrison', 'Kingston, NY'),
('Dixon', 'Monroe Township, NJ'),
('Vargas', 'Indian Trail, NC'),
('Grover', 'Zanesville, OH'),
('Gilbert', 'Braintree, MA'),
('Silva', 'Maumee, OH'),
('Schneider', 'Williamsport, PA');

INSERT INTO `cityLib`.`Publisher` (`PubName`, `PubAddress`)
VALUES
('Nathan Gardner', '456 Ketch Harbour Street'),
('Johnathan Evans', '7984 Riverview Street'),
('Betsy Waters', '35 Monroe Road'),
('Stephen Simpson', '70 Snake Hill St.'),
('Naomi Mccoy', '28 Bridgeton St.'),
('Leticia Wolfe', '14 Clay Street'),
('Cassandra Barnett', '8743 Ridgewood St.'),
('Shelly Colon', '675 Old York Drive'),
('Esther Johnson', '34 North Rd.'),
('Terry Townsend', '877 W. Old York St.'),

('Johanna Thompson', '374 Kerwin Ave'),
('Becky Carlson', '4896 Jamie St.'),
('Bianca Sitter', '159 Jesse Drive'),
('Emily Rider', '96 South Rd.'),
('Theo Frank', '479 Grey St.'),
('John Snow', '198 Bling St.'),

 ('Victor Reed', '159 Jesse Drive'),
('Sam Change', '96 South Rd.'),
('Mike Tailor', '479 Grey St.'),
('Alex Scott', '198 Bling St.'),

('Glen Martain', '957 Hanover St.'),
('Kelly Baker', '4763 Grand St.');

INSERT INTO `cityLib`.`Author` (`Author_Name`)
VALUES
('Freda Hunt'),
('Felicia Stewart'),
('Moses Gibbs'),
('Bertha Cox'),
('Monique Hogan'),
('Irene Grant');

INSERT INTO `cityLib`.`Chief_Editor` (`EName`)
VALUES
('Cesar Harvey'),
('Tabitha Bishop'),
('Dorothy Ray'),
('Darrel Washington');

INSERT INTO `cityLib`.`Document` (`Title`, `PDate`, `PublisherID`)
VALUES
('a workshop on technology', '1992-08-20', '1'),
('international conference on power transmission', '2001-07-02', '2'),
('accelerating programming', '1998-07-09', '3'),
('symposium on access space', '1948-01-03', '4'),
('ethics in the workplace', '2008-04-23', '5'),
('2nd institution on engineering', '2003-06-07', '6'),
('RF measurement', '1995-05-05', '7'),
('electromagnetic compatibility', '1985-11-27', '8'),
('acoustic sound enhancement', '1987-12-08', '9'),
('underwater geosciences', '2009-04-22', '10'),
('thief of dawn', '1992-04-19', '11'),
('pirate with sins', '2001-08-05', '12'),
('girls of the curse', '1998-03-02', '13'),
('friends with honor', '1948-06-05', '14'),
('no new friends', '2008-02-20', '15'),
('snakes and dogs', '2003-02-25', '16'),
('man vs nature', '1991-06-03', '17'),
('around the world', '1948-01-09', '18'),
('future in space', '2008-06-19', '19'),
('recycling and refueling', '2002-06-20', '20'),
('life on mars', '2003-02-03', '21'),
('bioenergy', '2012-05-02', '22');

INSERT INTO `cityLib`.`Proceedings` (`CDate`, `CLocation`,`CEditor`)
VALUES
('2011-02-03', 'Easton, PA','Cesar Harvey'),
('2003-02-04', 'Jamaica Plain','Tabitha Bishop'),
('1992-05-08', 'Oshkosh, WI','Cesar Harvey'),
('1994-09-07', 'Torrington, CT','Tabitha Bishop'),
('1998-05-12', 'Wyoming, MI','Cesar Harvey'),
('1989-03-10', 'Marion, NC', 'Edna Lane'),
('2001-12-12', 'Orange Park','abc'),
('2004-12-09', 'Vienna, VA','Tabitha Bishop'),
('2001-08-09', 'Newton, NJ','def'),
('2006-04-07', 'Niles, MI','Cesar Harvey');


INSERT INTO `cityLib`.`Copy` (`CopyNo`, `LibID`, `Position`)
VALUES
('1', '1','001A01'),
('2', '1','001A11'),
('2', '2', '001A02'),
('3', '3', '001A05'),
('4', '4', '001A07'),
('5', '5', '001A11'),
('6', '5', '001A23'),
('7', '4', '001A20'),
('8', '3', '001A21'),
('9', '2', '001A09'),
('10', '1', '001A05'),
('11', '6', '001A16'),
('12', '10', '001A13'),
('13', '9', '001A41'),
('14', '7', '001A08'),
('15', '8', '001A33'),
('16', '3', '001A77'),
('17', '5', '001A71'),
('18', '6', '001A36'),
('19', '9', '001A28'),
('20', '7', '001A63'),
('21', '4', '001A37'),
('22', '1', '001A64');

INSERT INTO `cityLib`.`Borrows` (`BORNUMBER`, `ReaderID`, `DocID`, `CopyNo`, `LibID`, `BDate`, `BTime`)
VALUES
('11', '2', '6', '6', '5', '2016-04-01', '04:02'),
('12', '2', '8', '8', '3', '2016-04-02', '05:03'),
('13', '2', '10', '10', '1', '2016-04-03', '12:22'),
('14', '2', '12', '12', '10', '2016-04-04', '18:53'),
('15', '2', '15', '15', '8', '2016-04-05', '14:24'),
('16', '2', '18', '18', '6', '2016-04-06', '17:25'),
('17', '2', '17', '17', '5', '2016-04-07', '12:46'),
('18', '2', '7', '7', '4', '2016-04-08', '13:27'),
('19', '2', '21', '21', '4', '2016-04-09', '15:08'),
('20', '12', '22', '22', '1', '2016-04-10', '09:48');

INSERT INTO `cityLib`.`Reserves` (`ResNumber`, `ReaderID`, `DocID`, `CopyNo`, `LibID`, `RDate`, `RTime`)
VALUES
('10', '2', '1', '2', '1', '2016-04-01', '03:02'),
('11', '1', '1', '1', '1', '2016-04-01', '04:02'),
('12', '1', '2', '2', '2', '2016-04-02', '05:03'),
('13', '1', '3', '3', '3', '2016-04-03', '12:22'),
('14', '1', '4', '4', '4', '2016-04-04', '18:53'),
('15', '1', '5', '5', '5', '2016-04-05', '14:24'),
('16', '1', '9', '9', '2', '2016-04-06', '17:25'),
('17', '1', '19', '19', '9', '2016-04-07', '12:46'),
('18', '1', '11', '11', '6', '2016-04-08', '13:27'),
('19', '1', '13', '13', '9', '2016-04-09', '15:08'),
('20', '15', '20', '20', '7', '2016-04-10', '09:48');

INSERT INTO `cityLib`.`Book` (`DocID`, `ISBN`)
VALUES
('11', '9576702756985'),
('12', '1610654262418'),
('13', '5009015624281'),
('14', '5162306315438'),
('15', '8756602944289'),
('16', '9245935090583');

INSERT INTO `cityLib`.`Writes` (`AuthorID`, `DocID`)
VALUES
('1', '11'),
('2', '11'),
('3', '12'),
('4', '12'),
('5', '13'),
('6', '13'),
('6', '14'),
('1', '14'),
('5', '15'),
('4', '15'),
('2', '16');

INSERT INTO `cityLib`.`Journal_Volume` (`DocID`, `JVolume`, `Editor_ID`)
VALUES
('17', '8', '1'),
('18', '6', '2'),
('19', '2', '3'),
('20', '9', '4');

INSERT INTO `cityLib`.`Journal_Issue` (`DocID`, `Issue_Number`, `Scope`)
VALUES
('17', '5', 'null'),
('18', '1', 'null'),
('19', '9', 'null'),
('20', '7', 'null');

INSERT INTO `cityLib`.`Inv_Editor` (`DocID`, `Issue_Number`, `IE_Name`)
VALUES
('17', '5', 'Harold Quibble'),
('19', '9', 'Sheena Arbor');

INSERT INTO `cityLib`.`AdminLogin` (`AdminID`, `LoginPassword`)
VALUES
('HP','himpatel'),
('JH', 'jerhoc'),
('AP', 'aapala');
