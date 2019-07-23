CREATE TABLE employee (
employee_id NUMBER(10,0) NOT NULL AUTO_INCREMENT,
first_name VARCHAR2(255) DEFAULT NULL,
last_name VARCHAR2(255) DEFAULT NULL,
email_address VARCHAR(255),
PRIMARY KEY (employee_id));

DROP SEQUENCE hibernate_sequence;

CREATE SEQUENCE hibernate_sequence;