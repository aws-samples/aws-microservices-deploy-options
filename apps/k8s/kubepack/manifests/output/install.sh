#!/bin/bash


pushd manifests/output/github.com/kubepack/aws-microservices-deploy-options/apps/k8s/kubepack
kubectl apply -R -f .
popd
			
