# EasyShare

This application allows users who are in one network send and receive messages from each other. 
To get started, download Server.exe and Client.exe from this repository and run Server.exe. 
If it succeeds, the message "Server started on ip <your_ip> port 8081" should appear. 
Now one can run Client.exe on every mashine that is able to connect to machine where server is running.

## Usage
Ctrl-Q to copy, Ctrl-B to paste by default. Settings can be changed in "settings" menu in the client. note that using Ctrl-V 
and Ctrl-C, as well as any other combination that are used by the system, is STRONGLY not recommended. 
On the server side, one can make "black list" of ip addresses that cannot connect to this server. These settings are stored in file "allowed.txt"
Client and server also have log files, where information about revealed server/connected clients is written.
