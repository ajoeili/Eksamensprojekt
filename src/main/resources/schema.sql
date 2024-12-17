CREATE TABLE IF NOT EXISTS PROJECTS
(
    PROJECT_ID  INT AUTO_INCREMENT PRIMARY KEY,
    NAME        VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255) NOT NULL,
    START_DATE  DATE         NOT NULL,
    END_DATE    DATE         NOT NULL,
    CONSTRAINT unique_project UNIQUE (NAME, DESCRIPTION, START_DATE, END_DATE)
    );

CREATE TABLE IF NOT EXISTS SUBPROJECTS
(
    SUBPROJECT_ID INT AUTO_INCREMENT PRIMARY KEY,
    NAME          VARCHAR(255) NOT NULL,
    DESCRIPTION   VARCHAR(255) NOT NULL,
    START_DATE    DATE         NOT NULL,
    END_DATE      DATE         NOT NULL,
    PROJECT_ID    INT          NOT NULL,
    FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS (PROJECT_ID),
    CONSTRAINT unique_subproject UNIQUE (NAME, DESCRIPTION, START_DATE, END_DATE, PROJECT_ID)
    );

CREATE TABLE IF NOT EXISTS EMPLOYEES
(
    EMPLOYEE_ID        INT AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME         VARCHAR(255) NOT NULL,
    LAST_NAME          VARCHAR(255) NOT NULL,
    EMAIL              VARCHAR(255) NOT NULL,
    PASSWORD           VARCHAR(255) NOT NULL,
    ROLE               VARCHAR(255) NOT NULL,
    IS_PROJECT_MANAGER BOOLEAN      NOT NULL,
    CONSTRAINT unique_employee UNIQUE (FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
    );

CREATE TABLE IF NOT EXISTS TASKS
(
    TASK_ID         INT AUTO_INCREMENT PRIMARY KEY,
    NAME            VARCHAR(255) NOT NULL,
    DESCRIPTION     VARCHAR(255) NOT NULL,
    ESTIMATED_HOURS INT          NOT NULL,
    PRIORITY        INT,
    DEADLINE        DATE         NOT NULL,
    IS_COMPLETED    BOOLEAN      NOT NULL DEFAULT FALSE,
    EMPLOYEE_ID     INT          NOT NULL,
    SUBPROJECT_ID   INT          NOT NULL,
    FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (EMPLOYEE_ID),
    FOREIGN KEY (SUBPROJECT_ID) REFERENCES SUBPROJECTS (SUBPROJECT_ID),
    CONSTRAINT unique_task UNIQUE (NAME, DESCRIPTION, ESTIMATED_HOURS, PRIORITY, DEADLINE, SUBPROJECT_ID)
    );

-- Junction table
CREATE TABLE IF NOT EXISTS PROJECT_EMPLOYEE
(
    PROJECT_ID  INT,
    EMPLOYEE_ID INT,
    FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS (PROJECT_ID),
    FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (EMPLOYEE_ID),
    PRIMARY KEY (PROJECT_ID, EMPLOYEE_ID)
    );


-- TEST DATA ----------------------------------------------------------------------------------------------------------------------


-- TEST PROJECTS

INSERT INTO PROJECTS (NAME, DESCRIPTION, START_DATE, END_DATE)
VALUES ('Project calculation tool',
        'Tool to calculation project data',
        '2024-11-09',
        '2025-05-04');

INSERT INTO PROJECTS (NAME, DESCRIPTION, START_DATE, END_DATE)
VALUES ('E-commerce Platform',
        'Platform for online shopping',
        '2024-10-01',
        '2025-04-01');

INSERT INTO PROJECTS (NAME, DESCRIPTION, START_DATE, END_DATE)
VALUES ('Employee Management System',
        'System to manage employee information and tasks',
        '2024-09-15',
        '2025-03-15');


-- TEST SUB-PROJECTS

INSERT INTO SUBPROJECTS(NAME, DESCRIPTION, START_DATE, END_DATE, PROJECT_ID)
SELECT 'Login',
       'Set up system to login employees',
       '2024-11-11',
       '2024-12-12',
       p.PROJECT_ID
FROM PROJECTS p
WHERE p.NAME = 'Project calculation tool';

INSERT INTO SUBPROJECTS(NAME, DESCRIPTION, START_DATE, END_DATE, PROJECT_ID)
SELECT 'Product Catalog',
       'Develop a product catalog for the platform',
       '2024-10-10',
       '2024-11-15',
       p.PROJECT_ID
FROM PROJECTS p
WHERE p.NAME = 'E-commerce Platform';

INSERT INTO SUBPROJECTS(NAME, DESCRIPTION, START_DATE, END_DATE, PROJECT_ID)
SELECT 'Payment Gateway Integration',
       'Integrate payment gateway for the platform',
       '2024-11-20',
       '2024-12-15',
       p.PROJECT_ID
FROM PROJECTS p
WHERE p.NAME = 'E-commerce Platform';

INSERT INTO SUBPROJECTS(NAME, DESCRIPTION, START_DATE, END_DATE, PROJECT_ID)
SELECT 'Employee Profiles',
       'Develop a feature to store employee profiles',
       '2024-09-20',
       '2024-10-15',
       p.PROJECT_ID
FROM PROJECTS p
WHERE p.NAME = 'Employee Management System';

INSERT INTO SUBPROJECTS(NAME, DESCRIPTION, START_DATE, END_DATE, PROJECT_ID)
SELECT 'Attendance Tracking',
       'Build an attendance tracking module',
       '2024-10-20',
       '2024-11-20',
       p.PROJECT_ID
FROM PROJECTS p
WHERE p.NAME = 'Employee Management System';

INSERT INTO SUBPROJECTS(NAME, DESCRIPTION, START_DATE, END_DATE, PROJECT_ID)
SELECT 'CSS Styling',
       'Create a styling sheet for the tool',
       '2024-11-19',
       '2024-12-01',
       p.PROJECT_ID
FROM PROJECTS p
WHERE p.NAME = 'Project calculation tool';

INSERT INTO SUBPROJECTS(NAME, DESCRIPTION, START_DATE, END_DATE, PROJECT_ID)
SELECT 'Time-calculation',
       'Create a method to calculate time information',
       '2024-12-11',
       '2025-01-12',
       p.PROJECT_ID
FROM PROJECTS p
WHERE p.NAME = 'Project calculation tool';

-- TEST EMPLOYEES

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Terese',
        'Jensen',
        'teje49@email.com',
        'password2',
        'Project Manager',
        true);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Jane',
        'Jonassen',
        'jajo40@email.com',
        'password3',
        'Back-end Developer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Poul',
        'Hansen',
        'poha27@email.com',
        'password4',
        'Designer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Simon',
        'Mortensen',
        'simo05@email.com',
        'password1',
        'Front-end Developer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Michael',
        'Christiansen',
        'mich22@email.com',
        'securepass1',
        'Database Administrator',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Laura',
        'Frederiksen',
        'lauf50@email.com',
        'securepass2',
        'Quality Assurance Engineer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Anders',
        'Olsen',
        'ando12@email.com',
        'securepass3',
        'DevOps Engineer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Emma',
        'Nielsen',
        'emniel56@email.com',
        'securepass4',
        'UI/UX Designer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Frederik',
        'Larsen',
        'frelar32@email.com',
        'securepass5',
        'Project Manager',
        true);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Sofie',
        'Jørgensen',
        'sojo14@email.com',
        'securepass6',
        'Full Stack Developer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Mikkel',
        'Poulsen',
        'mikp25@email.com',
        'securepass7',
        'Front-end Developer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Camilla',
        'Kristensen',
        'camk34@email.com',
        'securepass8',
        'Back-end Developer',
        false);

