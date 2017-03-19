# java-unity-websocket-connector
The purpose of this project is to prototype out connecting a Unity3d project to a backend Java server via websockets.  It is licensed under the Apache License, Version 2.0 (the "License")

#### Note: This uses the websocket-sharp library found here: https://github.com/sta/websocket-sharp  
* This is used as a compiled dll at unity-client/Assets/websocket-sharp.dll but it may need to be recompiled on your system.

The backend Java server is a maven project and can be run with <code>mvn clean jetty:run</code> It is currently set to run on port 11037. It uses Spring Security to authenticate connecting clients.

Currently a user is able to start the Unity3d program; enter in the server ip, username, and password; and log in.  If it successfully connects to the backend server, it will request the user's friendslist from the server, and display it when recieved.

#### Note: User information can be found hardcoded in unity-websocket-server/src/main/java/com/logiclodge/unitywebsocketserver/user/UserDao.java

This has been tested (and works) on the following Unity platforms
* Windows
* Android

#### TODO list:
* Implement 'ADDFRIEND' request/response
* Unit Testing
* Propper error handling both clientside and serverside.
* Figure out a way to tell that the username/password is incorrect clientside (currently just fails with a netork error)

#### DISCLAIMER: I am a Java programmer so some of the Unity C# techniques may seem odd.
