CREATE TABLE bag_mutaties_file
(
	id					NUMERIC(20)		PRIMARY KEY,
	date_from		DATE					NOT NULL,
	date_to			DATE					NOT NULL,
	content			BYTEA					NULL,
	status			NUMERIC(1)		NOT NULL DEFAULT 0,
	CONSTRAINT u_bag_mutaties_file UNIQUE (date_from)
);

CREATE TABLE bag_mutatie
(
	id														NUMERIC(20)		PRIMARY KEY,
	tijdstip_verwerking						TIMESTAMP			NOT NULL,
	volgnr_verwerking							NUMERIC(3)		NOT NULL,
	object_type										NUMERIC(1)		NOT NULL,
	mutatie_product								TEXT					NOT NULL,
	CONSTRAINT u_bag_mutatie UNIQUE (tijdstip_verwerking,volgnr_verwerking)
);

CREATE TABLE bag_woonplaats
(
	bag_woonplaats_id							NUMERIC(16)		NOT NULL,
	aanduiding_record_inactief		NUMERIC(1)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	woonplaats_naam								VARCHAR(80)		NOT NULL,
	woonplaats_geometrie					TEXT					NOT NULL,
	officieel											NUMERIC(1)		NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	einddatum_tijdvak_geldigheid	TIMESTAMP			NULL,	
	in_onderzoek									NUMERIC(1)		NOT NULL,
	bron_documentdatum						DATE					NULL,
	bron_documentnummer						VARCHAR(20)		NULL,
	woonplaats_status							NUMERIC(1)		NOT NULL,
	CONSTRAINT u_bag_woonplaats_id UNIQUE (bag_woonplaats_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie)
);

CREATE INDEX i_bag_woonplaats_id ON bag_woonplaats(bag_woonplaats_id); 
   
CREATE TABLE bag_openbare_ruimte
(
	bag_openbare_ruimte_id				NUMERIC(16)		NOT NULL,
	aanduiding_record_inactief		NUMERIC(1)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	openbare_ruimte_naam					VARCHAR(80)		NOT NULL,
	officieel											NUMERIC(1)		NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	einddatum_tijdvak_geldigheid	TIMESTAMP			NULL,	
	in_onderzoek									NUMERIC(1)		NOT NULL,
	openbare_ruimte_type					NUMERIC(1)		NOT NULL,
	bron_documentdatum						DATE					NULL,
	bron_documentnummer						VARCHAR(20)		NULL,
	openbareruimte_status					NUMERIC(1)		NOT NULL,
	bag_woonplaats_id							NUMERIC(4)		NOT NULL,
	verkorte_openbare_ruimte_naam	VARCHAR(80)		NULL,
	CONSTRAINT u_bag_openbare_ruimte_id UNIQUE (bag_openbare_ruimte_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie)
);

CREATE INDEX i_bag_openbare_ruimte_id ON bag_openbare_ruimte(bag_openbare_ruimte_id); 

CREATE TABLE bag_nummeraanduiding
(
	bag_nummeraanduiding_id				NUMERIC(16)		NOT NULL,
	aanduiding_record_inactief		NUMERIC(1)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	huisnummer										NUMERIC(5)		NOT NULL,
	officieel											NUMERIC(1)		NOT NULL,
	huisletter										VARCHAR(1)		NULL,
	huisnummertoevoeging					VARCHAR(4)		NULL,
	postcode											VARCHAR(6)		NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	einddatum_tijdvak_geldigheid	TIMESTAMP			NULL,	
	in_onderzoek									NUMERIC(1)		NOT NULL,
	type_adresseerbaar_object			NUMERIC(1)		NOT NULL,
	bron_documentdatum						DATE					NULL,
	bron_documentnummer						VARCHAR(20)		NULL,
	nummeraanduiding_status				NUMERIC(1)		NOT NULL,
	bag_openbare_ruimte_id				NUMERIC(16)		NOT NULL,
	bag_woonplaats_id							NUMERIC(4)		NULL,
	CONSTRAINT u_bag_nummeraanduiding_id UNIQUE (bag_nummeraanduiding_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie)
);

CREATE INDEX i_bag_nummeraanduiding_id ON bag_nummeraanduiding(bag_nummeraanduiding_id); 

CREATE TABLE bag_pand
(
	bag_pand_id										NUMERIC(16)		NOT NULL,
	aanduiding_record_inactief		NUMERIC(1)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	officieel											NUMERIC(1)		NOT NULL,
	pand_geometrie								TEXT					NOT NULL,
	bouwjaar											NUMERIC(4)		NOT NULL,
	pand_status										VARCHAR(64)		NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	einddatum_tijdvak_geldigheid	TIMESTAMP			NULL,	
	in_onderzoek									NUMERIC(1)		NOT NULL,
	bron_documentdatum						DATE					NULL,
	bron_documentnummer						VARCHAR(20)		NULL,
	CONSTRAINT u_bag_pand_id UNIQUE (bag_pand_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie)
);

