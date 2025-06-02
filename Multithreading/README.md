# 🎨 JavaFX Image Editor
Prosty edytor obrazów stworzony w JavaFX, umożliwiający podstawowe operacje graficzne na obrazach.

# 🛠 Technologie
- Java 21

- JavaFX 21.0.2 (controls, fxml, swing)

- Maven – zarządzanie zależnościami i budowanie projektu

# 📦 Funkcjonalności
✅ Wczytywanie obrazów z dysku \
✅ Wyświetlanie oryginalnego i przetworzonego obrazu \
✅ Skalowanie obrazu\
✅ Obracanie w lewo / prawo o 90° \
✅ Zastosowanie wybranej operacji z listy rozwijanej (ComboBox) \
✅ Zapisywanie przetworzonego obrazu na dysku \
✅ Podgląd logo aplikacji\
✅ Prosty system logowania operacji do pliku log.txt 

# 📁 Struktura projektu
```
src/
└── main/
    ├── java/
    │   └── org/example/multithreading/
    │       ├── ApplicationWindow.java      // Główna klasa uruchamiająca
    │       ├── Controller.java             // Kontroler GUI
    │       ├── ImageProcessor.java         // Operacje na obrazach
    │       └── LoggerService.java          // Logowanie zdarzeń
    └── resources/
        ├── main-view.fxml                  // Definicja interfejsu graficznego
        └── pwrlogo.jpg                     // Logo wyświetlane w UI
```
# 🚀 Jak uruchomić
Upewnij się, że masz zainstalowaną Javę 21 i Maven.

Zbuduj projekt:
```
mvn clean install
```
Uruchom aplikację (np. z IDE lub z terminala):

```
mvn javafx:run
```


# 📷 Przykład działania
Po uruchomieniu można wczytać obraz z dysku.

Obraz zostanie pokazany po lewej, a wszelkie operacje przetworzą go i pokażą po prawej stronie.

Można zapisać wynikowy obraz.

Wszystkie akcje są logowane w pliku log.txt.
