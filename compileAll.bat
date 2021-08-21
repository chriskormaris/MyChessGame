if not exist bin mkdir bin
if not exist bin\images mkdir bin\images
if not exist bin\images\black mkdir bin\images\black
if not exist bin\images\white mkdir bin\images\white
if not exist bin\sounds mkdir bin\sounds
copy res\images\black bin\images\black
copy res\images\white bin\images\white
copy res\images bin\images
copy res\sounds bin\sounds
javac src\chess_board\*.java src\chess_gui\*.java src\chess_gui2\*.java src\enumeration\*.java src\minimax_ai\*.java src\piece\*.java src\utility\*.java -d bin
pause
