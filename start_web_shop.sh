#!/bin/bash

# Wechsle zum Verzeichnis des Backends
cd backend

# Befehle für das Backend ausführen
docker-compose build
docker-compose up -d

# Zurück zum ursprünglichen Verzeichnis (root)
cd ..

# Wechsle zum Verzeichnis des Frontends
cd frontend

# Befehle für das Frontend ausführen
npm install
npm run serve

# Zurück zum ursprünglichen Verzeichnis (root)
cd ..
