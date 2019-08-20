# Zendesk Search
A simple command line application to interactively search Zendesk ticket, user and organization data 
supplied in json format.

## Building

The code is written in Scala and requires
* JDK 11 
* Scala 2.13

To build run
```
sbt assembly
```

Unit tests can be run via
```
sbt test
```
and integration tests via
```
sbt it:test
```
Note that the integration tests include a performance test that can run for quite a long time.

## Usage

To run the app use ```bin/zendesk-search.sh``` or ```bin/zendesk-search.bat``` from the project directory

```bin/zendesk-search.sh --help``` will display the command line options

When the app runs it will display help with some examples


## Notes
### Assumptions
* Data schemas are the same as the sample file provided.  No need to cater for data arriving in a different format
* Ids for org and user are always integers & don't exceed integer max value of 2147483647
* Ids are unique across data set
* Example data is representative of the required and optional fields
* Fields that are collections (e.g tags) will be searched by the elements of the collection
  i.e. a tag field will match if any tag in the set is the same as the search term


### Tradeoffs
* To speed up the search, all the fields of the entities are indexed.  This greatly increases speed
but also increases memory usage

* There is a limit set on the number of results that will be shown with their related entities (e.g. the users & tickets of an org).  
This is to prevent the search time scaling linearly when the search result set is large

* All fields are searched via exact match

### Known Errors
* The SearchPerformanceIT integration test is currently unreliable. Performance with the indexes is much better than
the doing a brute force search, but there is still some factor causing near linear scaling 
with bigger data sets

* If data does not include all the required fields then the app will error while parsing the file.  
This can be fixed by changing the appropriate field in the case class to Option[X] and updating the get function 
in the corresponding SearchableField (also should update the output in the ConsoleWriter class)  

### Possible Improvements
* Better error handling of file parsing errors

* Support fuzzy matching on some types of fields (e.g. datetimes could match on just the date part)


