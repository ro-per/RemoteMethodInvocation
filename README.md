# RMI-based Chat System
## Table of contents
* [Authors](#Authors)
* [Assignment description](#Assignment-description)
* [General info](#General-info)
* [Features](#Features)
* [Screenshots](#Screenshots)
* [Info](#Info)

## Authors
* Romeo Permentier
* Nick Braeckman

## Assignment description
Implement a simple chat-system with following features:
* Server: is responsible for communication between users and keeps track of online users.
* Client: asks a username on startup. (Duplicate usernames are forbidden and therefore blocked)
* Private chat: chatting with someone who is online in the room must be possible.


## Features
### Private chat
* send private messages to 1 person at a time
* reading all the private messages, coming from all private users
* show received private messages after reopening the chat system
### Public chat
* click on a username to open the private chat and send messages to that user
### Login screen
* choose a username and chat server

## Run the chat system

1) Download the jar's

2) Execute the jar's in the following order:

    * Server:
    ``` java -jar Server.jar <portnumber>```

    * Client: 
    ``` java -jar Client.jar```

## Clone the project

1. Clone project from github

2. Import project 
   *  Project JDK: 1.8.0_241
   *  Project Language Level: 8
   
3. Setup 2 modules:
   * Client Module
   * Server Module

4. Run configurations:
   * Program arguments Server: ```<portnumber>```

## Screenshots
![Login screen](Screenshots/screen_login.png?raw=true "Login Screen")
![Screenshot 1](Screenshots/screen1.png?raw=true "Screenshot 1")
![Screenshot 2](Screenshots/screen1.png?raw=true "Screenshot 2")
![Screenshot 3](Screenshots/screen1.png?raw=true "Screenshot 3")
![Screenshot 4](Screenshots/screen1.png?raw=true "Screenshot 4")
![Screenshot 5](Screenshots/screen1.png?raw=true "Screenshot 5")

## More Info
* [Java RMI](https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html)
