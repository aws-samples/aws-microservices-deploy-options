sam local invoke -t sam.yaml -e test/find-all-names-event.json NameFunction
sam local invoke -t sam.yaml -e test/find-name-event.json NameFunction
sam local invoke -t sam.yaml -e test/event.json GreetingFunction
