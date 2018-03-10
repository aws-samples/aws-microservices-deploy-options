#!/bin/bash
######   Prerequisites for running this script   ######
######    Need awscli configured with default profile and  jq (https://github.com/stedolan/jq/wiki/Installation)   and perl 5 or higher#########

if [ -f "$OUTPUT_FILE" ]; then
   echo "Removing prior property file"
   mv -f "$OUTPUT_FILE" "$OUTPUT_FILE".prior
fi

STACK_NAME=ecs-demo #default cluster name
OUTPUT_FILE="ecs-cluster.prop"
# if no command line arg given
if [ -z $1 ]
then
  	echo "No stackname was passed, will launch a new CFN with name ecs-demo"
	aws cloudformation create-stack --stack-name ecs-demo --template-url 'https://s3.amazonaws.com/compute-options-public/ecscli/infra.yaml' --capabilities CAPABILITY_IAM
elif [ -n $1 ]
then
	echo -e "Setting CFN Stack name to $1"
  	STACK_NAME=$1
fi

cfn_tail_create_new() {
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
	#check for typos in CFN Stack name from User
	 [[  -z  $lastEvent  ]] && echo "Error while calling DescribeStacks" && exit 0
	eventId=$(echo "$lastEvent" | jq -r .EventId)
	if [ "$eventId" != "$lastEventId" ]
	then
		lastEventId=$eventId
		echo $(echo $lastEvent | jq -r '.Timestamp + "\t-\t" + .ResourceType + "\t-\t" + .LogicalResourceId + "\t-\t" + .ResourceStatus')
	fi
	sleep 3
	stackStatus=$(aws cloudformation describe-stacks --region $region --stack-name $stack | jq -c -r .Stacks[0].StackStatus)
       if [[ "$stackStatus" == "CREATE_COMPLETE" ]]
       then
           aws cloudformation describe-stacks --stack-name $stack --query 'Stacks[0].Outputs' --output=text | tee ecs-cluster.prop       
       fi 

    done
  echo "Stack Status: $stackStatus"
}
cfn_tail_create_new $STACK_NAME us-east-1

#Create property file
if [ -f "$OUTPUT_FILE" ]; then
	perl -i.bak -lpe 's/\s+/=/g' "$OUTPUT_FILE"
	echo -e "Created Output property file"
	echo -e "Run this command on your terminal now:  source $OUTPUT_FILE \n "
	cat $OUTPUT_FILE
fi

