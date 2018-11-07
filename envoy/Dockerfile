#Update envoy default image to load configuration dinamically from configmap

FROM envoyproxy/envoy:latest

#If not provided start the default envoy
ENV ENVOY_CONFIG_PATH /etc/envoy.yaml
ENV ENVOY_STARTUP_PARAMS ""

CMD /usr/local/bin/envoy -c $ENVOY_CONFIG_PATH $ENVOY_STARTUP_PARAMS
