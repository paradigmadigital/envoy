#!/bin/bash
 
./gradlew clean assemble

sudo docker build -t jarodriguezparadigma/sds:1.0 .
sudo docker login -u jarodriguezparadigma
sudo docker push jarodriguezparadigma/sds:1.0