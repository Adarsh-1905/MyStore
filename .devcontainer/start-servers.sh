#!/bin/bash
set -e

echo "🚀 Starting MyStore application servers..."

# Start backend in background
echo "🔧 Starting Spring Boot backend on port 8080..."
cd /workspaces/MyStore/backend
mvn clean spring-boot:run &
BACKEND_PID=$!

# Give backend time to start
echo "⏳ Waiting for backend to initialize..."
sleep 15

# Start frontend
echo "⚛️  Starting Angular frontend on port 4200..."
cd /workspaces/MyStore/frontend
npm start

# Keep running
wait
