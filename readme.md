# SimpleSevice

This is a very `simpleservice` that can be configured with environment variables to behave differently.

See [Makefile] to get it up and running.

## Demo for Instana

Demo the following call trace:

curl -> service-a.local/endpoint-a -> service-b.local/endpoint-b -> service-c.local/last-endpoint


## Infra
* Change perspective: Container & Host

* Filter:

  by K8S tags:
  entity.tag:"app=simpleservice\-a"

  by K8S namespaces:
  entity.kubernetes.namespace:mdl

  by Docker label:
  entity.docker.label:io.kubernetes.pod.name=simpleservice-deploy-*

  by Kubernetes label:
  entity.kubernetes.pod.label:* 

## Application

  TODO

## Issues


Built it Issues:

For deployment: When availableReplicas are less than desiredReplicas for 45 sec.

For node: When node is in bad condition for more than 30 sec.
          When allocatable cpu and memory is less than 20%

for pod: When pod is in bad condition for more than 30 sec.

for cluster: When cluster component status is unhealthy  for 30 sec.