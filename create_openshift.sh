#!/bin/bash

# Create serviceAccount with credentials
oc create sa apisa -n envoy
oc adm policy add-role-to-user view -z apisa -n envoy

oc create -f ./envoy-sds/openshift/resources.yaml
oc create -f ./petstore/openshift/resources.yaml
oc create -f ./pet/openshift/resources.yaml
oc create -f ./ingress/openshift/resources.yaml
oc create -f ./zipkin/openshift/resources.yaml
