# Envoy docker image

Update default envoy docker image to provide configuration file path and startup parameters as environment variables. This envoy image __will be used for all the purposes__: as a sidecar proxy (sharing the pod with the application) and as a reverse proxy itself (alone in the pod). The docker is configuration-agnostic as it is intended to load the configuration from a volume or configmap

## Getting started

The repository includes the Dockerfile itself and a build script. If you want to build the docker yourself there is some restrictions that you can find in the 'Prerequisites' section

## Prerequisites

In order to execute the docker you only need access to dockerhub where it is publically available. https://hub.docker.com/r/jarodriguezparadigma/envoy/

If you want to build it using the script consider that it publishes the docker to 'jarodriguezparadigma' account so at some point it will ask you for credentials to access the account. So you are going to need to __create your own account at dockerhub__ and edit the script to upload the docker to it. Also you will need to __edit the resources.yaml files for each deployment to update the image__ to download from dockerhub

## Installing

As said before the docker is already available publically at dockerhub

## Configuration

The docker considers two environment variables:

* ENVOY_CONFIG_PATH: path where envoy will find its configuration file and load it on startup. Default value '/etc/envoy.yaml'
* ENVOY_STARTUP_PARAMS: envoy optional startup params. Default value: ''

## Built with

* Docker base image: envoyproxy/envoy:latest
* Docker version: 17.12.1-ce
