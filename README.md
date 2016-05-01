#определяет обьем и количество папопок и файлов в директории

для всех операционных систем

многопоточный 

автоматически регулирует количество потоков по загрузке процессора

имеет очень большое преимущество в скорости работы перед стандартными на больших, огромных объемах и удаленных сетевых дисках

имеется возможность в windows добавить на правую кнопку 
* open regedit
* go to HKEY_CLASSES_ROOT\directory\shell
* add new key in HKEY_CLASSES_ROOT\directory\shell with  name folderAnalyzer  and add Data "Open with folder Analyzer"
* add new key in HKEY_CLASSES_ROOT\directory\shell\folderAnalyzer with  name command  and add Data:   cmd.exe /s /k "java -jar C:\Users\user\Desktop\folderAnalyzer-1.0.jar %1"
* close regedit

запускается в консоли java -jar folderAnalyzer-1.0.jar Z:\

выводит результат в консоль вида

/Users
 Size: 41.837G\n
 Folders and Files: 294798\n
 Files: 243228\n
 Folders: 51570\n
 Analyzed ends in 00 hours, 00 minutes, 14 seconds, 25 milliseconds



