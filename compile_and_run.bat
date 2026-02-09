@echo off
echo Running Mutation Tool...
echo.

cd src
echo Compiling with Calculator.java...
javac *.java ..\Calculator.java

if %ERRORLEVEL% equ 0 (
    echo.
    echo Success! Running program...
    echo ==============================
    java -cp .;.. Main
) else (
    echo Compilation failed
)

cd ..
echo.
pause