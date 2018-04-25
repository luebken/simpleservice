#! /usr/bin/make -f

.DEFAULT_GOAL := help

build: ## build the jar and docker image
	./mvnw package
	docker build -t luebken/simpleservice .

java-run: ## run the jar
	java -jar target/simpleservice-0.0.1.jar

docker-push: ## push the docker image
	docker push luebken/simpleservice

# via http://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
help: ##Shows help message
	@echo "Available make commands:"
	@grep -E '^[0-9a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'