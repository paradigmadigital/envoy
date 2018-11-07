#!/bin/bash
 
./gradlew clean assemble

sudo docker build -t jarodriguezparadigma/petstore:1.0 .
sudo docker login -u jarodriguezparadigma
sudo docker push jarodriguezparadigma/petstore:1.0