INSERT INTO EMPLOYEES(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE, IS_PROJECT_MANAGER)
VALUES ('Oliver',
        'Thomsen',
        'olth29@email.com',
        'securepass9',
        'Tester',
        false);

-- TEST TASKS

INSERT INTO TASKS (NAME, DESCRIPTION, ESTIMATED_HOURS, PRIORITY, DEADLINE, EMPLOYEE_ID, SUBPROJECT_ID)
SELECT 'Authentication',
       'Create APIs to authenticate employees',
       3,
       1,
       '2024-11-20',
       e.EMPLOYEE_ID,
       s.SUBPROJECT_ID
FROM EMPLOYEES e,
     SUBPROJECTS s
WHERE e.FIRST_NAME = 'Jane'
  AND e.LAST_NAME = 'Jonassen'
  AND s.NAME = 'Login';

INSERT INTO TASKS(NAME, DESCRIPTION, ESTIMATED_HOURS, PRIORITY, DEADLINE, EMPLOYEE_ID, SUBPROJECT_ID)
SELECT 'Test login',
       'Set up tests to check the login process',
       5,
       2,
       '2025-01-11',
       e.EMPLOYEE_ID,
       s.SUBPROJECT_ID
FROM EMPLOYEES e,
     SUBPROJECTS s
