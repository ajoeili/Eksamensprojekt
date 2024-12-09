CREATE TABLE IF NOT EXISTS PROJECTS
(
    PROJECT_ID  INT AUTO_INCREMENT PRIMARY KEY,
    NAME        VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255) NOT NULL,
    START_DATE  DATE         NOT NULL,
    END_DATE    DATE         NOT NULL
    );

CREATE TABLE IF NOT EXISTS SUBPROJECTS
(
    SUBPROJECT_ID INT AUTO_INCREMENT PRIMARY KEY,
    NAME          VARCHAR(255) NOT NULL,
    DESCRIPTION   VARCHAR(255) NOT NULL,
    START_DATE    DATE         NOT NULL,
    END_DATE      DATE         NOT NULL,
    PROJECT_ID    INT,
    FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS (PROJECT_ID)
    );

CREATE TABLE IF NOT EXISTS EMPLOYEES
(
    EMPLOYEE_ID        INT AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME         VARCHAR(255) NOT NULL,
    LAST_NAME          VARCHAR(255) NOT NULL,
    EMAIL              VARCHAR(255) NOT NULL,
    PASSWORD           VARCHAR(255) NOT NULL,
    ROLE               VARCHAR(255) NOT NULL,
    IS_PROJECT_MANAGER BOOLEAN
    );

CREATE TABLE IF NOT EXISTS TASKS
(
    TASK_ID         INT AUTO_INCREMENT PRIMARY KEY,
    NAME            VARCHAR(255) NOT NULL,
    DESCRIPTION     VARCHAR(255) NOT NULL,
    ESTIMATED_HOURS INT          NOT NULL,
    PRIORITY        INT,
    DEADLINE        DATE         NOT NULL,
    EMPLOYEE_ID     INT,
    SUBPROJECT_ID   INT,
    FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (EMPLOYEE_ID),
    FOREIGN KEY (SUBPROJECT_ID) REFERENCES SUBPROJECTS (SUBPROJECT_ID)
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


-- TEST PROJECT

INSERT INTO PROJECTS (NAME, DESCRIPTION, START_DATE, END_DATE)
VALUES ('Project calculation tool',
        'Tool to calculation project data',
        '2024-11-09',
        '2025-05-04');


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


