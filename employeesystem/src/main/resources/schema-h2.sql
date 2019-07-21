CREATE TABLE employee (
employee_id NUMBER(10,0) NOT NULL AUTO_INCREMENT,
first_name VARCHAR2(255) DEFAULT NULL,
last_name VARCHAR2(255) DEFAULT NULL,
email_address VARCHAR(255),
PRIMARY KEY (employee_id));

INSERT INTO employee (first_name, last_name, email_address) VALUES ('Dennis', 'Kalaygian', 'dennis@gmail.com');
INSERT INTO employee (first_name, last_name, email_address) VALUES ('Jeremy', 'Gencavage', 'jeremy@gmail.com');
INSERT INTO employee (first_name, last_name, email_address) VALUES ('Lynn', 'Johnson', 'lynn@gmail.com');
INSERT INTO employee (first_name, last_name, email_address) VALUES ('K', 'T', 'KT.gmail.com');

DROP SEQUENCE hibernate_sequence;

CREATE SEQUENCE hibernate_sequence;