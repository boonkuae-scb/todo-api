## Todo List Api


This is a path one of todo application. In this path is focusing on Api and Integrate with LINE Message API.
  
   ![QRCode](docs/todo-qr-code.png "qr code chat")

### Prerequisite

1. LINE Message API account from [https://developers.line.me/en/](https://developers.line.me/en/).The parameters that we need from this step as below.
     * `channel-token` . A channel token of LINE bot account.
     * `channel-secret`. A channel secret of LINE bot account.
2. LINE Web login account from [https://developers.line.me/en/](https://developers.line.me/en/).The parameters that we need from this step as below.
     * `token-url` . A requests an access token endpoint LINE Web login account.Set this value to `https://api.line.me/oauth2/v2.1/token`
     * `channel-id`. A channel id of LINE Web login account.
     * `channel-secret`. A channel secret of LINE Web login account.
     * `grant-type`. A channel grant-type of LINE Web login account. use `authorization_code`
3. MONGODB from [https://mlab.com](https://mlab.com): 
     We used mongodb to stored date of the Todo Application. I suggest MLab to for mongodb.
     * `mongodb_uri` : Mongodb endpoint. 

4. Todo Frontend:
    I create a sample frontend to manage todo list and also deploy this application to the Heroku. 
    * `frontend-url`. The frontend of todo application. Use `https://line-todo-manage.herokuapp.com/`
    * `redirect-uri`. Redirect URL endpoint that LINE will redirect to after logged-in successful.use `https://line-todo-manage.herokuapp.com/auth`
    
5. To integration test with LINE Bot is required `HTTPS` So, we use **Narok** is handling `HTTPS` on local development environment
    * Register Ngrok Account : [https://dashboard.ngrok.com/user/signup](https://dashboard.ngrok.com/user/signup)
    * Download Ngrok application: [https://ngrok.com/download](https://ngrok.com/download)
    * Ngrok Manual: [https://ngrok.com/docs](https://ngrok.com/docs)
### Sep up Development environment
A step by step series of examples that tell you how to get a development env running
##### 1. Down load a source code
```git
git clone git@github.com:boonkuaeb/todo-api.git
```
##### 2. Edit application configuration files
Open the `src/main/resources/application.yml` with your favorite editor.
Replace contain of `application.yml` file as a table below.

| Parameter Name        | Definition|
| ------------- |:-------------:|
| ${DEVTOOLS_IS_DEV_MODE}      |  `true` for development env, `false` for production environment |
| ${MONGODB_URI}      |  Replace this value as a `mongodb_uri` from the prerequisite  step 3|
| ${LINE_BOT_CHANNEL_TOKEN}      | Replace this value as a `chanel-token` from the prerequisite  step 1 |
| ${LINE_BOT_CHANNEL_SECRET} | Replace this value as a `chanel-secret` from the prerequisite  step 1    |
| ${LINE_WEBAPP_URL} | Replace this value as a `frontend-url` from the prerequisite  step 4    |
| ${LINE_WEBAPP_TOKEN_URL} | Replace this value as a `token-url` from the prerequisite  step 2     |
| ${LINE_WEBAPP_CHANNEL_ID} | Replace this value as a `channel-id` from the prerequisite  step 2     |
| ${LINE_WEBAPP_CHANNEL_SECRET} | Replace this value as a `channel-secret` from the prerequisite  step 2     |
| ${LINE_WEBAPP_GRANT_TYPE} | Replace this value as a `grant-type` from the prerequisite  step 2     |
| ${LINE_WEBAPP_GRANT_TYPE} | Replace this value as a `redirect-uri` from the prerequisite  step 4     |

##### 3. Unit Test
```bash
mvn clean test
```

##### 4. End to End Test
1. Prepare `HTTPS`
    * Run `./ngrok http 8080`
    * Find the `https` url that Ngrok generated. For example `https://random-url-todo-api.ngrok.io`
2. Update LINE Bot a Webhook URL
    * Go to [https://developers.line.biz/console/](https://developers.line.biz/console/)
    * Select the provider and chanel that registered form Prerequisite step
    * Select Channel Tab.
    * Update the Webhook URL with pattern `https url` + `/callback`. In this case, I use `https://random-url-todo-api.ngrok.io/callback`
   
3. Add The LINE channel to your LINE mobile application 
    * Go to [https://developers.line.biz/console/](https://developers.line.biz/console/)
    * Select the chanel that registered from the Prerequisite step
    * Select Channel Tab.
    * Find the QR code image
    * Open your LINE mobile application. 
    * Add new friend via scan QR code.
    * Check a Friends list on LINE application. You will see new friend name `My Todo App Bot`
    
4. Test Chanel
    * Chat to `My Todo App Bot`
    * Input message text : `Buy milk : 3/5/18 : 13:00`
    * Result: 
    
      ![Chat](docs/todo-chat.png "Test chat" )


### Create Docker Images.

1. Make sure all Unit Test passed properly. 
    ```$docker
    mvn clean test
    ```
2. Create docker image
    ```$docker
    mvn package -DskipTests=true docker:build -Dtodo-profile=default
    ```
    Check `todo-default-img` created.
      ```docker
      docker images
      ```
      Find docker image name as  `todo-default-img`
      
3. Run container from docker image
    ```docker
     docker run --name todo-default -p8080:8080 -e DEVTOOLS_IS_DEV_MODE=false -e MONGODB_URI=your_mongodb_uri -e LINE_WEBAPP_URL=your_line_webapp_url -e LINE_BOT_CHANNEL_TOKEN=your_line_bot_channel_token -e LINE_BOT_CHANNEL_SECRET=your_line_bot_channel_secret -e LINE_WEBAPP_TOKEN_URL=your_line_webapp_token_url -e LINE_WEBAPP_CHANNEL_ID=your_line_webapp_channel_id -e LINE_WEBAPP_CHANNEL_SECRET=your_line_webapp_channel_secret -e LINE_WEBAPP_GRANT_TYPE=your_line_webapp_grant_type -e LINE_WEBAPP_REDIRECT_URI=your_line_webapp_redirect_uri -d todo-img-default
    ```
    _The values of the parameters are decent on your environment configuration._
4. Test the docker container is running

   Open URL [http://localhost:8080](http://localhost:8080) and check the page is showing `Whitelabel Error Page` message. That means we can run the application from docker container properly.


5. Store docker images to the Docker repository.
    I use [https://hub.docker.com/](https://hub.docker.com/). 
    * Open the terminal and login to docker-hub account as this step.
        ```docker
        docker login
        ```
        Input user/password of docker-hub account.
    * Check `todo-default-img` created.
        ```docker
        docker images
        ```
      Find the Image Id of `todo-default-img` image.
      For example I use `todo-default-image-id-xxx` as a Image Id of `todo-default-img`
        
    * Tags  the  `todo-default-image-id-xxx` Image Id from previous step.
        * `boonkuaeb/todo:latest` is Docker Hub repository name and version
        
        Run command:
        ```docker
          docker tag todo-default-image-id-xxx boonkuaeb/todo:latest
        ```
        
    * Push `boonkuaeb/todo:latest` image to Docker-Hub repository
      ```docker
      docker push boonkuaeb/todo
      ```
      Open [https://hub.docker.com/](https://hub.docker.com/) and find the boonkuaeb/todo that just pushed.
      
### Deploy the Docker Images.      
In this case, I plan to test deployment todo application on my local environment. So, I will clean up all container and images as a few dockers command below.

```docker
docker stop todo-default
```

```docker
docker rm todo-default
```


```docker
docker rmi todo-default-img
```

**Deployment step**
1. Pull and deploy docker image :

    The deployment command is very similar to step 3 of `Create Docker Images` step. The different things are. 
    * `-d ` is a image name : change to Docker Hub repository  name as a `boonkuaeb/todo`
    * `--name` is a container name: change to `todo-prod`
    * `DEVTOOLS_IS_DEV_MODE=false` 
    
    The command to deploy as a below.
    ```docker
    docker run --name todo-prod -p8080:8080 -e DEVTOOLS_IS_DEV_MODE=false -e MONGODB_URI=your_mongodb_uri -e LINE_WEBAPP_URL=your_line_webapp_url -e LINE_BOT_CHANNEL_TOKEN=your_line_bot_channel_token -e LINE_BOT_CHANNEL_SECRET=your_line_bot_channel_secret -e LINE_WEBAPP_TOKEN_URL=your_line_webapp_token_url -e LINE_WEBAPP_CHANNEL_ID=your_line_webapp_channel_id -e LINE_WEBAPP_CHANNEL_SECRET=your_line_webapp_channel_secret -e LINE_WEBAPP_GRANT_TYPE=your_line_webapp_grant_type -e LINE_WEBAPP_REDIRECT_URI=your_line_webapp_redirect_uri -d boonkuaeb/todo
    ```
2. Check the container is running :
    ```docker
    docker ps
    ```
    Find name container as `todo-prod` is running.
    
3. Check application is work:

    Open hosts file as a command `sudo vim /etc/hosts` add new line `127.0.0.1 todo-prod` and save the file.
    
    Open URL `http://todo-prod:8080` and check the page is showing Whitelabel Error Page message. That means we can run the application on docker container properly.
    

