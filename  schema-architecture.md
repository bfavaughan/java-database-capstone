This Spring Boot application uses both MVC and REST controllers. 

Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. 

The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.

1. User accesses AdminDashboard or DoctorDashboard pages.
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer for information and being the heart of the system gathers information from the repositries and sends it back to the presentation layer
4. There are two databases at the repository layer, one relational to handle patient and doctor records which we will be using MySQL, a second one for Prescriptions will be using MongoDB. This layer will handle the business logic.
5. Each repository will interact with each database store, by utilising both Relational and NoSQL databases, we are leveraging the strength of both Structured and Unstructured databases.
6. We are utilising Model Binding so that once data is received from the database they are mapped into Java Model Classes that the controller can easily work with.
7. Finally, the bound models are used directly in the application layer. In the MVC flow Thymeleaf will be displaying the data to the user and the data will also be available and ready for the REST API to query.