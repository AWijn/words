***HOW TO RUN***

1) Docker file in wordgames directory
-> do a docker-compose up here to get the postgres database up and running

2) spring boot application
-> do a mvn spring-boot:run


***TO SEND FILE***

POSTMAN COLLECTION OR CURL

POSTMAN:
https://www.getpostman.com/collections/8b54d6585befd16165b7

postText: post plain text to application, which saves it in the database and gives you the word combinations.
cleanUp: delete all saved text from database
getTexts: get all texts in database and their ids
getWordsById: get word combinations of text from database with specific id


LINUX
curl --request POST 'http://localhost:8080/wordgames/file' --data '@input.txt' --header "Content-Type: text/plain"
curl --request POST 'http://localhost:8080/api/file' --data '@input.txt' --header "Content-Type: text/plain" >> output.txt

WINDOWS
curl --request POST "http://localhost:8080/wordgames/file" --data-binary "@input.txt" --header "Content-Type: text/plain"
curl --request POST "http://localhost:8080/wordgames/file" --data-binary "@input.txt" --header "Content-Type: text/plain" >> output.txt



