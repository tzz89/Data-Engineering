

## Key concepts
1. Dimension are attributes of any entity
2. There are 2 types of dimension
   1. Slowly-Changing
   2. Fixed
3. There are 3 type of data modelling
   1. OLTP: Mostly for Software engineers (for single row)
   2. OLAP: Mostly for data engineer (for entire population)
   3. Master Data: Optmized for completeness of entity definition, deduped
4. Common data modelling pattern 
   1. production database -> Master Data (joined many DB) -> OLAP cubes -> Metrics
5. Idempotent pipeline: Produces the same result regardless of when it is ran
6. Dimensions can be additive (age) or non additive (users on web, users on android. They can have overlap)
7. Enums are good for low to medium cardinality (eg < 40)
   1. Built in data quality
   2. Built in static fields
   3. Built in documentation
8. Flexible Schemas (eg other_properties column)
   1. Having a Map column / JSON column
   2. Poor space utilization as the 'header' is repeated
9. Graph Modeling 
   1.  Nodes usually have below schema
       1. Identifier: String
       2. Type String 
       3. Properties: MAP<String, String>
    2. Relationship usually have below schema   
       1. edge_id (optional)
       2. subject_identifier String (src)
       3. subject_type Vertex_Type
       4. object_identifier: String (dst)
       5. Object_type: Vertex_Type
       6. Edge_type: Edge_type
       7. properties: MAP<String, String>


## DataTypes
1. Struct: Rigidly Defined, values can be any type (compression is good)
2. Map: Keys are loosely defined, values all have to be same type
3. Array List of values all have to be the same type

## File storage type
1. Parquet
   1. One of the most important compression in is run length encoding but shuffling in spark will mess with the sorting.
   2. if we keep the temporal columns in an array, the ordering will be maintained in the array
   

## Cumulative Table design
1. Holding on to history
2. Uses yesterday and today's value and full outer join the 2 dataframe together and coalesce the rows
3. Usages: Growth analytics (eg dim all users)
4. State transition tracking
5. We can prune the table (eg when the time since last activity > X days)

Advantage: dont need to use group by (no shuffle)
Disadvantage: Huge size, Backfilling have to be day by day and not parallel

## Slowly Changing dimension design
1. Latest snapshot (Overwrite historical - pipeline will not be idempotent)
2. Daily/Monthly/Yearly snapshot
3. SCD types 0,1,2,3
   1. Type 0 Not changing (eg birth date) 
   2. Type 1 Only care about the latest value 
   3. Type 2 current values either end_date that is null/9999-12-31 (Recommended)
      1. Hard to use as there are more 1 row per dimension
      2. type 2 is Idempotent
   4. Type 3 Only care about "original" and "current"
      1. cannot capture the history when dimension are changed more than once
   
Loading of type 2 tables can be done on the entire history or incrementally

### Way 1 (DO NOT USE - will not work if there are repeated streaks)
Advantage: Can be parallelized during backfill
Disadvantage: Process historical data
[Way 1 SQL](SQL/Slow-changing-dimension_way_1.sql)

| player\_name | is\_active | scoring\_class | start\_season | end\_season |
| :--- | :--- | :--- | :--- | :--- |
| Aaron Brooks | true | bad | 2012 | 2013 |
| Aaron Brooks | true | bad | 2016 | 2018 |
| Aaron Brooks | true | average | 2014 | 2015 |
| Aaron Brooks | false | average | 2011 | 2011 |

### Way 2

We union historical record, unchanged records from last season, changed records (2 rows) and new records
Disadvantage: Difficult to backfill
Advantage: Process only the incremental data
[Way 2 SQL](SQL/Slow-changing-dimension_way_2.sql)




## Idempotent best practice
1. Use Merge/Insert Overwrite instead of insert into
2. Using start_date > XXX without end_date < XXX
3. Not using partition markers / markers
4. Not using depends_on_past for cumulative pipeline (cannot parallize the backfill)


## Common Pitfalls
1. Always consider the possibility of null when filtering
2. 