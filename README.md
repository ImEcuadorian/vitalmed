
# 🏥 Clínica Vitalmed - Medical System in Java

[![Java](https://img.shields.io/badge/Java-24-blue.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-8.14-brightgreen.svg)](https://gradle.org/)
[![Lombok](https://img.shields.io/badge/Lombok-Required-orange.svg)](https://projectlombok.org/)
[![Architecture](https://img.shields.io/badge/Architecture-MVC-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Clínica Vitalmed** is a full Java-based desktop system designed for managing medical appointments, doctor schedules, patient records, and clinical histories. Built using a clean MVC architecture, it stores all data in `.txt` files and supports three types of users: Administrator, Doctor, and Patient.

---

## ✨ Main Features

### 👩‍💼 Administrator
- Secure login with fixed credentials
- Register doctors
- Assign weekly schedules (5 days × 4 shifts)
- Reset patient passwords
- View and manage users (CRUD)

### 👨‍⚕️ Doctor
- Login via email and ID
- View weekly appointments
- Access and update patient clinical history
- Record treatments, conditions, and medications

### 👨‍💻 Patient
- Register or login
- Book appointments via 3-step wizard
- Choose specialty, doctor, time slot
- Download appointment confirmation as PDF

---

## 🛠 Technologies Used

- ✅ Java 24
- ✅ Gradle 8.14
- ✅ Lombok
- ✅ JetBrains Annotations
- ✅ JUnit 5
- ✅ Custom JAR: `generic-library-1.0.0.jar`

---

## 🚀 Installation & Running

1. Clone the CRUDRepository or download the ZIP
2. Open in Eclipse or IntelliJ (with Gradle support)
3. Run:

```bash
./gradlew build
./gradlew run
```

4. Main entry point: `io.github.imecuadorian.vitalmed.Vitalmed`

---

## 📁 Project Structure

```
src/main/java/io/github/imecuadorian/vitalmed/
│
├── model/          # Domain entities
├── service/        # Business logic and interfaces
├── CRUDRepository/     # Text file-based persistence
├── controller/     # View-to-service interaction logic
├── util/           # Validators, constants, helpers
├── factory/        # Centralized service injection
├── view/           # GUI (Swing, built with WindowBuilder)
└── Vitalmed.java   # App entry point
```

---

## ✅ Validation Rules

- ✔ Password must have ≥ 8 characters, 1 uppercase, and 1 special symbol (`@`, `-`, `/`)
- ✔ Uses `Generic<T, S>` for flexible data pairing
- ✔ Regular expression validation (emails, IDs, passwords)
- ✔ Error handling and safe input filtering
- ✔ Data stored under `/data/*.txt`

---

## 🧪 Testing

Unit tests are implemented using JUnit 5. To run:

```bash
./gradlew test
```

---

## 📋 Requirements

- Java 21 or newer (Java 24 recommended)
- Gradle 8.14
- Lombok plugin installed in your IDE
- Place `generic-library-1.0.0.jar` in `libs/` folder

---

## 🎓 Academic Project

This application was developed for the **1st Partial Exam** in the subject **Programación Aplicada**.  
It fulfills all functional and non-functional requirements from the assignment statement.

---

## 👥 Team Members

- Mauricio
- Juan
- Josué
- Ronaldo
- Jairo

---

## 📄 License

This project is licensed under the terms of the [MIT License](LICENSE).  
You are free to use, modify, and distribute it for educational or personal purposes.
