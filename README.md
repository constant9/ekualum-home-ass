# Kualum-home-ass  
You are part of a development team which has received a task from product  
to implement a new feature for the company’s data
ingestion pipeline.
The requirement is to write a program that will be part of the data preparation phase of the
pipeline.
The program will be written in Scala/Java, and will do the following:
1. Receive a path as it’s only argument  
2. Go over the path and for each file with .txt extension in the directory (including  
sub-folders), it will mark the current local time at the end of the file.  
3. The format example of the timestamp mark:  
```
################################
###### Kualum timestamp #######
####### 2020-08-26 14:21:59 #######
################################
```

4. The program should validate that the path is a valid directory
5. The program cannot use recursion in order to iterate over the files (in order to not waste
memory)
6. The output of the program should be “data preparation succeeds for folder
`<folder_name>`. Marked `<number of files>` files.” or “data preparation failed for folder
<folder_name>`”. If the program failed, the output should also contain the error that
occurred.
Extra Points:
1. Make the timestamp mark configurable instead of only supporting the mark provided in
this question.
2. Make the data preparation task configurable. Instead of always appending something at
the end of the file, allow the user to perform some other action on the file. For example,
move all the files to a directory called “`Kualum_<timestamp>`”  
Limitations:  
1. As specified in the requirements - recursion is not allowed in the code
2. You have 2 hours for the task
Please zip your solution and send it back to the interviewer.
Good Luck!


# Solution description  
## Design  
The solution app is based on spring boot framework.  
The app backend allows for different implementations of "File Processor" interface.  
Each processor implementation is a self sufficient package with configuration and logic. The interface acts as a facáde to this project.  
Selection of processor strategy is done by using spring's configuration.  
Implemented crawler is the "local file system crawler" - but other implementation could be supplied.  
The crawler has an internal worker pool - the "executor service".  
Each new folder discovered results in a "crawl task" added to the pool's embedded queue.  
Next crawl task will be picked by the worker in the pool and scanned for folders.  
The tasks queue makes the scan in BFS manner.  
After the folders are discovered and new crawl tasks created, files processing runs  
on all files of provided type in current folder.  
The provided "stamper processor" is reading a "stamp" file
  as provided in application config or by default - uses the embedded stamp file.

Running the app :  
Supply a single argument (root folder path) to the command line.
