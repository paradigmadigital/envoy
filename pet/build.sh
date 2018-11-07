#!/bin/bash

./gradlew clean assemble

sudo docker build -t jarodriguezparadigma/pet:1.0 .
sudo docker login -u jarodriguezparadigma
sudo docker push jarodriguezparadigma/pet:1.0
