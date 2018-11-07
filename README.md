# Envoy example

The purpose of this project is to provide an example of configuration and operation of an Envoy environment. The aim is to examplify mostly the features of zuul and hystrix but by Envoy. For that purpose some basic microservices were built.

The whole system was deployed in a minishift

## Getting started

The project has the following directories. __You can find a README for each one__ that I recommend you to visit. We will only use this for the general and common explanations.

* envoy: envoy docker image that we will use for the examples
* envoy-sds: discovery service that envoy will invoke to perform service discovery. It queries the available services to the kubernetes API
* ingress: envoy proxy that will work as gateway to the whole system
* pet: microservice that will work as a server of pet instances
* petstore: microservice that will act as a client of the pet microservice
* zipkin: server that stores distributed tracing data

## Architecture

The following image shows the built architecture.

![alt text](/arquitectura.jpg)

The following diagram depicts the sequence communication of a request to the petStore service

![alt text](/secuencia.jpg)

1. In the first step a customer performs a request to the ecosystem entrypoint which is the ingress
2. The ingress envoy container redirects the request to the envoy of the requested microservice, in this case the petstore.
3. The PetStore envoy sends the request to the PetStore service running on port 8080
4. The PetStore service needs information from the Pet service so it sends the request to its local envoy to port 9900
5. The PetStore envoy sends the request to the Pet envoy instance running on port 10000
6. The Pet envoy instance sends the request to the Pet service that will perform the concrete logic once it finishes it will send the response to its local envoy
7. The Pet envoy will return the response to the PetStore envoy instance
8. The PetStore envoy instance sends the response to the PetStore application
9. PetStore application combines pet data with its own data and send the response to its local envoy
10. PetStore envoy instance sends the response to the ingress envoy
11. The ingress envoy returns the response to the customer

## Prerequisites

In order to execute the example you will need:

* A running minishift cluster (of course it should also work in Openshift). I didn't test it on Kubernetes but I am assuming it will work.
* The application uses Gradle as build tool, but a wrapper is included so you shouldn't need to install it
* The application downloads some dockers from dockerhub. Those dockers are public but if you want to build your own, obviously you are going to need access to a docker registry and also docker installed (you probably already have it because you are running a docker orchestration tool).

## Installing

There is a script in the root directory called create_openshift.sh that will create a service account and all the resources to run the example.
You need to be previously logged in in your cluster to execute the script and have privileges to create service accounts.
Plus, the application assumes in some resources a namespace called envoy. If not you need to adjust some configurations.

## REST API

After all has been started you should have a openshift route of the ingress service where you can invoke the following endpoints:

* GET /petstore/petstore/2?includePets=true the '2' could be whatever number because it is not used internally. The includePets determines if a call will be performed to the pet service or an empty list of pets will be returned
* GET /pet/pets?petStoreId=2 the value of petStoreId is not used internally so you can provide any number

As you may have guessed the first part of the URI determines wich microservice to invoke. This is similar to how a routing is configured in zuul

## Built with
* minishift (running openshift v3.7.1)
* Java 9.0.4

You can find the spring, gradle, envoy ... versions in the corresponding resources

## Future work

* Configure health checks
* Load tests
* Develop a Java filter that adds the host header from the uri's host so microservice name can be written directly on the URL
* Include some microservice written in another programming language
* Test another envoy features like deployments, TLS ...
