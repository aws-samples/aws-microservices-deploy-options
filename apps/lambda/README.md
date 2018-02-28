# Serverless API
This project uses [`aws-serverless-java-container`](https://github.com/awslabs/aws-serverless-java-container).

The starter project defines a number of resources `/`, `/resources/greeting`, and `/resources/names` that can accept `GET` requests.

The project folder also includes a `sam.yaml` file. You can use this [SAM](https://github.com/awslabs/serverless-application-model) file to deploy the project to AWS Lambda and Amazon API Gateway or test in local with [SAM Local](https://github.com/awslabs/aws-sam-local). 

## Compile

Using [Maven](https://maven.apache.org/), you can create an AWS Lambda-compatible jar file simply by running the maven package command from the projct folder.

```bash
$ cd aws-compute-options/services
$ mvn clean package -Plambda
```

## Install

You can use [AWS SAM Local](https://github.com/awslabs/aws-sam-local) to start your project.

First, install SAM local:

```bash
$ npm install -g aws-sam-local
```

## Test

### Test Lambdas

Next, from the project root folder - where the `sam.yaml` file is located - start the API with the SAM Local CLI.

```bash
cd aws-compute-options/apps/lambda
sam local invoke -t sam.yaml -e test/find-all-names-event.json NamesFunction
sam local invoke -t sam.yaml -e test/find-name-event.json NamesFunction
sam local invoke -t sam.yaml -e test/greeting-event.json GreetingFunction
```

### Test API

Next, from the project root folder - where the `sam.yaml` file is located - start the API with the SAM Local CLI.

For Mac users, please use the following command

```bash
$ cd aws-compute-options/apps/lambda
$ sam local start-api --template sam.yaml --env-vars test/env-mac.json

...
Mounting com.sapessi.jersey.StreamLambdaHandler::handleRequest (java8) at http://127.0.0.1:3000/{proxy+} [OPTIONS GET HEAD POST PUT DELETE PATCH]
...
```

For Windows users, please use the following command

```bash
$ cd aws-compute-options/apps/lambda
$ sam local start-api --template sam.yaml --env-vars test/env-win.json

...
Mounting com.sapessi.jersey.StreamLambdaHandler::handleRequest (java8) at http://127.0.0.1:3000/{proxy+} [OPTIONS GET HEAD POST PUT DELETE PATCH]
...
```

Using a new shell, you can send a test ping request to your API:

```bash
$ curl -s http://127.0.0.1:3000/resources/greeting

Hello

$ curl -s http://127.0.0.1:3000/

Hello
``` 

## Deploy to AWS

You can use the [AWS SAM Local CLI](https://github.com/awslabs/aws-sam-local) to quickly deploy your application to AWS Lambda and Amazon API Gateway with your SAM template. To use the package command you will need to have the [AWS CLI](https://aws.amazon.com/cli/) installed.

You will need an S3 bucket to store the artifacts for deployment. Once you have created the S3 bucket, run the following command from the project's root folder - where the `sam.yaml` file is located:

```
$ sam package --template-file sam.yaml --output-template-file output-sam.yaml --s3-bucket <YOUR S3 BUCKET NAME>
Uploading to xxxxxxxxxxxxxxxxxxxxxxxxxx  6464692 / 6464692.0  (100.00%)
Successfully packaged artifacts and wrote output template to file output-sam.yaml.
Execute the following command to deploy the packaged template
aws cloudformation deploy --template-file /your/path/output-sam.yaml --stack-name <YOUR STACK NAME>
```

As the command output suggests, you can now use the cli to deploy the application. Choose a stack name and run the `aws cloudformation deploy` command from the output of the package command.
 
```
$ sam deploy --template-file output-sam.yaml --stack-name aws-compute-options-lambda --capabilities CAPABILITY_IAM
```

Once the application is deployed, you can describe the stack to show the API endpoint that was created. The endpoint should be the `ServerlessJerseyApi` key of the `Outputs` property:

```
$ aws cloudformation describe-stacks --stack-name aws-compute-options-lambda --query 'Stacks[0].Outputs[*].{Service:OutputKey,Endpoint:OutputValue}'
[
    {
        "Endpoint": "https://xxxxxxx.execute-api.us-west-2.amazonaws.com/prod/", 
        "Service": "WebappApiEndpoint"
    }, 
    {
        "Endpoint": "https://xxxxxxx.execute-api.us-west-2.amazonaws.com/prod/resources/names", 
        "Service": "NamesApiEndpoint"
    }, 
    {
        "Endpoint": "https://xxxxxxx.execute-api.us-west-2.amazonaws.com/prod/resources/greeting", 
        "Service": "GreetingApiEndpoint"
    }
]
```

Copy the `Endpoint` into a browser or use curl to test your first request:

```bash
$ curl -s https://xxxxxxx.execute-api.us-west-2.amazonaws.com/prod/greeting
Hello
```
