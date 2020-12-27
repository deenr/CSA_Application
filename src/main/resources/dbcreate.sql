DROP TABLE IF EXISTS Auteur;
CREATE TABLE Auteur (
	auteur_id	INTEGER NOT NULL,
	auteur_naam	TEXT NOT NULL,
	PRIMARY KEY(auteur_id AUTOINCREMENT)
);
DROP TABLE IF EXISTS Boer;
CREATE TABLE Boer (
	auteur_id	INTEGER NOT NULL,
	boer_adres	TEXT NOT NULL,
	FOREIGN KEY(auteur_id) REFERENCES Auteur(auteur_id),
	PRIMARY KEY(auteur_id)
);

DROP TABLE IF EXISTS HaaltAf;
CREATE TABLE HaaltAf (
	klant_id	INTEGER NOT NULL,
	verkoopt_id	INTEGER NOT NULL,
	pakket_weeknr	INTEGER NOT NULL,
	pakket_afgehaald	INTEGER DEFAULT 0,
	FOREIGN KEY(verkoopt_id) REFERENCES Verkoopt(verkoopt_id),
	FOREIGN KEY(klant_id) REFERENCES Klant(auteur_id),
	PRIMARY KEY(verkoopt_id,klant_id)
);

DROP TABLE IF EXISTS Klant;
CREATE TABLE Klant (
	auteur_id	INTEGER NOT NULL,
	PRIMARY KEY(auteur_id),
	FOREIGN KEY(auteur_id) REFERENCES Auteur(auteur_id)
);

DROP TABLE IF EXISTS Pakket;
CREATE TABLE Pakket (
	pakket_id	INTEGER NOT NULL,
	pakket_naam	TEXT NOT NULL,
	pakket_aantalVolwassenen	INTEGER NOT NULL,
	pakket_aantalKinderen	INTEGER NOT NULL,
	PRIMARY KEY(pakket_id AUTOINCREMENT)
);

DROP TABLE IF EXISTS Product;
CREATE TABLE Product (
	product_id	INTEGER NOT NULL,
	product_naam	TEXT NOT NULL,
	product_soort	TEXT DEFAULT NULL,
	PRIMARY KEY(product_id AUTOINCREMENT)
);

DROP TABLE IF EXISTS SchrijftIn;
CREATE TABLE SchrijftIn (
	klant_id	INTEGER NOT NULL,
	verkoopt_id	INTEGER NOT NULL,
	PRIMARY KEY(klant_id,verkoopt_id),
	FOREIGN KEY(klant_id) REFERENCES Klant(auteur_id),
	FOREIGN KEY(verkoopt_id) REFERENCES Verkoopt(verkoopt_id)
);

DROP TABLE IF EXISTS Verkoopt;
CREATE TABLE Verkoopt (
	verkoopt_id	INTEGER NOT NULL,
	auteur_id	INTEGER NOT NULL,
	pakket_id	INTEGER NOT NULL,
	verkoopt_prijs	INTEGER NOT NULL,
	FOREIGN KEY(auteur_id) REFERENCES Boer(auteur_id),
	FOREIGN KEY(pakket_id) REFERENCES Pakket(pakket_id),
	PRIMARY KEY(verkoopt_id AUTOINCREMENT)
);

DROP TABLE IF EXISTS ZitIn;
CREATE TABLE ZitIn (
	product_id	INTEGER NOT NULL,
	verkoopt_id	INTEGER NOT NULL,
	zitIn_hoeveelheid	INTEGER NOT NULL,
	zitIn_weeknr	INTEGER NOT NULL,
	PRIMARY KEY(product_id,verkoopt_id),
	FOREIGN KEY(product_id) REFERENCES Product(product_id),
	FOREIGN KEY(verkoopt_id) REFERENCES Verkoopt(verkoopt_id)
);

DROP TABLE IF EXISTS sqlite_sequence;
CREATE TABLE sqlite_sequence (
	name	,
	seq
);

INSERT INTO Auteur(auteur_naam) VALUES ('Boer Jaak');
INSERT INTO Auteur(auteur_naam) VALUES ('Boer Jos');
INSERT INTO Auteur(auteur_naam) VALUES ('Boer Frank');

INSERT INTO Auteur(auteur_naam) VALUES ('Jonas');
INSERT INTO Auteur(auteur_naam) VALUES ('Jerre');
INSERT INTO Auteur(auteur_naam) VALUES ('Dean');
INSERT INTO Auteur(auteur_naam) VALUES ('Maarten');

INSERT INTO Boer(auteur_id, boer_adres) VALUES (1,'Wortelstraat 80');
INSERT INTO Boer(auteur_id, boer_adres) VALUES (2,'Plattelandweg 7');
INSERT INTO Boer(auteur_id, boer_adres) VALUES (3,'Graanlaan 981');

INSERT INTO Klant(auteur_id) VALUES (4);
INSERT INTO Klant(auteur_id) VALUES (5);
INSERT INTO Klant(auteur_id) VALUES (6);
INSERT INTO Klant(auteur_id) VALUES (7);

INSERT INTO Product(product_naam, product_soort) VALUES ('Aardappel', 'groenten');
INSERT INTO Product(product_naam, product_soort) VALUES ('Prei', 'groenten');
INSERT INTO Product(product_naam, product_soort) VALUES ('Broccoli', 'groenten');
INSERT INTO Product(product_naam, product_soort) VALUES ('Wortel', 'groenten');
INSERT INTO Product(product_naam, product_soort) VALUES ('Courgette', 'groenten');
INSERT INTO Product(product_naam, product_soort) VALUES ('Komkommer', 'groenten');

