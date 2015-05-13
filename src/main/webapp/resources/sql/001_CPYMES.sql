CREATE USER 'cpymes'@'localhost' IDENTIFIED BY 'Claro2015*';
GRANT ALL ON cpymes.* TO 'cpymes'@'localhost';

CREATE DATABASE IF NOT EXISTS cpymes;

mysql --user=cpymes --password=Claro2015* cpymes

CREATE TABLE cpymes.alarm_pymes (
  id_alarm_pymes BIGINT(20) NOT NULL,
  descripcion VARCHAR(300) NOT NULL,
  estado VARCHAR(1) NOT NULL,
  PRIMARY KEY (id_alarm_pymes));
  
ALTER TABLE cpymes.alarm_pymes 
CHANGE COLUMN id_alarm_pymes id_alarm_pymes BIGINT(20) NOT NULL AUTO_INCREMENT ;
