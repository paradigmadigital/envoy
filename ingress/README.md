# Reverse edge proxy

Envoy instance that is going to work as ingress. All requests to the services coming from outside the system will go through it.

## Getting Started

This element is an Envoy instance that will work as ingress / reverse proxy. It will use the envoy docker image from the root envoy directory. So this directory will only include the files to create the Openshift resources (envoy configuration between those resources)

## Configuration

The configuration of the envoy instance is included in the configmap that you can find in _openshift/resources.yaml_. The configuration have the following characteristics:

* _Clusters_: it configures three clusters: service discovery cluster and the clusters for the petstore and pet services. These last two resolve their instances using the service discovery.
* _Mappings_: it configures two route mappings one for each microservice. You can invoke pet microservice under _/pet_ path and petstore under _/petstore_ path

## Running

Running the ingress instance itself does not have value because its purpose is to route requests to microservices so running the instance alone won't let you check its behaviour. To do that you need to run it in a openshift / kubernetes environment with the rest of the pieces that are included in the repository. You will need almost each of them, because if for example you don't deploy the sds service, you won't be able to discover the pet and petstore services instances.

## Openshift

The following resources are created by the _resources.yaml_:

* _configmap_: the configuration included in the configmap is explained in the _Configuration_ section
* _deployment config_: it uses the envoy image from this repository and configure it to run in cluster mode.
* _service_: to load balance between instances
* _route_: to be able to access the service from outside the PaaS
