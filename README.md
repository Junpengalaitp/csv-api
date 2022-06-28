## Quick Start (Java 17+)
* start app: java -jar csv-api-project-1.0.jar
  * the app will run on port 8080
* test sample request:
  ```
  curl --location --request POST 'http://localhost:8080/api/v1/enrich' \
  --header 'Content-Type: text/csv' \
  --header 'Accept: text/csv' \
  --data-raw 'date,product_id,currency,price
  20160101,1,EUR, 10.0
  20160101,2,EUR, 20.1
  20160101,3,EUR, 30.342'
  ```
* 

# Design Doc
* input: a csv file, which comes from the request body according to the example
* data validation: check the data format, use Apache Commons Validator
* static product.csv: shouldn't be too large, load it to memory as a map when app starts.
* support large csv files:
  * the csv file is upload by the client by post request
  * use concurrency, and the easiest way is to use parallel stream
  * if the csv is extremely large(10GiB+), then use an api maybe not a good idea, should upload the csv to the file system, and use file processing engine such as Spark to handle it