INSERT INTO Product(product_naam, product_soort) VALUES ('Schorseneren', 'groenten');
INSERT INTO Product(product_naam, product_soort) VALUES ('Pastinaak', 'groenten');
INSERT INTO Product(product_naam, product_soort) VALUES ('Aardpeer', 'groenten');
INSERT INTO Product(product_naam, product_soort) VALUES ('Koolraap', 'groenten');

INSERT INTO Product(product_naam, product_soort) VALUES ('Aarbei', 'fruit');
INSERT INTO Product(product_naam, product_soort) VALUES ('Banaan', 'fruit');
INSERT INTO Product(product_naam, product_soort) VALUES ('Appel', 'fruit');
INSERT INTO Product(product_naam, product_soort) VALUES ('Peer', 'fruit');
INSERT INTO Product(product_naam, product_soort) VALUES ('Mandarijn', 'fruit');
INSERT INTO Product(product_naam, product_soort) VALUES ('Kiwi', 'fruit');

INSERT INTO Product(product_naam, product_soort) VALUES ('Kipfilet', 'vlees');
INSERT INTO Product(product_naam, product_soort) VALUES ('Buikspek', 'vlees');
INSERT INTO Product(product_naam, product_soort) VALUES ('Biefstuk', 'vlees');
INSERT INTO Product(product_naam, product_soort) VALUES ('Braadworst', 'vlees');
INSERT INTO Product(product_naam, product_soort) VALUES ('Kipsaté', 'vlees');
INSERT INTO Product(product_naam, product_soort) VALUES ('Paardenfilet', 'vlees');
INSERT INTO Product(product_naam, product_soort) VALUES ('Lamskotelet', 'vlees');

INSERT INTO Product(product_naam, product_soort) VALUES ('Roos', 'bloemen');
INSERT INTO Product(product_naam, product_soort) VALUES ('Madeliefje', 'bloemen');
INSERT INTO Product(product_naam, product_soort) VALUES ('Anjer', 'bloemen');
INSERT INTO Product(product_naam, product_soort) VALUES ('Boterbloem', 'bloemen');
INSERT INTO Product(product_naam, product_soort) VALUES ('Hibiscus', 'bloemen');
INSERT INTO Product(product_naam, product_soort) VALUES ('Jasmijn', 'bloemen');

INSERT INTO Pakket(pakket_naam, pakket_aantalVolwassenen, pakket_aantalKinderen) VALUES ('Mediumpakket', 2, 0);
INSERT INTO Pakket(pakket_naam, pakket_aantalVolwassenen, pakket_aantalKinderen) VALUES ('Grootpakket', 2, 2);
INSERT INTO Pakket(pakket_naam, pakket_aantalVolwassenen, pakket_aantalKinderen) VALUES ('Familiepakket', 2, 4);

INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (1,1,1000);
INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (1,2,1250);
INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (1,3,1500);

INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (2,1,1200);
INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (2,2,1400);
INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (2,3,1600);

INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (3,1,800);
INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (3,2,900);
INSERT INTO Verkoopt(auteur_id, pakket_id, verkoopt_prijs) VALUES (3,3,1000);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (1,1,4,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (1,2,2,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (1,7,20,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (1,13,5,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (1,21,7,1);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (2,1,3,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (2,3,2,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (2,8,3,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (2,14,6,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (2,19,7,1);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (3,1,5,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (3,4,7,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (3,9,6,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (3,15,2,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (3,20,13,1);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (4,1,15,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (4,2,8,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (4,7,40,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (4,13,15,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (4,21,10,1);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (5,1,13,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (5,3,4,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (5,8,11,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (5,14,23,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (5,19,11,1);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (6,1,14,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (6,4,27,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (6,9,15,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (6,15,6,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (6,20,20,1);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (7,1,9,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (7,2,8,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (7,7,30,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (7,13,11,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (7,21,15,1);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (8,1,9,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (8,3,3,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (8,8,7,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (8,14,9,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (8,19,11,1);

INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (9,1,7,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (9,4,12,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (9,9,11,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (9,15,4,1);
INSERT INTO ZitIn(verkoopt_id, product_id, zitIn_hoeveelheid, zitIn_weeknr) VALUES (9,20,17,1);

INSERT INTO SchrijftIn(klant_id,verkoopt_id) VALUES (4,3);
INSERT INTO SchrijftIn(klant_id,verkoopt_id) VALUES (5,6);
INSERT INTO SchrijftIn(klant_id,verkoopt_id) VALUES (6,8);
INSERT INTO SchrijftIn(klant_id,verkoopt_id) VALUES (7,1);

INSERT INTO HaaltAf(klant_id,verkoopt_id, pakket_weeknr, pakket_afgehaald) VALUES (4,3,1,0);
INSERT INTO HaaltAf(klant_id,verkoopt_id, pakket_weeknr, pakket_afgehaald) VALUES (5,6,1,0);
INSERT INTO HaaltAf(klant_id,verkoopt_id, pakket_weeknr, pakket_afgehaald) VALUES (6,8,1,0);
INSERT INTO HaaltAf(klant_id,verkoopt_id, pakket_weeknr, pakket_afgehaald) VALUES (7,1,1,1);