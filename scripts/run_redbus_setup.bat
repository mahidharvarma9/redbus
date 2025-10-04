@echo off
echo.
echo ========================================
echo   RedBus Setup and Test Suite
echo ========================================
echo.
echo This will automatically set up and test your RedBus application.
echo Please make sure Docker Desktop is running first!
echo.
pause

cd /d "%~dp0"
python redbus_setup_and_test.py

echo.
echo ========================================
echo Setup completed! Check the results above.
echo ========================================
echo.
pause

