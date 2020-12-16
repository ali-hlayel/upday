# upday
This is the challenge project of Ali Hlayel, created by the requirements defined in the sent challenge. 
It contains 1 microservice which is implemnted by me. I used IntelliJ IDE for development. Infrastructure is created over Spring Boot, Hibernate, h2 database(dev purpose) and RESTFul services.

# Article Service
Article microservice is built as RESTFul API. it contains methods about article details: 

One is for retrieving all articles. 
Second update article. 
Third one create a new article.
Forth one git a list of articles for a certain period. 
Fifth one is to delete an article. 
Sixth one is to get all articles for a specific authors.
And last one is to retrieve all articles based on keyword.
        
# Getting started
The article-service is built using docker. The root container contains the Dockerfile which java 11 container and copy the articleService jar files inside. 
Also the docker-compose.yml describes the configuration of service. The components will be run in a local environment.
To run the service execute execute ./build-all.sh shell script in the root directory of project in the terminal.
# Test
The code is already tested using Junit test. you can find the tests for the controller and the article serviceImpl.
I also used swagger API V3 for testing the project at the run time process. 
you can test the service using swagger by entering the following link:http://localhost:8080/swagger-ui/index.html?url=/v3/api-docs

As shown in the attached figure, I built 7 controllers endpoints. 

# Endpoints
The following pictures shows the test results for the service using swagger.
As shown in the in Fig-1. The article controller contains form 7 Api's.

<img width="1427" alt="Screenshot 2020-09-20 at 20 49 41" src="https://user-images.githubusercontent.com/68303228/102285103-d4be7400-3f35-11eb-8571-b457c933d5b3.png">

1. Create new article. 
URL : "http://localhost:8080/article-service/article" 
Method: POST Hint: Article may has more than one author or keyword. 
Json entity example: 
{
                       "header": "Covaid-19 update",
                       "shortDescription": "Covaid-19 status update",
                       "text": "This is a sample text for an article",
                       "publishDate": "2021-01-01",
                       "authors": [
                         {
                           "firstName": "Ali",
                           "lastName": "Hlayel"
                         }
                       ],
                       "keywords": [
                         {
                           "keyword": "COVAID-19"
                         }
                       ]
                     }
Swagger example:
<img width="1427" alt="Screenshot 2020-09-20 at 20 49 41" src="https://user-images.githubusercontent.com/68303228/102287110-00435d80-3f3a-11eb-9160-df3a2f0b7f2e.png">

2. Get all articles: 
URL : "http://localhost:8080/article-service/articles?limit=10" 
Method: Get 
Swagger example:
<img width="1427" alt="Screenshot 2020-09-20 at 20 49 41" src="https://user-images.githubusercontent.com/68303228/102287340-865fa400-3f3a-11eb-8af5-5ccb58cac6ec.png">

3. Get all articles for a certain period.
URL: http://localhost:8080/article-service/articles/between?publishDateFrom=2020-01-01&publishDateTo=2021-01-01
Method: Get

Swagger example:
<img width="1427" alt="Screenshot 2020-09-20 at 20 49 41" src="https://user-images.githubusercontent.com/68303228/102287547-ff5efb80-3f3a-11eb-8cda-7af74289b037.png">

