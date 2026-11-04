# AntiPlague Game

A desktop strategy game built with **Java Swing**. A virus spreads between countries on a world map, and the player spends points on upgrades to slow it down and cure the infected population.

## Features
- **World map** with 10 countries (USA, Canada, Brazil, Turkey, Poland, France, Norway, China, Australia, South Africa), each with its own population and infection count
- **Infection spread** between connected countries, driven by Swing `Timer`s
- **Animated transport routes:** cars, planes and ships move between countries
- **Upgrade shop:** medical aid, vaccine distribution, travel restrictions, quarantine, awareness campaigns, global lockdown and more
- **Three difficulty levels** (Easy / Medium / Hard) that change how fast the virus spreads
- **Live HUD** that shows points and elapsed time
- **High-score table** saved to a local file between sessions
- **Keyboard shortcut:** `Ctrl + Shift + Q` quits the current game

## How to run
Requirements: JDK 8 or newer.

**IntelliJ IDEA:** open the project folder and run `Main`.

**Command line** (from the project root):
```bash
javac -d out src/Main.java
java -cp out Main
```
> Run from the project root so the game can find the `src/` and `resources/` images.

## Project structure
```
src/
  Main.java              # game window, menus, map, timers, upgrades, Country class
  menufoto.jpg           # main-menu background
  dunya-haritasi-2.jpg   # world map
resources/
  car / plane / ship icons
```

## Author
Mustafa Mert Atalan
