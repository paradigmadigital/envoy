#!/bin/bash

sudo docker build -t jarodriguezparadigma/envoy:1.0 .
sudo docker login -u jarodriguezparadigma
sudo docker push jarodriguezparadigma/envoy:1.0
