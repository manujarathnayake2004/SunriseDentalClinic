$ErrorActionPreference = "Stop"
. (Join-Path $PSScriptRoot "tools\resolve-maven.ps1")
$mvn = Resolve-MavenCommand

Write-Host "Starting Sunrise Dental Clinic on port 8080..." -ForegroundColor Cyan
Write-Host "Database target: XAMPP MySQL/MariaDB on 127.0.0.1:3306" -ForegroundColor DarkCyan

if (-not $mvn) {
    Write-Host "Maven was not found." -ForegroundColor Red
    Write-Host "Install Apache Maven 3.9.x or extract it to C:\Maven\apache-maven-3.9.16." -ForegroundColor Yellow
    Read-Host "Press Enter to close"
    exit 1
}

function Test-MySqlPort {
    try {
        return (Test-NetConnection -ComputerName 127.0.0.1 -Port 3306 -InformationLevel Quiet -WarningAction SilentlyContinue)
    } catch {
        return $false
    }
}

if (-not (Test-MySqlPort)) {
    $xamppHome = if (-not [string]::IsNullOrWhiteSpace($env:XAMPP_HOME)) { $env:XAMPP_HOME } else { "C:\xampp" }
    $mysqlStart = Join-Path $xamppHome "mysql_start.bat"

    if (Test-Path $mysqlStart) {
        Write-Host "XAMPP MySQL is not running. Starting it from $xamppHome ..." -ForegroundColor Yellow
        Start-Process -FilePath "cmd.exe" -ArgumentList "/c", "`"$mysqlStart`"" -WindowStyle Minimized

        $ready = $false
        for ($i = 0; $i -lt 25; $i++) {
            Start-Sleep -Seconds 1
            if (Test-MySqlPort) { $ready = $true; break }
        }

        if (-not $ready) {
            Write-Host "XAMPP MySQL did not become available on port 3306." -ForegroundColor Red
            Write-Host "Open XAMPP Control Panel and start MySQL manually. If port 3306 is busy, stop MySQL80 first." -ForegroundColor Yellow
            Read-Host "Press Enter to close"
            exit 1
        }
    } else {
        Write-Host "Could not find XAMPP at C:\xampp and XAMPP_HOME is not set." -ForegroundColor Red
        Read-Host "Press Enter to close"
        exit 1
    }
}

Write-Host "Database server is available on port 3306." -ForegroundColor Green
if ([string]::IsNullOrWhiteSpace($env:DB_USERNAME)) { $env:DB_USERNAME = "root" }

Set-Location $PSScriptRoot
& $mvn -pl clinic-app spring-boot:run
