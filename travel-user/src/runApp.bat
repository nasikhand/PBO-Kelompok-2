@echo off
cd src
javac -cp ".;lib/mysql-connector-j-9.3.0.jar" App.java db\Koneksi.java views\Auth\LoginUser.java
if errorlevel 1 (
  echo Kompilasi gagal!
  pause
  exit /b 1
)
java -cp ".;lib/mysql-connector-j-9.3.0.jar" App
pause
