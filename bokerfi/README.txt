Bokerfi - Projekt Submission

1. Bauen und Starten der Anwendung (Docker):
   Die Anwendung ist vollständig containerisiert. Zum Starten muss lediglich Docker installiert sein.
   
   Befehl zum Starten (im Hauptverzeichnis):
   docker compose up --build -d

   Dieser Befehl baut das Backend (Spring Boot) und Frontend (CSS/HTML/JS) und startet alle benötigten Dienste.


2. Architektur & Services (Container Übersicht):
   Das System besteht aus folgenden Docker-Containern:
   - bokerfi-frontend: Webserver (Port 80), liefert die SPA aus und routet API-Calls an das Backend.
   - bokerfi-backend: Spring Boot Applikation (Port 8080), enthält die Business-Logik.
   - bokerfi-db: MariaDB Datenbank (Port 3306), persistente Datenspeicherung.
   - bokerfi-mailhog: SMTP-Server & Web-UI (Port 8025/1025), fängt E-Mails ab für Testzwecke.
   - sonarqube: Code-Qualitäts-Server (Port 9000), läuft als eigenständiger Container zur Analyse.

3. Zugriffs-URLs & Adressen:

   | Service | URL | Beschreibung |
   |---------|-----|--------------|
   | **Startseite (App)** | http://localhost | Zugang zur Anwendung (Login-Page) |
   | **MailHog (E-Mails)** | http://localhost:8025 | Web-Interface zum Lesen von "versendeten" System-Mails |
   | **SonarQube** | http://localhost:9000 | Code-Quality Dashboard (Login: admin/Amjad123@@@123) |

4. Anmeldedaten (Administrator):
   E-Mail: admin@test.de
   Passwort: password

   (Hinweis: Weitere Test-Accounts wie Manager/Employee sind ebenfalls in der DB vorangelegt, siehe VSWP-Vorgabe).

5. SonarQube Hinweis:
   Der SonarQube-Container (`sonarqube`) läuft separat parallel zur App (`app`/`bokerfi-*`). Er analysiert den Code, ist aber für die Laufzeit der Bokerfi-Anwendung selbst nicht technisch notwendig. Die Ergebnisse der Analyse ("Code Quality Note") sind auch in der CI-Pipeline des Gitlab-Projekts einsehbar.