CREATE INDEX i_bag_pand_id ON bag_pand(bag_pand_id); 

CREATE TABLE bag_verblijfsobject
(
	bag_verblijfsobject_id				NUMERIC(16)		NOT NULL,
	aanduiding_record_inactief		NUMERIC(1)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	officieel											NUMERIC(1)		NOT NULL,
	verblijfsobject_geometrie			TEXT					NOT NULL,
	oppervlakte_verblijfsobject		NUMERIC(6)		NOT NULL,
	verblijfsobject_status				NUMERIC(1)		NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	einddatum_tijdvak_geldigheid	TIMESTAMP			NULL,	
	in_onderzoek									NUMERIC(1)		NOT NULL,
	bron_documentdatum						DATE					NULL,
	bron_documentnummer						VARCHAR(20)		NULL,
	bag_nummeraanduiding_id				NUMERIC(16)		NOT NULL,
	CONSTRAINT u_bag_verblijfsobject_id UNIQUE (bag_verblijfsobject_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie)
);

CREATE INDEX i_bag_verblijfsobject_id ON bag_verblijfsobject(bag_verblijfsobject_id); 

CREATE TABLE bag_gebruiksdoel
(
	bag_verblijfsobject_id				NUMERIC(16)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	gebruiksdoel									NUMERIC(2)		NOT NULL,
	CONSTRAINT u_bag_gebruiksdoel_id UNIQUE (bag_verblijfsobject_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie,gebruiksdoel)
);

CREATE TABLE bag_gerelateerd_pand
(
	bag_verblijfsobject_id				NUMERIC(16)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	bag_pand_id										NUMERIC(16)		NOT NULL,
	CONSTRAINT u_bag_gerelateerd_pand_id UNIQUE (bag_verblijfsobject_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie,bag_pand_id)
);

CREATE TABLE bag_ligplaats
(
	bag_ligplaats_id							NUMERIC(16)		NOT NULL,
	aanduiding_record_inactief		NUMERIC(1)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	officieel											NUMERIC(1)		NOT NULL,
	ligplaats_status							NUMERIC(1)		NOT NULL,
	ligplaats_geometrie						TEXT					NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	einddatum_tijdvak_geldigheid	TIMESTAMP			NULL,	
	in_onderzoek									NUMERIC(1)		NOT NULL,
	bron_documentdatum						DATE					NULL,
	bron_documentnummer						VARCHAR(20)		NULL,
	bag_nummeraanduiding_id				NUMERIC(16)		NOT NULL,
	CONSTRAINT u_bag_ligplaats_id UNIQUE (bag_ligplaats_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie)
);

CREATE INDEX i_bag_ligplaats_id ON bag_ligplaats(bag_ligplaats_id); 

CREATE TABLE bag_standplaats
(
	bag_standplaats_id						NUMERIC(16)		NOT NULL,
	aanduiding_record_inactief		NUMERIC(1)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	officieel											NUMERIC(1)		NOT NULL,
	standplaats_status						NUMERIC(1)		NOT NULL,
	standplaats_geometrie					TEXT					NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	einddatum_tijdvak_geldigheid	TIMESTAMP			NULL,	
	in_onderzoek									NUMERIC(1)		NOT NULL,
	bron_documentdatum						DATE					NULL,
	bron_documentnummer						VARCHAR(20)		NULL,
	bag_nummeraanduiding_id				NUMERIC(16)		NOT NULL,
	CONSTRAINT u_bag_standplaats_id UNIQUE (bag_standplaats_id,begindatum_tijdvak_geldigheid,aanduiding_record_correctie)
);

CREATE INDEX i_bag_standplaats_id ON bag_standplaats(bag_standplaats_id); 

CREATE TABLE bag_neven_adres
(
	bag_object_id									NUMERIC(16)		NOT NULL,
	bag_object_type								NUMERIC(1)		NOT NULL,
	aanduiding_record_correctie		NUMERIC(9)		NOT NULL,
	begindatum_tijdvak_geldigheid	TIMESTAMP			NOT NULL,	
	bag_nummeraanduiding_id				NUMERIC(16)		NOT NULL,
	CONSTRAINT u_bag_neven_adres_id UNIQUE (bag_object_id,bag_object_type,begindatum_tijdvak_geldigheid,aanduiding_record_correctie,bag_nummeraanduiding_id)
);
