.PHONY: install package deploy

S3_BUCKET=$${S3_BUCKET}

install: 
	cd ../../services && mvn clean install -Plambda

package: ../../services/webapp/target/webapp.zip ../../services/greeting/target/greeting.zip ../../services/name/target/name.zip
	if [ -n "$${AWS_PROFILE}" ]; then export AWS_PROFILE="--profile $${AWS_PROFILE}"; fi;\
  sam package --template-file sam.yaml --s3-bucket ${S3_BUCKET} --output-template-file sam.transformed.yaml $${AWS_PROFILE}

deploy: package
	if [ -n "$${AWS_PROFILE}" ]; then export AWS_PROFILE="--profile $${AWS_PROFILE}"; fi;\
	sam deploy --template-file sam.transformed.yaml --stack-name aws-compute-options-lambda --capabilities CAPABILITY_IAM $${AWS_PROFILE}

