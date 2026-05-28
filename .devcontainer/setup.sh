#!/bin/bash
set -e

echo "🔧 Setting up MyStore development environment..."

# Install Maven
echo "📦 Installing Maven..."
apt-get update
apt-get install -y maven

# Install frontend dependencies
if [ -f "frontend/package.json" ]; then
  echo "📚 Installing Angular frontend dependencies..."
  cd frontend
  npm ci
  cd ..
fi

echo "✅ Setup complete!"
