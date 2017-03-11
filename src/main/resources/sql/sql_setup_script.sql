CREATE schema IF NOT EXISTS tvf;

CREATE TABLE IF NOT EXISTS `tvf`.`users` (
  `idusers` INT NOT NULL AUTO_INCREMENT,
  `rootadmin` BIT(1) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `authhash` CHAR(72) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `fullname` VARCHAR(45) NOT NULL,
  `banned`  BIT(1) NOT NULL,
  PRIMARY KEY (`idusers`),
  UNIQUE INDEX `idusers_UNIQUE` (`idusers` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC));


CREATE TABLE IF NOT EXISTS  `tvf`.`groups` (
  `idgroups` INT NOT NULL AUTO_INCREMENT,
  `groupname` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idgroups`),
  UNIQUE INDEX `idgroups_UNIQUE` (`idgroups` ASC),
  UNIQUE INDEX `groupname_UNIQUE` (`groupname` ASC));


CREATE TABLE IF NOT EXISTS `tvf`.`user2group` (
  `idusers` INT NOT NULL,
  `idgroups` INT NOT NULL);

CREATE TABLE IF NOT EXISTS `tvf`.`routeentities` (
  `idrouteentities` INT NOT NULL,
  `parentid` INT,
  `name` VARCHAR(45) NOT NULL,
  `type` INT NOT NULL,
  `pageid` INT,
  PRIMARY KEY (`idrouteentities`),
  UNIQUE INDEX `idrouteentities_UNIQUE` (`idrouteenties` ASC));
);
