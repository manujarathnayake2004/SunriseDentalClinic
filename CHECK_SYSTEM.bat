@echo off
setlocal
cd /d "%~dp0"
echo ===== Sunrise Dental Clinic System Check =====
where java >nul 2>nul && (java -version) || echo Java is not available in PATH.
powershell -NoProfile -ExecutionPolicy Bypass -Command ". '%~dp0tools\resolve-maven.ps1'; $m=Resolve-MavenCommand; if($m){Write-Host ('Maven: ' + $m) -ForegroundColor Green}else{Write-Host 'Maven not found' -ForegroundColor Red}; foreach($p in 3306,8080,8081){$ok=Test-NetConnection 127.0.0.1 -Port $p -InformationLevel Quiet -WarningAction SilentlyContinue; Write-Host ('Port ' + $p + ': ' + $(if($ok){'OPEN'}else{'closed'}))}"
pause
