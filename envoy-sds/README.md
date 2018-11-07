# Discovery Service for Envoy

Envoy discovery service that Envoy instances will ask for cluster members. The service will ask Kubernetes API for those cluster members

## Getting started

Envoy recommends to use a SDS/EDS service (in their own terminology) as default way of discovering cluster memebers IPs. In order to do that it provides an API definition a service should implement and that all envoy instances will invoke to retrieve the list of instances of each cluster.

There are three versions of that API:

* REST v1: already deprecated and much more simple
* REST v2: it allows to load dinamically almost every envoy configuration
* gRPC v2: same v2 API but communicating with gRPC

For the sake of simplicity __we implemented the REST v1 API__ as I didn't know from the beginning it was already deprecated and also because __it may require a lot of time to implement v2__ and that would exceed the scope of the POC.

The purpose of the implementation is that, knowing envoy can't resolve instances of the ecosystem, connect to Kubernetes API to retrieve the list of instances. This way every time envoy invokes the API to retrieve the list of instances of a cluster our implementation will ask Kubernetes API for that instances list and return it to the Envoy instance.

If you want to implement v2 version of the API you can find Java implementation from Envoy here https://github.com/envoyproxy/java-control-plane  You would only need to adapt it to query kubernetes for each resources type. Consider that resources types are different in Kubernetes and in Envoy

__You will need a service account with special permissions to run this service__ because it will need permission to query the Kubernetes API, in order to do that you are going to need a user with those permissions in Openshift / Kubernetes

## Prerequisites

* Openshift / Kubernetes account with permissions to create service accounts: you will need to create a service account that can query Kubernetes API. You can find the commands to create it in the _create_openshift.sh_ script in the root directory.
* Docker hub account: if you want to change application (not configuration) or build your own docker your are going to need your own docker hub account and update build script and deployment configuration since the included docker account is my own.

## Configuration

The application __doesn't include configuration in the artifact as it will be loaded from configmaps__. But you can find the application configuration in the configmap in the file openshift/resources.yaml. The __configuration is loaded from an external resource__ by using the _spring.config.location_ startup param (you can find it in the Dockerfile).

If you want to run it locally without the docker you should copy the application.yml from the configmap to somo location in your computer and use the startup param to tell application where to find its configuration. Or you could use the already existing docker image.

These are the configuration properties:

* kubernetes.namespace: namespace where the project will run. It is used to query the pods IPs from the kubernetes API
* kubernetes.api.host: host direction where the application can find the Kubernetes API
* kubernetes.token.path: path inside the docker where openshift will load the service account token that will be used to authenticate against the kubernetes API
* envoy.port: port where envoy listens for ingress traffic. It will be used to rewrite pods port because we are not going to communicate directly the application but by its local envoy

## Build

Application is built using the gradle wrapper with the _gradlew clean assemble_ command.
You can find the details of how to build the docker in the following section.

## Docker

Docker image only includes application but not configuration. So you need to __provide a volume with the configuration file__. You can tell the application where to find that file using the environment variable _CONFIG_PATH_. You also need to provide a volume with the token to access the Kubernetes API, you can tell application where to find the token in the configuration file.

Application listens on 8080 port.

In the root directory it is the script _build.sh_ that builds the application using the gradle wrapper and packages it inside the docker. This script also __tries to publish to a dockerhub account__ which you need credentials to access to. So you need to remove that part of the script or change it for your own account. Also remember that if you do so, you also need to update the docker reference in the deployment_config in the _openshift/resources.yaml_ file.

## Running

You have 3 options to run the application depending on your environment:

* __minishift / minikube__ (or similar): you only need to create the resources under the _openshift_ directory. Remember that you have to previously create the service account as described in the _Prerequisites_ section

* __docker__: you can run the built docker from https://hub.docker.com/r/jarodriguezparadigma/sds/ but you have to consider that it does not include application configuration so you will need to add a volume with the configuration file that you can get from _openshift/resources.yaml_ configmap and another volume with a token to authenticate against kubernetes API. You will need to update the configuration file values depending where you mount the token volume and where your kubernetes API is running. Check _Docker_ section for more details

* __application__: as said previously you need to provide the path where to find Kubernetes token (see previous docker point to know how). And you need to provide the path to the configuration file. To do so you can use the _-Dspring.config.location=<path>_ startup param

## API

Application provides a single endpoint:

__GET /v1/registration/{serviceName}__: returns the list of instances for the provided serviceName. _serviceName_ is the name of a _service_ layer from Kubernetes / Openshift. The namespace where the _service_ resides is read from the configuration file.

## Openshift

The _resources.yaml_ under the _openshift_ directory creates the following resources:

* _configmap_: with the application configuration file. Check _configuration_ section for more details
* _deployment config_: that downloads the docker from dockerhub, uses the created service account and loads the configmap
* _service_: to load balance in case of various instances

## Built with

* Java 9.0.4
* Gradle 4.5.1
* Docker base image: openjdk:9
* Docker version: 17.12.1-ce
