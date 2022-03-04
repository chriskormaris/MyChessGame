if not exist bin mkdir bin
if not exist bin\images mkdir bin\images
if not exist bin\images\black mkdir bin\images\black
if not exist bin\images\white mkdir bin\images\white
if not exist bin\sounds mkdir bin\sounds
copy res\images\black bin\images\black
copy res\images\white bin\images\white
copy res\images bin\images
copy res\sounds bin\sounds
javac src\com\chriskormaris\mychessgame\api\chess_board\*.java src\com\chriskormaris\mychessgame\gui\*.java src\com\chriskormaris\mychessgame\gui\enumeration\*.java src\com\chriskormaris\mychessgame\gui\util\*.java src\com\chriskormaris\mychessgame\api\enumeration\*.java src\com\chriskormaris\mychessgame\api\ai\*.java src\com\chriskormaris\mychessgame\api\piece\*.java src\com\chriskormaris\mychessgame\api\util\*.java src\com\chriskormaris\mychessgame\api\util\evaluation_function\*.java src\com\chriskormaris\mychessgame\api\exception\*.java -d bin
pause
