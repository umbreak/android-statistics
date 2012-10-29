android-statistics
==================
## Short Description
 * Android Client Side: Display statistics for Android using AchartEngine.
Charts are displayed in a timeline fashion, with zooming and scaling capabilities.
 * Tomcat+Hibernate+MySQL Server Side: REST Web Service that provides an API for the statistcal displaying system. It provides different algorithm for Data Set reduction for adapting the requested chart to the Mobile resolution.
 
## Android Client Side
The client side is in charge of the interaction with the REST Web Service for GET, POST, UPDATE and DELETE the resources available. 

[This figure](http://img341.imageshack.us/img341/7803/clasesrelationshipschem.png) shows the relationship between the classes, with the variables that they exchange. Each color represents a group of classes that are wrapped in the same activity and have the same lifecycle. 
 
## Java REST API
Interacts with the Database and the client application. Translate the client’s request to a proper SQL request, process the SQL response and delivers it, in an optimized way, to the client’s application. These interactions with the Database are performed using Hibernate. 

The Server provides an API to develop different client applications. The API methods provided are available [here (WADL)](https://github.com/downloads/umbreak/android-statistics/application.wadl) and [here (table)](http://www.slideshare.net/DidacMontero/rest-api).

Information related with each class and the relationship between them, can be found [here](http://img836.imageshack.us/img836/2112/serverclassscheme.png)

## Presentation and .apk

More information can be found in the [project presentation](http://www.slideshare.net/DidacMontero/master-thesis-presentation-14655682).
 
The App can be downloaded from the [download section](https://github.com/umbreak/android-statistics/downloads) or directly from the [QR code](https://github.com/umbreak/android-statistics/TU-Charts%20App.apk/qr_code)
