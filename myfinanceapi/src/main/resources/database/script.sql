-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema my_finance_api
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `my_finance_api` ;

-- -----------------------------------------------------
-- Schema my_finance_api
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `my_finance_api` DEFAULT CHARACTER SET utf8 ;
USE `my_finance_api` ;

-- -----------------------------------------------------
-- Table `my_finance_api`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_finance_api`.`category` ;

CREATE TABLE IF NOT EXISTS `my_finance_api`.`category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) CHARACTER SET 'utf8' NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `my_finance_api`.`transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_finance_api`.`transaction` ;

CREATE TABLE IF NOT EXISTS `my_finance_api`.`transaction` (
  `transaction_id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) CHARACTER SET 'utf8' NULL,
  `value` DECIMAL(7,2) NOT NULL,
  `type` ENUM('EXPENSE', 'REVENUE') CHARACTER SET 'utf8' NOT NULL,
  `due_date` DATE NULL,
  `category_id` INT NULL,
  PRIMARY KEY (`transaction_id`),
  INDEX `fk_transaction_category_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_transaction_category`
    FOREIGN KEY (`category_id`)
    REFERENCES `my_finance_api`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
