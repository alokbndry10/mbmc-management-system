#!/usr/bin/env pwsh
# Restaurant Management System - Quick Launch Script

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Restaurant Management System" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Cyan

# Check if MySQL service is running
Write-Host "[1/4] Checking MySQL service..." -ForegroundColor Green
$mysqlService = Get-Service -Name "MySQL*" -ErrorAction SilentlyContinue | Select-Object -First 1

if ($mysqlService) {
    if ($mysqlService.Status -eq "Running") {
        Write-Host "      ✓ MySQL is running" -ForegroundColor Green
    } else {
        Write-Host "      ! MySQL is not running. Starting..." -ForegroundColor Yellow
        try {
            Start-Service $mysqlService.Name
            Start-Sleep -Seconds 3
            Write-Host "      ✓ MySQL started successfully" -ForegroundColor Green
        } catch {
            Write-Host "      ✗ Failed to start MySQL. Please start it manually." -ForegroundColor Red
            Write-Host "      Run: net start $($mysqlService.Name)" -ForegroundColor Yellow
            exit 1
        }
    }
} else {
    Write-Host "      ✗ MySQL is not installed!" -ForegroundColor Red
    Write-Host "`n      Please install MySQL first:" -ForegroundColor Yellow
    Write-Host "      https://dev.mysql.com/downloads/installer/`n" -ForegroundColor Cyan
    Read-Host "Press Enter to exit"
    exit 1
}

# Navigate to project directory
$projectPath = "C:\Users\alokb\IdeaProjects\student management system"
Set-Location $projectPath

# Check if JAR exists
Write-Host "`n[2/4] Checking application JAR..." -ForegroundColor Green
$jarPath = "target\student_management_system-1.0-SNAPSHOT.jar"

if (-not (Test-Path $jarPath)) {
    Write-Host "      ! JAR not found. Building project..." -ForegroundColor Yellow

    # Find Maven
    $mvn = "C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.1\plugins\maven\lib\maven3\bin\mvn.cmd"
    if (-not (Test-Path $mvn)) {
        $mvn = "mvn"
    }

    Write-Host "`n[3/4] Building with Maven (this may take a minute)..." -ForegroundColor Green
    & $mvn clean package -DskipTests -q

    if ($LASTEXITCODE -eq 0) {
        Write-Host "      ✓ Build successful!" -ForegroundColor Green
    } else {
        Write-Host "      ✗ Build failed! Check error messages above." -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
} else {
    Write-Host "      ✓ JAR file found" -ForegroundColor Green
    Write-Host "`n[3/4] Skipping build (JAR already exists)" -ForegroundColor Green
}

# Run the application
Write-Host "`n[4/4] Launching application..." -ForegroundColor Green
Write-Host "      Default login: admin / admin123`n" -ForegroundColor Cyan

Write-Host "========================================`n" -ForegroundColor Cyan

# Launch the JAR
java -jar $jarPath

# Catch exit
if ($LASTEXITCODE -ne 0) {
    Write-Host "`n✗ Application exited with error code: $LASTEXITCODE" -ForegroundColor Red
    Read-Host "Press Enter to exit"
}

