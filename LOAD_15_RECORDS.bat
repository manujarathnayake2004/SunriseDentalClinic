@echo off
setlocal
cd /d "%~dp0"

echo ================================================
echo   Sunrise Dental Clinic - Load 15 Record Dataset
echo ================================================
echo.

set "MYSQL=C:\xampp\mysql\bin\mysql.exe"
if not exist "%MYSQL%" (
  echo XAMPP MySQL was not found at:
  echo %MYSQL%
  echo.
  echo Start XAMPP and confirm it is installed in C:\xampp.
  pause
  exit /b 1
)

echo Checking XAMPP MySQL connection...
"%MYSQL%" -u root -N -e "SELECT 1;" >nul 2>&1
if errorlevel 1 (
  echo.
  echo Could not connect to XAMPP MySQL with the default root account.
  echo Make sure XAMPP MySQL is running on port 3306.
  echo If your root account has a password, import the SQL manually in phpMyAdmin.
  pause
  exit /b 1
)

for /f %%A in ('"%MYSQL%" -u root -N -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='sunrise_dental' AND table_name='patients';"') do set TABLEOK=%%A
if not "%TABLEOK%"=="1" (
  echo.
  echo The Sunrise Dental tables are not ready yet.
  echo Run ClinicApplication once, then create the first ADMIN account at /setup.
  pause
  exit /b 1
)

for /f %%A in ('"%MYSQL%" -u root -N -e "SELECT COUNT(*) FROM sunrise_dental.users;"') do set USERCOUNT=%%A
if "%USERCOUNT%"=="0" (
  echo.
  echo No administrator account exists yet.
  echo Open http://localhost:8080/setup and create your ADMIN account first.
  pause
  exit /b 1
)

for /f %%A in ('"%MYSQL%" -u root -N -e "SELECT COUNT(*) FROM sunrise_dental.audit_log WHERE action_type='ASSESSMENT_DATASET' AND description='15-record assessment dataset loaded successfully.';"') do set ALREADY=%%A
if not "%ALREADY%"=="0" (
  echo.
  echo The 15-record dataset has already been loaded.
  echo No duplicate records were added.
  pause
  exit /b 0
)

echo Loading records...
"%MYSQL%" -u root < "%~dp0database\04_assessment_dataset_15.sql"
if errorlevel 1 (
  echo.
  echo The dataset could not be loaded. Review the SQL error above.
  pause
  exit /b 1
)

echo.
echo SUCCESS.
echo 15 patients, 15 dentists, 15 treatments, 15 staff records,
echo 15 appointments, 15 bills and 15 audit entries were added.
echo Existing records were not deleted.
echo.
echo Restart ClinicApplication, then press Ctrl+Shift+R in Chrome.
pause
endlocal
