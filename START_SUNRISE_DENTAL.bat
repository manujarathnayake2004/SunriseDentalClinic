@echo off
setlocal
cd /d "%~dp0"

echo ==============================================
echo       SUNRISE DENTAL CLINIC - START
echo ==============================================
echo.

echo Starting billing web service on port 8081...
start "Sunrise Billing Service" powershell -NoExit -ExecutionPolicy Bypass -File "%~dp0run-billing.ps1"

timeout /t 3 /nobreak >nul

echo Starting clinic application on port 8080...
start "Sunrise Clinic Application" powershell -NoExit -ExecutionPolicy Bypass -File "%~dp0run-clinic.ps1"

echo Waiting for the clinic application to become ready...
powershell -NoProfile -Command "$ready=$false; for($i=0;$i -lt 120;$i++){ if(Test-NetConnection -ComputerName 127.0.0.1 -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue){$ready=$true; break}; Start-Sleep -Seconds 1 }; if($ready){exit 0}else{exit 1}"

if errorlevel 1 (
    echo.
    echo The clinic application did not become available on port 8080.
    echo Check the Billing Service and Clinic Application windows for the exact error.
    pause
    exit /b 1
)

echo Opening Sunrise Dental Clinic...
start "" "http://localhost:8080"
endlocal
