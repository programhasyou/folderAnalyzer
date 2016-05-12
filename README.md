##outputs the result to the terminal
#####  /Users  
#####  Size: 41.837G  
#####  Folders and Files: 294798  
#####  Files: 243228  
#####  Folders: 51570  
#####  Analyzed ends in 00 hours, 00 minutes, 14 seconds, 25 milliseconds  

#### or

#####  Z:\  
#####  Size: 2129.111G  
#####  Folders and Files: 10585231  
#####  Files: 9573251  
#####  Folders: 1011980  
#####  Analyzed ends in 01 hours, 18 minutes, 56 seconds, 607 milliseconds  
***


#### * for all operating systems
#### * multithread 
#### * automatically adjusts the number of threads on the CPU load
#### * it has a very big advantage in speed over standard on huge volumes and remote network drives
***



#### it is possible to add windows on the right click: 
##### * open regedit
##### * go to HKEY_CLASSES_ROOT\directory\shell
##### * add new key in HKEY_CLASSES_ROOT\directory\shell with  name folderAnalyzer  and add Data "Open with folder Analyzer"
##### * add new key in HKEY_CLASSES_ROOT\directory\shell\folderAnalyzer with  name command  and add Data:   cmd.exe /s /k "java -jar C:\Users\user\Desktop\folderAnalyzer-1.0.jar %1"
##### * close regedit
***
 
 
#### run in terminal java -jar folderAnalyzer-1.0.jar Z:\







