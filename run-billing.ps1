$ErrorActionPreference = "Stop"
. (Join-Path $PSScriptRoot "tools\resolve-maven.ps1")
$mvn = Resolve-MavenCommand
if (-not $mvn) {
    Write-Host "Maven was not found." -ForegroundColor Red
    Write-Host "Install Apache Maven 3.9.x or extract it to C:\Maven\apache-maven-3.9.16." -ForegroundColor Yellow
    Read-Host "Press Enter to close"
    exit 1
}

Write-Host "Starting Sunrise Dental Billing Service on port 8081..." -ForegroundColor Green
Set-Location $PSScriptRoot
& $mvn -pl billing-service spring-boot:run
