This is a demo for https://github.com/GoogleCloudPlatform/spring-cloud-gcp/issues/1987

To run:
1. Get Google Cloud Spanner Emulator running: https://github.com/GoogleCloudPlatform/cloud-spanner-emulator
2. Create the database and instance for the demo (see application.yml):
    - project-id: demo
    - instance-id: demo-instance
    - database: demo-db

    after correctly setting up gcloud to use the emulator (see links above)
    ```bash
    gcloud config set project demo
    gcloud spanner instances create demo-instance --config=<your gcloud config name> --description="My Instance" --nodes=1
    ```
3. Run the example and view the stdout
    ```bash
    mvn spring-boot:run
    ```