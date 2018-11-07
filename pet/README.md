# Pet microservice

Microservice that returns the list of pets of a petstore. The pod will run the application alongside an Envoy instance working as sidecar proxy.

## Getting started

The purpose of this microservice is to serve requests from the petstore microservice to see working communication between microservices through envoy instances. Because of that it does not include any persistence. The list of pets returned for every pestore is always the same.

## Prerequisites

* Docker hub account: if you want to change application or build your own docker your are going to need your own docker hub account and update build script and deployment configuration since the included docker account is my own.

* minishift / minikube running instance (or similar): you could deploy the application alone but it has no value, so if you want to check the overall system behaviour you will need some of these solutions.

## Configuration

The application does not have configuration by itself. The only element configured is the envoy instance. You can find the configuration in the configmap in _openshift/resources.yaml_. The envoy configuration basically says that the instance will listen on port 10000 and will route every request to the local service.

## Build

Application is built using the gradle wrapper with the _gradlew clean assemble_ command.
You can find the details of how to build the docker in the following section.

## Docker

Docker image only includes the application that will listen on 8080 port.

In the root directory it is the script _build.sh_ that builds the application using the gradle wrapper and packages it inside the docker. This script also __tries to publish to a dockerhub account__ which you need credentials to access to. So you need to remove that part of the script or change it for your own account. Also remember that if you do so, you also need to update the docker reference in the deployment_config in the _openshift/resources.yaml_ file.

The docker of the Envoy instance is the defined in the _envoy_ folder under the repository root directory.

## Running

You have 3 options to run the application depending on your environment:

* __minishift / minikube__ (or similar): you only need to create the resources under the _openshift_ directory. It will download the docker images from dockerhub.

* __docker__: you can run the built docker from https://hub.docker.com/r/jarodriguezparadigma/pet/. If you want to build the docker on your own please check the _Docker_ section.

* __application__: the application does not have any dependencies except the ones needed to build the artifact. So you only need to build it and run it.

## API

The API exposes three endpoints:

* __GET /pets?petStoreId={petStoreId}__: retrieve the list of pets of a petStore. The returned list of pets is always the same, doesn't matter the parameter value. __Introduces a delay of 2 seconds__ for circuit breaking test purposes

* __GET /health__: returns and HTTP code representing the status of the application. The status is stored as an internval variable

* __POST /health/{status}__: updates the value of the internal variable that stores the status returned by the GET method.

## Openshift

The _resources.yaml_ under the _openshift_ directory creates the following resources:

* _configmap_: with the envoy configuration file. Check _configuration_ section for more details
* _deployment config_: that downloads both dockers (application and envoy) from dockerhub, they run on ports 8080 and 10000 respectively. Also the configmap is loaded in a volume
* _service_: for sds to discover instances

## Built with

* Java 9.0.4
* Gradle 4.5.1
* Docker base image: openjdk:9
* Docker version: 17.12.1-ce
