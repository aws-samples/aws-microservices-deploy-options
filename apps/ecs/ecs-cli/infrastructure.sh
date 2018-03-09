#!/bin/bash
######   Prerequisites for running this script   ######
######    Need awscli configured with default profile and  jq (https://github.com/stedolan/jq/wiki/Installation)   and perl 5 or higher#########

OUTPUT_FILE="ecs-cluster.prop"

if [ -f "$OUTPUT_FILE" ]; then
   echo "Removing prior property file"
   mv -f "$OUTPUT_FILE" "$OUTPUT_FILE".prior
fi

aws cloudformation create-stack --stack-name ecs-demo --template-body file://infra.yaml --capabilities CAPABILITY_IAM

cloudformation_tail() {
  local stack="$1"
  local region="$2"
  local lastEvent
  local lastEventId
  local stackStatus=$(aws cloudformation describe-stacks --region $region --stack-name $stack | jq -c -r .Stacks[0].StackStatus)

  until \
	[ "$stackStatus" = "CREATE_COMPLETE" ] \
	|| [ "$stackStatus" = "CREATE_FAILED" ] \
	|| [ "$stackStatus" = "DELETE_COMPLETE" ] \
	|| [ "$stackStatus" = "DELETE_FAILED" ] \
	|| [ "$stackStatus" = "ROLLBACK_COMPLETE" ] \
	|| [ "$stackStatus" = "ROLLBACK_FAILED" ] \
	|| [ "$stackStatus" = "UPDATE_COMPLETE" ] \
	|| [ "$stackStatus" = "UPDATE_ROLLBACK_COMPLETE" ] \
	|| [ "$stackStatus" = "UPDATE_ROLLBACK_FAILED" ]; do
	
	#[[ $stackStatus == *""* ]] || [[ $stackStatus == *"CREATE_FAILED"* ]] || [[ $stackStatus == *"COMPLETE"* ]]; do
	lastEvent=$(aws cloudformation describe-stack-events --region $region --stack $stack --query 'StackEvents[].{ EventId: EventId, LogicalResourceId:LogicalResourceId, ResourceType:ResourceType, ResourceStatus:ResourceStatus, Timestamp: Timestamp }' --max-items 1 | jq .[0])
	eventId=$(echo "$lastEvent" | jq -r .EventId)
	if [ "$eventId" != "$lastEventId" ]
	then
		lastEventId=$eventId
		echo $(echo $lastEvent | jq -r '.Timestamp + "\t-\t" + .ResourceType + "\t-\t" + .LogicalResourceId + "\t-\t" + .ResourceStatus')
	fi
	sleep 3
	stackStatus=$(aws cloudformation describe-stacks --region $region --stack-name $stack | jq -c -r .Stacks[0].StackStatus)

       if [ "$stackStatus" == "CREATE_COMPLETE" ]
       then
           aws cloudformation describe-stacks --stack-name $stack --query 'Stacks[0].Outputs' --output=text | tee ecs-cluster.prop       
       fi 

    done
  echo "Stack Status: $stackStatus"
}

cloudformation_tail ecs-demo us-east-1

if [-f "$OUTPUT_FILE"]; then
	perl -i.bak -lpe 's/\s+/=/g' "$OUTPUT_FILE"
	echo "Created Output property file. \n"
	echo "Run this command on your terminal:  source $OUTPUT_FILE"
	cat $OUTPUT_FILE
fi






ecs-cli configure --cluster $ECSCluster --region us-east-1 --default-launch-type FARGATE




ecs-cli compose --verbose --file greeting-docker-compose.yaml --task-role-arn $ECSRole --ecs-params ecs-params_greeting.yml  service up --target-group-arn $GreetingTargetGroupArn --container-name  greeting --container-port 8081


ecs-cli compose --verbose --file name-docker-compose.yaml --task-role-arn $ECSRole --ecs-params ecs-params_name.yml  service up --target-group-arn $NameTargetGroupArn --container-name  name-service --container-port 8082



ecs-cli compose --verbose --file webapp-docker-compose.yaml --task-role-arn $ECSRole --ecs-params ecs-params_webapp.yml  service up --target-group-arn $WebappTargetGroupArn --container-name  webapp-service --container-port 8080
