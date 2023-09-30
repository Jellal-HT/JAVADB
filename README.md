# MYDB

JAVADB is a robust and efficient relational database system using Java. Some principles are referenced from MySQL, PostgreSQL and SQLite  and incorporating the following key functionalities:

- Reliable Data Storage and Recovery: Implemented a robust storage mechanism for secure data persistence and ensured seamless data         
  recovery in case of system failures or crashes.

- Transaction Management: Implemented a serializable scheduling sequence based on the two-phase locking protocol to ensure data integrity 
  and concurrency control. Employed the Multiversion Concurrency Control (MVCC) technique to eliminate read/write locks, allowing for efficient parallel execution of transactions. Supported multiple transaction isolation levels, including read committed and repeatable read.

- SQL Parser and Management: Developed a comprehensive SQL statement parsing module that handles basic SQL commands. Implemented efficient 
  field management, table management, and index management functionalities, enabling smooth data manipulation operations.

- Socket-based Communication: Established a secure and efficient communication channel between the server and the client using Java 
  Socket programming. 

## Execution Method

Please note that you need to adjust the compilation version in the pom.xml file first. If you are importing it into an IDE, change the project's compilation version to match your JDK.

To begin, execute the following command to compile the source code

```shell
mvn compile
```

Next, execute the following command to create a database with /tmp/mydb as the path:

```shell
mvn exec:java -Dexec.mainClass="top.yichenghu.javadb.backend.Launcher" -Dexec.args="-create C:\Users\ychu3\JAVADB\tmp\javadb"
```

Subsequently, start the database service with default parameters using the following command:

```shell
mvn exec:java -Dexec.mainClass="top.yichenghu.javadb.backend.Launcher" -Dexec.args="-open /tmp/mydb"
```

At this point, the database service is already running on the local machine's port 8888. Restart another terminal and execute the following command to start the client to connect to the database：

```shell
mvn exec:java -Dexec.mainClass="top.yichenghu.javadb.client.Launcher"
```

This will initiate an interactive command line where you can input SQL-like syntax; pressing Enter will send the statements to the service and display the executed results.

