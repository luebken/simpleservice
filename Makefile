#! /usr/bin/make -f

.DEFAULT_GOAL := help
MY_VAR := $(shell kubectl get -o json svc/simpleservice-service |jq .status.loadBalancer.ingress[0].ip)

build: ## build the jar and docker image
	./mvnw package
	docker build -t luebken/simpleservice .

watch-kubectl:
	watch -n 1 kubectl get svc,deploy,rs,po

java-run: ## run the jar
	export SIMPLE_SERVICE_DOWNSTREAM_SERVICE=http://localhost:8080; java -jar target/simpleservice-0.0.1.jar

docker-push: ## push the docker image
	docker push luebken/simpleservice

kubectl-create: # creates a svc and deploy
	kubectl create -f deployment.yaml
	kubectl create -f service.yaml

kubectl-apply: # updates a deploy
	kubectl apply -f deployment.yaml

kubectl-delete:
	-kubectl delete svc/simpleservice-service
	kubectl delete deploy/simpleservice-deploy

curl:
	curl $(MY_VAR)

# via http://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
help: ##Shows help message
	@echo "Available make commands:"
	@grep -E '^[0-9a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'