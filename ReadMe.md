# Backend application:<br>
An application for business logic and data providing services to the frontend. This class provides data using Rest CRUD to the frontend and is written in a non-blocking fashion. A simulating controller(```SimulatingExternalServiceController.java```) is also implemented which just provides a HTTP status changing behavior.
<br>It provides 4 endpoints namely: ```/services/add```, ```/services/update```, ```/services/removebyid/{id}```, ```/services/getAllServices```.
<br>On add and update events the URL is also checked if its a valid one or not and the service is marked accordingly. The timeout for these validation can also be configured using ```request.timeout``` property(in seconds) in ```application.properties``` file.<br/>
<br>If it is required to create the jar file use ```mvn clean package``` in the project directory. Use ```mvn clean package -DskipTests``` to make jar without running tests. The program can be run using ```java -jar target/kry_task-0.0.1-SNAPSHOT.jar```.
<br/><br/>

<br>If needed to run in IDEs such as Intellij Idea, all the projects can be used using the 'Application' run configuration and pointing to the classes with ```@SpringBootApplication``` annotation namely: ```com.task.service_state_poller.ServiceStatePollerApplication.java``` for poller service, ```com.task.backend.BackendApplication``` for backend application and ```com.kry.WebApp.Application``` for web application.<br/>
