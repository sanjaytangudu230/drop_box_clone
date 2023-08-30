Created a DropBox Clone using SpringBoot and AWS S3 as Storage

Steps to start the service:

 i) Go to project directory in command prompt and use "docker-compose up" for start AWS S3(minio docker image) and mysql
 
ii) Run "DropBoxCloneApplication" to start the SpringBoot microservice

We haved use JWT mechanism to authorize the request token. JWT will be given in the response to client while login & signup endpoints. Users should use that JWT token to do operations on their files. Users can't access the  
 files of the other users.