WHERE e.FIRST_NAME = 'Jane'
  AND e.LAST_NAME = 'Jonassen'
  AND s.NAME = 'Login';

INSERT INTO TASKS(NAME, DESCRIPTION, ESTIMATED_HOURS, PRIORITY, DEADLINE, EMPLOYEE_ID, SUBPROJECT_ID)
SELECT 'Design login page',
       'Design login page with company logo and colors',
       5,
       2,
       '2024-12-20',
       e.EMPLOYEE_ID,
       s.SUBPROJECT_ID
FROM EMPLOYEES e,
     SUBPROJECTS s
WHERE e.FIRST_NAME = 'Poul'
  AND e.LAST_NAME = 'Hansen'
  AND s.NAME = 'Login';

INSERT INTO TASKS(NAME, DESCRIPTION, ESTIMATED_HOURS, PRIORITY, DEADLINE, EMPLOYEE_ID, SUBPROJECT_ID)
SELECT 'HTML and styling for login page',
       'Set up login page HTML and CSS styling according to design',
       3,
       1,
       '2025-01-20',
       e.EMPLOYEE_ID,
       s.SUBPROJECT_ID
FROM EMPLOYEES e,
     SUBPROJECTS s
WHERE e.FIRST_NAME = 'Simon'
  AND e.LAST_NAME = 'Mortensen'
  AND s.NAME = 'Login';

INSERT INTO TASKS(NAME, DESCRIPTION, ESTIMATED_HOURS, PRIORITY, DEADLINE, EMPLOYEE_ID, SUBPROJECT_ID)
SELECT 'Design Product Catalog',
       'Create UI/UX design for the product catalog',
       8,
       2,
       '2024-11-01',
       e.EMPLOYEE_ID,
       s.SUBPROJECT_ID
FROM EMPLOYEES e,
     SUBPROJECTS s
WHERE e.FIRST_NAME = 'Terese'
  AND e.LAST_NAME = 'Jensen'
  AND s.NAME = 'Product Catalog';

-- TASKS FOR EMPLOYEE MANAGEMENT SYSTEM -> EMPLOYEE PROFILES
INSERT INTO TASKS(NAME, DESCRIPTION, ESTIMATED_HOURS, PRIORITY, DEADLINE, EMPLOYEE_ID, SUBPROJECT_ID)
SELECT 'Database Schema for Employee Profiles',
       'Design and implement database schema for employee profiles',
       5,
       1,
       '2024-10-05',
       e.EMPLOYEE_ID,
       s.SUBPROJECT_ID
FROM EMPLOYEES e,
     SUBPROJECTS s
WHERE e.FIRST_NAME = 'Terese'
  AND e.LAST_NAME = 'Jensen'
  AND s.NAME = 'Employee Profiles';

-- Assign Employee 1 to Project 1
INSERT INTO PROJECT_EMPLOYEE (PROJECT_ID, EMPLOYEE_ID)
VALUES (1, 1);

-- Assign Employee 2 to Project 1
INSERT INTO PROJECT_EMPLOYEE (PROJECT_ID, EMPLOYEE_ID)
VALUES (1, 2);

-- Assign Employee 3 to Project 1
INSERT INTO PROJECT_EMPLOYEE (PROJECT_ID, EMPLOYEE_ID)
VALUES (1, 3);

-- Assign Employee 4 to Project 1
INSERT INTO PROJECT_EMPLOYEE (PROJECT_ID, EMPLOYEE_ID)
VALUES (1, 4);

-- Assign employee 1 to Project 2
INSERT INTO PROJECT_EMPLOYEE (PROJECT_ID, EMPLOYEE_ID)
VALUES (2, 1);

-- Assign employee 1 to Project 3
INSERT INTO PROJECT_EMPLOYEE (PROJECT_ID, EMPLOYEE_ID)
VALUES (3, 1);