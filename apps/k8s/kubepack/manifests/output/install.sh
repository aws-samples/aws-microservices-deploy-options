#!/bin/bash


pushd manifests/output/github.com/aws-samples/aws-microservices-deploy-options/apps/k8s/kubepack
kubectl apply -R -f .
popd
			
