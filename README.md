[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/gQyBcnrC)
# Web Technologien // begleitendes Projekt Sommersemester 2024
Zum Modul Web Technologien gibt es ein begleitendes Projekt. Im Rahmen dieses Projekts werden wir von Veranstaltung zu Veranstaltung ein Projekt sukzessive weiter entwickeln und uns im Rahmen der Veranstaltung den Fortschritt anschauen, Code Reviews machen und Entwicklungsschritte vorstellen und diskutieren.

Als organisatorischen Rahmen für das Projekt nutzen wir GitHub Classroom. Inhaltlich befassen wir uns mit einer Client-Server Anwendung mit deren Hilfe [Bilder mit Langzeitbelichtung](https://de.wikipedia.org/wiki/Langzeitbelichtung) sehr einfach nachgestellt werden können.

Warum ist das cool? Bilder mit Langzeitbelichtung sind gar nicht so einfach zu erstellen, vor allem, wenn man möglichst viel Kontrolle über das Endergebnis haben möchte. In unserem Ansatz, bildet ein Film den Ausgangspunkt. Diesen zerlegen wir in Einzelbilder und montieren die Einzelbilder mit verschiedenen Blendmodes zu einem Bild mit Langzeitbelichtungseffekt zusammen.

Dokumentieren Sie in diesem Beibootprojekt Ihre Entscheidungen gewissenhaft unter Zuhilfenahme von [Architectual Decision Records](https://adr.github.io) (ADR).

Hier ein paar ADR Beispiele aus dem letzten Semestern:
- https://github.com/mi-classroom/mi-web-technologien-beiboot-ss2022-Moosgloeckchen/tree/main/docs/decisions
- https://github.com/mi-classroom/mi-web-technologien-beiboot-ss2022-mweiershaeuser/tree/main/adr
- https://github.com/mi-classroom/mi-web-technologien-beiboot-ss2022-twobiers/tree/main/adr

Halten Sie die Anwendung, gerade in der Anfangsphase möglichst einfach, schlank und leichtgewichtig (KISS).

---

# Projektorganisation

Entwickler: Dennis Wäckerle

Reviewer: Methusshan Elankumaran

Der Workflow kann [hier](worrkflow.md) nachgelesen werden.
Der Aufwand für die einzelnen Features kann [hier](time.md) gefunden werden.

---

# Running the project

Es gibt zwei Möglichkeiten, um die Anwendung auszuführen: Docker und Lokal.

## Docker

Für das Ausführen wird [Docker](https://www.docker.com/) benötigt. Am einfachsten ist es [Docker Desktop](https://docs.docker.com/desktop/) zu installieren. Alternativ kann auch nur die [Docker Engine](https://docs.docker.com/engine/install/) und die [Docker Compose Plugin](https://docs.docker.com/compose/install/linux/) installiert werden, wenn ein Linux OS genutzt wird.

### Voraussetzungen



### Starten

Es ist am einfachsten die Anwendung per Docker Compose auszuführen. Dafür muss folgender Befehl, in der Project Root ausgeführt werden:

```
docker compose up -d
```

Das Frontend ist unter http://localhost:5173 erreichbar. Das Backend kann nur über http://localhost:8080 erreicht werden.

## Local

Alternativ kann die Anwendung lokal ausgeführt werden. Dafür muss das Frontend und Backend einzeln gestartet werden.

### Backend

#### Voraussetzung

Eine [Java 21 JDK](https://adoptium.net/temurin/releases/) ist nötig, um die Anwendung lokal auszuführen.

#### Starten

Um das Backend zu starten, muss im `backend` Ordner folgender Befehl ausgeführt werden:

```
./gradlew bootRun
```

Das Backend ist unter http://localhost:8080 zu erreichen.

### Frontend

#### Voraussetzungen

Für das Frontend wird [Node >= 18.13](https://nodejs.org/en/download/package-manager) und NPM benötigt.

#### Starten

Das Frontend wird über NPM gestartet. Im `frontend` Ordner müssen die Dependencies mit `npm install`installiert werden. Danach kann das Frontend mit `npm run dev` gestartet werden.

Das Frontend ist unter http://localhost:8080 zu erreichen.
