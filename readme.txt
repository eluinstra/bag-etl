================
= Build Project
================
mvn clean package
mvn clean compile assembly:single

===============
= BAG Database
===============
Supported databases:
- Oracle -> bag-etl-xxx-src.zip/resources/database/master/oracle/*.sql
- Postgres -> bag-etl-xxx-src.zip/resources/database/master/postgres/*.sql

====================
= BAG Configuration
====================
example override properties file: bag-etl-xxx-src.zip/resources/bag.properties
or see below

edit and copy override properties file to:
<execute_dir>/bag.properties

override properties file bag.properties:
#-------------------------------------------------------------------------------
# Override properties
#-------------------------------------------------------------------------------

job.importExtract.maxThreads=8

bagLeveringInfo.klantnummer=9990000000
bagLeveringInfo.gebiedType=NEDERLAND
bagLeveringInfo.gegevensvariant=LEVENSCYCLUS
bagLeveringInfo.formaat=XML
bagLeveringInfo.productversie=02

bagLeveringInfo.mutatie.productcode=DNLDLXAM02
bagLeveringInfo.mutatie.producttype=ABONNEMENT_MUTATIE

bagLeveringInfo.extract.productcode=DNLDLXEE02
bagLeveringInfo.extract.producttype=EENMALIG_EXTRACT

# Datastore
bag.jdbc.driverClassName=oracle.jdbc.OracleDriver
bag.jdbc.url=jdbc:oracle:thin:@localhost:1521:bag
bag.jdbc.username=bag
bag.jdbc.password=bag
bag.pool.preferredTestQuery=select 1 from dual

#bag.jdbc.driverClassName=org.postgresql.Driver
#bag.jdbc.url=jdbc:postgresql://localhost:5432/bag
#bag.jdbc.username=bag
#bag.jdbc.password=bag
#bag.pool.preferredTestQuery=select 1

==================
= Import BAG File
==================
Usage: nl.ordina.bag.etl.Import <filename> [<loglevel>]
Description: Takes BAGFile as argument. Depending on the file type it executes BAG Extract or Mutaties Import.

> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.Import i:/BAGExtract/DNLDLXEE02-9990000000-999000006-01042011.zip debug
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.Import i:/BAGMutaties/DNLDLXAM02-9990000000-999000000-01042011-02042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.Import i:/BAGMutaties/DNLDLXAM02-9990000000-999000001-02042011-03042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.Import i:/BAGMutaties/DNLDLXAM02-9990000000-999000002-03042011-04042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.Import i:/BAGMutaties/DNLDLXAM02-9990000000-999000003-04042011-05042011.zip

=====================
= Import BAG Extract
=====================
Usage: nl.ordina.bag.etl.ImportExtract <filename>
Description: Takes BAGExtractFile as argument. Parses, validates and imports BAGExtractFile into bag tables and writes initial record in table bag_mutaties_file with date_from = 01-01-1970 and status = 1.

> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportExtract i:/BAGExtract/DNLDLXEE02-9990000000-999000006-01042011.zip

======================
= Import BAG Mutaties
======================
Usage: nl.ordina.bag.etl.ImportMutaties <filename> [<filename>]
Description: Takes BAGMutatiesFile(s) as argument. Parses, validates and imports BAGMutatiesFiles into table bag_mutaties_file. Then while available:
	- takes next BAGMutatiesFile from table bag_mutaties_file with status = 0
	- imports BAGMutatiesFile into table bag_mutatie
	- exports BAGMutaties from table bag_mutatie to bag tables

> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutaties i:/BAGMutaties/DNLDLXAM02-9990000000-999000000-01042011-02042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutaties i:/BAGMutaties/DNLDLXAM02-9990000000-999000001-02042011-03042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutaties i:/BAGMutaties/DNLDLXAM02-9990000000-999000002-03042011-04042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutaties i:/BAGMutaties/DNLDLXAM02-9990000000-999000003-04042011-05042011.zip

> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutaties i:/BAGMutaties/DNLDLXAM02-9990000000-999000000-01042011-02042011.zip i:/BAGMutaties/DNLDLXAM02-9990000000-999000001-02042011-03042011.zip i:/BAGMutaties/DNLDLXAM02-9990000000-999000002-03042011-04042011.zip i:/BAGMutaties/DNLDLXAM02-9990000000-999000003-04042011-05042011.zip

=============================
= Import BAG Mutaties Direct
=============================
Usage: nl.ordina.bag.etl.ImportMutatiesDirect <filename> [<filename>]
Description: Takes BAGMutatiesFile(s) as argument. Parses and imports each BAGMutatiesFile into table bag_mutatie and export BAGMutaties from table bag_mutatie to bag tables.

> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutatiesDirect i:/BAGMutaties/DNLDLXAM02-9990000000-999000000-01042011-02042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutatiesDirect i:/BAGMutaties/DNLDLXAM02-9990000000-999000001-02042011-03042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutatiesDirect i:/BAGMutaties/DNLDLXAM02-9990000000-999000002-03042011-04042011.zip
> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutatiesDirect i:/BAGMutaties/DNLDLXAM02-9990000000-999000003-04042011-05042011.zip

> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.ImportMutatiesDirect i:/BAGMutaties/DNLDLXAM02-9990000000-999000000-01042011-02042011.zip i:/BAGMutaties/DNLDLXAM02-9990000000-999000001-02042011-03042011.zip i:/BAGMutaties/DNLDLXAM02-9990000000-999000002-03042011-04042011.zip i:/BAGMutaties/DNLDLXAM02-9990000000-999000003-04042011-05042011.zip

===========================
= Test Database Connection
===========================
Usage: nl.ordina.bag.etl.TestDbConnection
Description: tests database connection

> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.TestDbConnection

==================
= Format XML File
==================
Usage: nl.ordina.bag.etl.XMLFormatter <filename>
Description: writes xml file in readable format to new file

> java -cp bag-etl-mail-1.0.0-jar-with-dependencies.jar nl.ordina.bag.etl.XMLFormatter file.xml
