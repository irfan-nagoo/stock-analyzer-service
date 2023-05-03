# stock-analyzer-service

This sample set of Microservices allows bulk injest of historcial stock data (CSV format) to the database (MongoDB) and provides a set of useful APIs with interesting stock analytics and trade information. The historical stock data can be gethered in the CSV format from sites like Yahoo Finance, Moneycontrol etc. Some sample stock data is also included in this project for illustration purpose.

This technology rice set of sample Microservices is a typical example of Event-Driven-Architecture. There are two Springboot Microservices in this project: data-producer-service (Frontend) and data-consumer-service (Backend).  These two services communiate with each other via REST and Async Message broker. The supported Async message brokers are: ActiveMQ, RabbitMQ and Kafka. These message brokers could be easily switched by changing a property in the properties file without any code change. The following set of REST APIs are supported:

    1. Single event feed         - This API accepts a single stock history record and inserts it in the database via a configured messageing broker.
    2. Bulk event feed           - This API accepts A set of dialy stock history records in multiple CSV files and uploads them to the database via a configured messaging broker.
    3. Async message broker feed - The applications can connect to configured Async message broker directly and send StockHistory events to the destination topic/queue.
    4. Delete API                - This API deletes the set of stock history records in a given time frame.
    5. List API                  - This paginated and sorted API returns the list of stock history records which could be used to render graphs and charts.
    6. StockAnalytics report     - This API runs the analytics on the stock history data in a given time frame and returns interesting analytical information.
    7. StockAnalysitis trade     - This API returns interesting trade information of the stock in given month by analyzing all avaialble stock historical data.
    
Technolgy Stack: Java 1.8, SpringBoot, SpringCloud, Spring Data JPA, Apache Common CSV, Lombok, ActiveMQ, RabbitMQ, Kafka, MongoDB and Junit 5.


Here is the Swagger url for various REST endpoints on local: http://localhost:8080/swagger-ui.html
  
