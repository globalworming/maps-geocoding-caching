# Coordinates Cache Service 

Small service that caches google maps geocoding results (coordinates and placeId). When using the google maps api, you are allowed to cache some results for up to 30 days. https://cloud.google.com/maps-platform/terms/maps-service-terms

Just deploy this application somewhere and send your geocoding requests there to store the coordinates in compliance to the google terms of service.

## A simple demo
Displaying maps with pins for specific locations is a common use case. Just put your [maps geocoding API key](https://developers.google.com/maps/documentation/geocoding/get-api-key) in the `application.properties` and use `mvn spring-boot:run` to start the application. check out the map at http://localhost:8080 and edit the locations.

![Screenshot_20200831_150725.png](Screenshot_20200831_150725.png)
 
 
 
