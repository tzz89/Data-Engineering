# This repository is to document different data engineering technologies




## HBase
'H' stands for Hadoop/HDFS and 'BASE' refers to database
1. HBase is a KV store
2. The key is combination of row_key, column family, column, timestamp
3. Records cannot be updated
4. During table creation, only column family needs to be specific and not the column which can be added


## Connection to HBase
Connection to hbase is done via connectionFactory
User are responsible for closing the connection
There are 2 main classes for interacting with HBase
1. "Admin" (DDL) is related to creating the namespace, table, and setting the constraints on the namespace like the max table, size etc
2. "Table" (DML) is related to altering the table information

It is recommended to create 1 connection per application. This is usually done via a static connection around the entry class. when the entry class terminates, we will close the connection

## Hierachy
1. Namespace is a logica group of tables. It is analogous to 'database' in RDBMS. When creating a namespace via "Admin", we can specify the properties via KV using Admin.createNamespace(KV...)
   - TableQuota
   - SpaceQuota
   - Owner
   - Description
   - TTL
 - 