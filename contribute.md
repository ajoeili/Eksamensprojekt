# Contributing to Calculation-tool

Thank you for considering contributing to this project! 
Whether it's improving documentation, fixing bugs, or adding new features, your contributions are really appreciated.

This guide will help you get started with contributing to this project.

## Table of Contents

1. [How to Contribute](#how-to-contribute)
2. [Reporting Issues](#reporting-issues)
3. [Submitting a Pull Request](#submitting-a-pull-request)
4. [Development Setup](#development-setup)
5. [Coding Guidelines](#coding-guidelines)
6. [Test Guidelines](#test-guidelines)
7. [Code of Conduct](#code-of-conduct)
8. [License](#license)

---

## How to Contribute

There are several ways you can contribute to the **Calculation-tool** project:

1. **Report Errors**: If you find any issues or errors, please open an issue and describe the problem.
2. **Fix Errors**: You can look for open issues, fix errors, and submit a pull request.
3. **Add New Features**: If you have an idea for a new feature or expansion, feel free to open an issue or submit a pull request.
4. **Improve Documentation**: I appreciate improvements to my documentation, whether it's improving the README or updating comments.

---

## Reporting Issues

If you come across errors or have a suggestion for a new feature, please **open an issue**. To make it easier for me to handle and fix issues, please ensure your issue includes:

- A **clear description** of the issue.
- **Steps to reproduce** the issue (if applicable).
- Any **error messages** or **logs** related to the issue.
- Information about your **environment** (fx. Java version etc.).

---

## Submitting a Pull Request

To submit a pull request with your contribution, follow these steps:

- **Fork the repository**: Click the "Fork" button at the top of this repository to create a personal copy of the repository.
- **Create a branch**: Before making any changes, create a new branch:
   
   git checkout -b feature/your-feature-name

## Changes to the Program

- **Make your changes:** Add the feature or fix the bug. Be sure to follow the Coding Guidelines section below.
- **Write tests:** If you are adding a new feature or fixing a bug, please include tests where applicable (refer to the Test Guidelines below).
- **Commit your changes:** Commit your changes with a descriptive message:

  git commit -m "Add new feature or fix issue"

- **Push your changes:**

git push origin feature/your-feature-name

- **Create a pull request:** Once your changes are pushed to your fork, go to the GitHub repository and click the "New Pull Request" button. Make sure to describe the changes in the pull request, so others can understand what you're contributing.

## Development Setup

To get started with developing for this project, you'll need to set up your local environment. Here are the steps:

**Prerequisites:**

Ensure you have the following installed:

- Java 21 (Azul JDK): Download from Azul JDK Downloads.
- Maven: Install Maven from Maven's official site.
- MySQL (or use H2 for local development): Install MySQL from MySQL's official site, or use H2 for testing.

Check out the README.md for specific versions.

## Steps to Set Up Locally

- **Clone the repository:**

git clone https://github.com/ajoeili/Eksamensprojekt
cd main

**Build the project using Maven:**

mvn clean install

**Run the application:**

mvn spring-boot:run

**Access the application** 

At http://localhost:8080/calculation-tool/login
Find test login in the README.md

## Coding Guidelines

To ensure consistency and maintainability, please follow these coding guidelines:

- **Follow the existing code style:** If the project uses specific formatting, try to follow it for readability.
- **Use meaningful names:** Variables, methods, and classes should have descriptive names that reflect their purpose.
- **Comment your code:** Provide comments for complex logic and functions.
- **No hard-coded values:** Avoid hard-coding values. Instead, use configuration files or environment variables when needed.

## Test Guidelines

If you're adding a new feature or fixing an error, please include appropriate tests to ensure the changes are working as expected. 
This project uses JUnit Jupiter for unit testing and SpringBootTest + Mockito for integration tests.

- **Write unit tests:** Write unit tests for each component that can be tested independently.
- **Write integration tests:** If applicable, write tests that verify the interaction between different components.
- **Run tests before submitting a pull request:** Make sure all tests pass locally before submitting your pull request.

To run the tests locally:

mvn test

## Code of Conduct

By participating in this project, you agree to follow the code of conduct, which encourages respectful and constructive communication. 
I have no tolerance for any type of discriminatory behavior from collaborators, and be particularly mindful of man-splaining or condescending unsolicited advice.
Let's keep the community welcoming for everyone.

## Thanks to

Special credit and thank you to the online community of coders for assisting me with this project by posting your advice and experience on pages like Reddit, Stack Overflow and the likes.
Credit also to my teachers for guidance and advice throughout the process.
And credit to ChatGPT for helping me format and edit text files, for helping me debug a million times and for suggesting ways to improve my code (when it was not completely useless).
