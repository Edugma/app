#!/bin/bash

# Set local Git username
echo "Enter your Git username:"
read username
git config user.name "$username"

# Set local Git email
echo "Enter your Git email:"
read email
git config user.email "$email"

# Confirm the settings
echo "Git username and email have been set locally:"
git config user.name
git config user.email
