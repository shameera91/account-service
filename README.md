# Account-Service

Docker is required to be installed on the machine.

# Installation

1. Clone the Repo & run the following commands.
    
    `./gradlew build -x test`
     
     initially we use this -x test command for skipping the integration tests being executed. Reason for that is, at that time no rabbitmq or postgresql container running in our machine. If we can run those two containers at first ,we can execute the tests also.

    `docker-compose up`
    
    this command will spin up a postgres container, rabbitmq container and another container for spring boot application exposing port 8080.
    
    Since we inculde the schema.sql in our spring boot applcation classpath, database and table structure automatically initialized.
