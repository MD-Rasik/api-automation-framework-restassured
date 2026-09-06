# 🚀 GoREST API Automation Framework

A production-style API test automation framework built with **Rest Assured + Java**, validating the [GoREST](https://gorest.co.in/) public API — covering full CRUD, token-based auth, data-driven testing, JSON schema validation, and CI-integrated Extent Reports.

<p align="left">
  <img src="https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/RestAssured-5.x-25A162?logo=cucumber&logoColor=white" />
  <img src="https://img.shields.io/badge/TestNG-Testing-orange?logo=testng&logoColor=white" />
  <img src="https://img.shields.io/badge/ExtentReports-Reporting-blueviolet" />
  <img src="https://img.shields.io/badge/GitHub_Actions-CI%2FCD-2088FF?logo=githubactions&logoColor=white" />
  <img src="https://img.shields.io/badge/Lombok-Boilerplate--free-red?logo=lombok&logoColor=white" />
</p>

<p align="left">
  <img src="https://github.com/YOUR_USERNAME/YOUR_REPO/actions/workflows/tests.yml/badge.svg" alt="Build Status" />
  <img src="https://img.shields.io/github/last-commit/YOUR_USERNAME/YOUR_REPO" />
  <img src="https://img.shields.io/github/license/YOUR_USERNAME/YOUR_REPO" />
</p>

---

## 📖 Table of Contents
- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Framework Architecture](#-framework-architecture)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Running the Tests](#-running-the-tests)
- [Test Reporting](#-test-reporting)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Sample Test](#-sample-test)
- [Key Highlights](#-key-highlights)
- [Future Enhancements](#-future-enhancements)
- [Author](#-author)

---

## 🔍 Overview

This project is an end-to-end API test automation framework built against the **GoREST** API, designed the way a real QA/SDET framework would be structured at a product company — not a single flat test class, but a layered, config-driven, CI-integrated suite.

It validates:
- ✅ Full CRUD operations (Users, Posts, Comments)
- ✅ Token-based authentication (Bearer token flow)
- ✅ Response schema validation
- ✅ Data-driven scenarios via JSON/Excel
- ✅ Environment-based configuration (dev/qa switch-ready)
- ✅ Automated HTML reporting with Extent Reports
- ✅ Continuous Integration via GitHub Actions on every push

---

## 🛠 Tech Stack

| Category | Tool |
|---|---|
| ![Java](https://img.shields.io/badge/-Java-orange?logo=openjdk&logoColor=white) | Core language |
| ![Maven](https://img.shields.io/badge/-Maven-C71A36?logo=apachemaven&logoColor=white) | Build & dependency management |
| ![RestAssured](https://img.shields.io/badge/-Rest%20Assured-25A162) | API test automation |
| ![TestNG](https://img.shields.io/badge/-TestNG-orange) | Test execution & orchestration |
| ![Jackson](https://img.shields.io/badge/-Jackson-000000) | JSON (de)serialization |
| ![Lombok](https://img.shields.io/badge/-Lombok-red) | Boilerplate-free POJOs |
| ![ExtentReports](https://img.shields.io/badge/-Extent%20Reports-blueviolet) | Rich HTML test reports |
| ![GitHubActions](https://img.shields.io/badge/-GitHub%20Actions-2088FF?logo=githubactions&logoColor=white) | CI/CD pipeline |
| ![Log4j](https://img.shields.io/badge/-Log4j2-D22128) | Structured logging |

---

## 🏗 Framework Architecture

This framework follows the **Service Object Model** — raw Rest Assured calls are wrapped inside dedicated service classes, keeping `@Test` methods thin, readable, and focused purely on assertions.

```
Test Class  →  Service Object  →  Rest Assured Call  →  GoREST API
   (what)         (how)              (execution)
```

- **`base/`** — shared `RequestSpecification`/`ResponseSpecification`, TestNG hooks
- **`pojo/`** — request/response models (Lombok-powered)
- **`serviceobjects/`** — one class per resource (`UserService`, `PostService`) exposing methods like `createUser()`, `getUserById()`, `deleteUser()`
- **`utils/`** — `ConfigReader`, `JsonUtils`, `ExcelUtils`, token manager
- **`tests/`** — actual test classes, one-line-readable assertions

---

## 📁 Project Structure

```
gorest-api-framework/
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   ├── pojo/
│   │   ├── serviceobjects/
│   │   └── utils/
│   └── test/
│       ├── java/tests/
│       └── resources/
│           ├── config.properties
│           ├── testdata/
│           └── schemas/
├── test-output/                  # TestNG default reports
├── reports/                      # Extent HTML reports (generated)
├── .github/workflows/tests.yml   # CI pipeline
├── pom.xml
└── README.md
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- A free [GoREST API token](https://gorest.co.in/) (sign in with GitHub/Google to generate one)

### Setup
```bash
# 1. Clone the repo
git clone https://github.com/MD-Rasik/api-automation-framework-restassured.git
cd YOUR_REPO

# 2. Add your GoREST token to src/test/resources/config.properties
echo "api.token=YOUR_GOREST_TOKEN" >> src/test/resources/config.properties

# 3. Install dependencies
mvn clean install -DskipTests
```

> ⚠️ Note: config.properties containing your token is git-ignored — never commit real tokens.

---

## ▶️ Running the Tests

```bash
# Run the full suite
mvn clean test

# Run a specific TestNG suite
mvn test -DsuiteXmlFile=testng.xml

# Run in parallel (configured in testng.xml)
mvn test -Dsurefire.suiteXmlFiles=testng-parallel.xml
```

---

## 📊 Test Reporting

This framework generates a rich **Extent Report** after every run, with pass/fail status, request/response logs per test, and execution timestamps.

📍 Report location after a run: `reports/ExtentReport_<timestamp>.html`

<p align="left">
  <img src="https://img.shields.io/badge/Report-Auto--generated-brightgreen" />
</p>

*(Add a screenshot of your Extent Report dashboard here once available — drag the image into the repo and reference it like `![Extent Report](reports/screenshot.png)`)*

---

## 🔄 CI/CD Pipeline

Every push and pull request to `main` triggers an automated run via **GitHub Actions**:

```yaml
# .github/workflows/tests.yml (summary)
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
      - run: mvn clean test
      - uses: actions/upload-artifact@v4
        with:
          name: extent-report
          path: reports/
```

The Extent Report is uploaded as a workflow artifact on every run — downloadable directly from the Actions tab, even without cloning the repo.

---

## 🧪 Sample Test

```java
@Test
public void testCreateAndFetchUser() {
    User newUser = User.builder()
            .name("Vikram Singh")
            .email("vikram.singh" + System.currentTimeMillis() + "@example.com")
            .gender("male")
            .status("active")
            .build();

    Response createResponse = userService.createUser(newUser);
    createResponse.then().statusCode(201);

    int userId = createResponse.jsonPath().getInt("id");

    userService.getUserById(userId)
            .then()
            .statusCode(200)
            .body("email", equalTo(newUser.getEmail()));

    userService.deleteUser(userId).then().statusCode(204);
}
```

---

## ⭐ Key Highlights

- 🔁 **Self-cleaning tests** — every CRUD test creates and tears down its own data, keeping runs idempotent
- 🔐 **Token-based auth handling** — centralized in a request spec, no repeated header code
- 📦 **Data-driven** — TestNG `@DataProvider` fed from JSON/Excel, no hardcoded payloads
- 🧩 **Schema validation** — every response validated against a JSON schema, catching contract breaks early
- 🌍 **Environment-ready** — swap base URLs via `config.properties`, no code changes needed

---

## 🔮 Future Enhancements

- [ ] Integrate Allure Reports as an alternative to Extent
- [ ] Add Cucumber (BDD) layer on top of the service objects
- [ ] Dockerize the test suite for portable CI execution
- [ ] Add contract testing with Pact

---

## 👤 Author

**Mohammed Rasik**
📧 mdrasikqaengineer@zohomail.in · 🔗 [LinkedIn](www.linkedin.com/in/mohammed-rasik)· 🐙 [GitHub](https://github.com/MD-Rasik)

---

<p align="center"><i>Built as part of a hands-on Rest Assured mastery journey — from Postman fundamentals to a CI-integrated API automation framework.</i></p>
