#!/bin/bash
function usage {
if [[ $# -eq 0 ]] ; then
    echo 'Usage: ./ecs-params-create.sh <SERVICENAME (Allowed values - name|webapp|greeting>'
    exit 0
fi
}

SERVICENAME="$1"

if ! [[ "$SERVICENAME" =~ ^(name|webapp|greeting)$ ]]; then usage ; fi

mkdir -p $SERVICENAME

source ecs-cluster.prop

cp ecs-params.template  ecs-params.yml
PARAM_FILE=ecs-params_"$SERVICENAME".yml
cp ecs-params.template $PARAM_FILE  

perl -i -pe 's/ECSRole/'${ECSRole}'/g' $PARAM_FILE
perl -i -pe 's/servicename/'${SERVICENAME}'/g' $PARAM_FILE
if [ "$SERVICENAME" != "webapp" ]; then
	perl -i -pe 's/subnet1/'${PrivateSubnet1}'/g' $PARAM_FILE
	perl -i -pe 's/subnet2/'${PrivateSubnet2}'/g' $PARAM_FILE
else
        perl -i -pe 's/subnet1/'${PublicSubnet1}'/g' $PARAM_FILE
	perl -i -pe 's/subnet2/'${PublicSubnet2}'/g' $PARAM_FILE
fi
perl -i -pe 's/sg-replaceme/'${SecurityGroupWebapp}'/g' $PARAM_FILE

mv $PARAM_FILE $SERVICENAME/
#echo "Changing directory to $SERVICENAME \n"
cd ./greeting

