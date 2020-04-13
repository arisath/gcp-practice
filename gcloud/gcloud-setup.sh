export PROJECT_ID=
gcloud config set core/project PROJECT_ID

gcloud components update
gcloud services enable vpcaccess.googleapis.com

### Create Memory Store Instance
gcloud redis instances create memstore \
--region us-central1 \
--zone us-central1-a \
--redis-version redis_4_0 \
 -network  default

gcloud compute networks vpc-access connectors create serverless-access \
--network default \
--region us-central1 \
--range 10.8.0.0/28

### Create Cloud Function
gcloud beta functions deploy function-1 \
--runtime python37 \
--trigger-http \
--region [REGION] \
--vpc-connector projects/[PROJECT_ID]/locations/[REGION]/connectors/[CONNECTOR_NAME] \
--set-env-vars REDISHOST=[REDIS_IP],REDISPORT=[REDIS_PORT]