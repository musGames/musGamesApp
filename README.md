# Guide: Klon og kør `musGamesApp` i Android Studio med emulator

Denne guide viser hvordan du kloner projektet fra GitHub og kører det i Android Studio med en Android emulator.

Repo:

https://github.com/musGames/musGamesApp.git

---

## 1. Klon projektet

Åbn terminal / PowerShell der hvor du vil have projektet liggende.

Kør:

```bash
git clone https://github.com/musGames/musGamesApp.git
```

Gå ind i projektmappen:

```bash
cd musGamesApp
```

---

## 2. Åbn projektet i Android Studio

Åbn **Android Studio**.

Vælg:

```text
File > Open
```

Vælg mappen:

```text
musGamesApp
```

Tryk:

```text
OK / Open
```

Vent på at Android Studio loader projektet og laver **Gradle Sync**.

---

## 3. Hvis Android Studio spørger om Gradle Sync

Tryk:

```text
Sync Now
```

Vent til sync er færdig.


## 4. Opret emulator

Gå til:

```text
Tools > Device Manager
```

Tryk:

```text
+ Add a new device
```

eller:

```text
Create Virtual Device
```

---

## 5. Vælg device

Vælg minimum:

```text
Pixel 8 Pro
```

Tryk:

```text
Next
```

---

## 6. Vælg Android version / system image

Her skal du vælge:

```text
API 34
UpsideDownCake
Android 14.0
Google Play Intel x86_64 Atom System Image
```

Hvis system imaget ikke allerede er installeret, tryk:

```text
Download
```

Når download/installationen er færdig, vælg samme system image igen.

Tryk:

```text
Next
```

---

## 7. Afslut emulator setup

På sidste side kan du lade standardindstillingerne være.

Tryk:

```text
Finish
```

Nu begynder Android Studio at installere emulatoren/system imaget.

Det kan tage cirka:

```text
2-5 minutter
```

---

## 8. Start emulatoren

Når emulatoren er oprettet, skal den kunne ses i **Device Manager**.

Tryk på play-knappen ud for din Pixel 8 Pro emulator.

Eller vælg emulatoren oppe i Android Studios device dropdown.

---

## 9. Kør projektet

Øverst i Android Studio skal du vælge app-konfigurationen.

Den hedder normalt noget som:

```text
app
```

Vælg din emulator som target device:

```text
Pixel 8 Pro API 34
```

Tryk på den grønne Run-knap:

```text
▶ Run
```

Android Studio bygger appen, installerer den på emulatoren og starter den.

---

## 10. Hvis emulatoren ikke vises

Gå til:

```text
Tools > Device Manager
```

Start emulatoren manuelt derfra.

Når den er startet, vælg den igen oppe i Android Studio som target device.

---

## 11. Hvis API 34 ikke findes

Gå til:

```text
Tools > SDK Manager
```

Under:

```text
SDK Platforms
```

Find:

```text
Android 14.0 UpsideDownCake
```

Sørg for at denne er installeret:

```text
Android SDK Platform 34
```

Under:

```text
SDK Tools
```

Sørg for at du har Android Emulator installeret.

Tryk:

```text
Apply
```

og derefter:

```text
OK
```

---

## 12. Kort kommando-version

```bash
git clone https://github.com/musGames/musGamesApp.git
cd musGamesApp
```

Derefter i Android Studio:

```text
File > Open > musGamesApp
Tools > Device Manager
Create Virtual Device
Pixel 8 Pro
Next
API 34 UpsideDownCake Android 14.0
Google Play Intel x86_64 Atom System Image
Next
Finish
Run
```
