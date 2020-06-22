if not exist bin mkdir bin
if not exist bin\images mkdir bin\images
if not exist bin\images\black mkdir bin\images\black
if not exist bin\images\white mkdir bin\images\white
if not exist bin\sounds mkdir bin\sounds
copy res\images\black bin\images\black
copy res\images\white bin\images\white
copy res\sounds bin\sounds
javac src\chess\*.java src\chessGUI\*.java src\chessGui2\*.java src\minimaxAi\*.java src\pieces\*.java src\utilities\*.java -d bin
pause
