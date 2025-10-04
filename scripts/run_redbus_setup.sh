#!/bin/bash

echo
echo "========================================"
echo "   RedBus Setup and Test Suite"
echo "========================================"
echo
echo "This will automatically set up and test your RedBus application."
echo "Please make sure Docker Desktop is running first!"
echo

read -p "Press Enter to continue or Ctrl+C to cancel: "

cd "$(dirname "$0")"
python3 redbus_setup_and_test.py

echo
echo "========================================"
echo "Setup completed! Check the results above."
echo "========================================"
echo

