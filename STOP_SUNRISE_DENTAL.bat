@echo off
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ports=8080,8081; foreach($p in $ports){ Get-NetTCPConnection -LocalPort $p -State Listen -ErrorAction SilentlyContinue | ForEach-Object { try { Stop-Process -Id $_.OwningProcess -Force -ErrorAction Stop; Write-Host ('Stopped process on port ' + $p) -ForegroundColor Green } catch {} } }"
echo Sunrise Dental Clinic application services have been stopped.
pause
