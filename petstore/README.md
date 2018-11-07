# PetStore microservice

Microservice that returns information form a petStore. The pod will run the application alongside an Envoy instance working as sidecar proxy.

## Getting started

The purpose of this microservice is to serve requests invoking _pet_ microservice to see working communication between microservices through envoy instances. Because of that it does not include any persistence. The petStore detals are always the same and also the list of pets

## Prerequisites

* Docker hub account: if you want to change application or build your own docker your are going to need your own docker hub account and update build script and deployment configuration since the included docker account is my own.

* minishift / minikube running instance (or similar): you could deploy the application alone but it has no value, so if you want to check the overall system behaviour you will need some of these solutions.

## Configuration

The application does not have configuration by itself. The only element configured is the envoy instance. You can find the configuration in the configmap in _openshift/resources.yaml_. The envoy configuration has the following characteristics:

* Listens on port 10000 for ingress requests that are going to be forwarded to the local application
* Listens on port 9900 for egress requests coming from the local application that are going to be forwarded to the other services envoys.
* clusters: it configures three clusters: service discovery cluster, local service and the clusters _pet_ service. This last one resolve its instances using the service discovery.

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

* __docker__: you can run the built docker from https://hub.docker.com/r/jarodriguezparadigma/petstore/. If you want to build the docker on your own please check the _Docker_ section.

* __application__: the application does not have any dependencies except the ones needed to build the artifact. So you only need to build it and run it. Consider that some of the functionalities relay on the _pet_ microservice

## API

The API exposes three endpoints:

* __GET /petstore/{id}?includePets={includePets}__: retrieve the details of the petstore and its pets. The information of the petstore is always the same, no matter the value of the _id_ parameter. The _includePets_ boolean parameter determines if the service needs to ask for the pets to the _pets_ microservice. The returned list of pets is always the same and empty list if _includePets=false_. The _pet_ microservice __introduces a delay of 2 seconds__ for circuit breaking test purposes

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
