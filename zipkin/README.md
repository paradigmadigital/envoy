# Zipkin server

Zipkin server to collect distributed tracing

## Getting Started

This Zipkin will collect the distributed tracing data from the system. No persistence is included so data will be lost after every restart

It will use the default zipkin docker image. So this directory will only include the files to create the Openshift resources

## Configuration

The zipkin server will not include any special configuration. The default zipkin docker will be started

## Running

To run the docker itself does not have any value because it will show no information, besides it is the official docker image, no customization. To see it working you need other services sending their traces.
To run it inside the Kubernetes / Openshift environment you only need to create the _resources.yaml_ under the _openshift_ folder

## Openshift

The following resources are created by the _resources.yaml_:

* _deployment config_: it uses the official zipkin image
* _service_: to load balance between instances (if persistence is included in the future)
* _route_: to be able to access the service from outside the PaaS
