# My Chess Game #

© Created by: Christos Kormaris, Athens 2020

Programming Language: `Java`

Video for this project:
- https://www.youtube.com/watch?v=rgYDlqQ_WSI

A traditional Chess game implementation using Minimax AI, with Alpha-Beta Pruning.
The Minimax AI is based on code from the laboratories of the `Artificial Intelligence` course,
of the `Department of Informatics`, of the `Athens University of Economics and Business`.
I created this project from scratch, line by line.
I started this project as a hobby, on March 2020, during the COVID-19 quarantine.
This project was a test for my skills as a programmer, and I'm very happy with the result. 
Of course, my free time and a little luck were some assets too.

**Fun fact:**
I decided to make this project, after I applied for a job interview for the Forex company `XM`.
The exercise I had to solve was a Chess problem in Java.
I had to make an algorithm that could find if a Knight piece could visit a position on the Chess board,
in less than `n` moves. The solution was a simple BFS algorithm.
For the purpose of this task, I designed a GUI.
After I was hired in the company, I continued the implementation of the GUI, adding more and more components every time.
Eventually, I implemented the movement of all the Chess pieces, the checkmate, the stalemate, 
the rest of the draw conditions and most of the other Chess functionalities.

The GUI is very customizable.
You can enable/disable sound, change square colors, GUI style, or even the number of the Chess board rows!!
In addition, you can use FEN (Forsyth–Edwards Notation) positions and start playing the game from there.

### Developer Notes
You need Gradle and Java 1.8 or above to compile and run this project.

**Recommended IDE:** `IntelliJ IDEA`

#### Java Modules
There are 4 Java modules inside this project:
1. `api`
2. `common`
3. `drag-and-drop-gui`
4. `buttons-gui`

- The `api` module contains all the logic for the Chess game. It can be used for the implementation of any Chess GUI.
- The `common` module contains common constants, libraries and utilities for the GUIs. It includes the `api` module.
- The `buttons-gui` module contains a GUI implementation using JButtons.
It includes the `api` and `common` modules.
- The `drag-and-drop-gui` module contains a GUI implementation using a JLayeredPane and the drag and drop method,
for moving the pieces. It includes the `api` and `common` modules.

#### AI

- The `AI` is also customizable. There are 2 AI types:
1. Minimax AI (you can optionally use Alpha-Beta pruning)
2. Random Choice AI

- There are 4 evaluation function implementations for the Minimax AI:
1. `Simplified` evaluation
2. `PeSTO` (Piece-Square Tables Only) evaluation
3. `Wukong` evaluation
4. `Shannon` evaluation

### Minimax Chess on Google Play

I am using the `api` module for the implementation of the following Android game, which I've also created:
- https://play.google.com/store/apps/details?id=com.chriskormaris.mychessgame


### Screenshots

#### Drag & Drop GUI
![screenshot](/screenshots/drag-and-drop-gui.png)

#### Buttons GUI
![screenshot](/screenshots/buttons-gui.png)


### References

The Java Swing Buttons GUI was inspired from:
- https://stackoverflow.com/questions/21077322/create-a-chess-board-with-jpanel
- http://www.java2s.com/Tutorials/Java/Swing_How_to/Layout/Create_Chess_layout_using_GridLayout.htm

The Java Swing Drag & Drop GUI was inspired from:
- https://stackoverflow.com/questions/64598967/trying-to-drag-something-using-a-jlayeredpane
- https://gist.github.com/skd1993/288fb21707263ae03799

The evaluation functions were based on:
- https://www.chessprogramming.org/Simplified_Evaluation_Function
- https://www.chessprogramming.org/PeSTO%27s_Evaluation_Function
- https://github.com/maksimKorzh/wukongJS

Other sources:
- https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
- https://github.com/amir650/BlackWidow-Chess
