# 📚 Bibliothèque Desktop App

A **Java-based desktop application** designed to manage a library system efficiently.  
It allows librarians to manage **Adherents (customers)**, **Bibliothécaires (workers)**, and **Books**, with full **real-time borrowing and return functionalities**.

---

## 🚀 Features

### 👥 User Management
- Add, edit, and remove **Adherents (customers)**.
- Manage **Bibliothécaires (workers)** with login access.

### 📘 Book Management
- Add new books with details such as title, author, and category.
- Update or delete existing book records.
- Track book availability in real time.

### 🔁 Borrow & Return System
- Record when a book is **borrowed** or **returned**.
- Automatically update book availability.
- Display all current and past transactions.
- Prevent double borrowing of the same book.

### 💾 Database Integration
- Connected to a **MySQL** database for persistent data storage.
- DAO (Data Access Object) pattern ensures clean separation between logic and data layers.

### 🖥️ User Interface
- Built using **JavaFX** for a modern and responsive UI.
- Includes styled tables, tabs, and modals for an intuitive user experience.
- Custom CSS themes for a professional look.

---

## 🏗️ Architecture


<img width="1247" height="907" alt="image" src="https://github.com/user-attachments/assets/71128e81-eb79-41e9-b9c8-5d0a4bd01ddb" />




<img width="1228" height="865" alt="image" src="https://github.com/user-attachments/assets/92b64fae-5a17-4118-993b-e0687565551d" />




<img width="1235" height="862" alt="image" src="https://github.com/user-attachments/assets/ae7a4aa7-677c-4c20-b59e-42d2983698f4" />



## 🧱 Example Database Script

```sql

CREATE DATABASE IF NOT EXISTS bibliotheque_db;
USE bibliotheque_db;


CREATE TABLE Personne (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    adresse VARCHAR(255)
);

CREATE TABLE Bibliothecaire (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricule INT UNIQUE NOT NULL,
    personne_id INT,
    FOREIGN KEY (personne_id) REFERENCES Personne(id)
);


CREATE TABLE Adherent (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricule INT UNIQUE NOT NULL,
    date_ins DATE NOT NULL,
    nb_emprunts_encours INT DEFAULT 0,
    personne_id INT,
    FOREIGN KEY (personne_id) REFERENCES Personne(id)
);

CREATE TABLE Livre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT UNIQUE NOT NULL,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(100) NOT NULL,
    annee_publication INT,
    dispo BOOLEAN DEFAULT TRUE
);


CREATE TABLE Emprunt (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_emprunt DATE NOT NULL,
    date_restitution_prevue DATE NOT NULL,
    date_restitution_effective DATE,
    date_prolongation DATE,
    livre_id INT,
    adherent_id INT,
    bibliothecaire_id INT,
    FOREIGN KEY (livre_id) REFERENCES Livre(id),
    FOREIGN KEY (adherent_id) REFERENCES Adherent(id),
    FOREIGN KEY (bibliothecaire_id) REFERENCES Bibliothecaire(id)
);







