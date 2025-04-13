
🧑‍💼 User Management Service
A Spring Boot microservice for managing users with JWT-based authentication, event-driven logging via Kafka, and persistence using MySQL. Designed with clean separation of concerns and built to run containerized with Docker.

🚀 Features
🔐 JWT-based Authentication (Login Endpoint)

🧑 User CRUD (Create, Read, Update, Delete)

📤 Kafka Producer (on user activity)

📥 Kafka Consumer + Journal Logger

🐬 MySQL for persistent storage

🐳 Dockerized for easy deployment

📦 Tech Stack
Java 17, Spring Boot 3.4.4

Spring Security, JWT

Apache Kafka (Producer/Consumer)

MySQL (via Spring Data JPA)

Docker & docker-compose

Maven for build and dependency management


How to start application: 
I have included a shell script to start the application which will take care of the background process
- Clone the repository
- open the docker
- In project directory run ./start.sh


API's and their responses:


1. http://localhost:8080//apica/user/register
   - Request type : POST
   - Paylod sample: {
    "username": "john_doe",
    "password": "securePassword123",
    "email": "john.doe@example.com",
    "roles": [
        "USER",
        "ADMIN"
    ]
}
sample response: {
    "id": 2,
    "username": "john_doe",
    "email": "john.doe@example.com",
    "roles": [
        "ADMIN",
        "USER"
    ]
}
This api is available for public users and can be accessible without a JWT token.
I have used JWT tokens for authentication and authorization
so first we have to create JWT with the credentials that we have registered

AuthController:
 http://localhost:8080/auth/login
  - Request Type : POST
  - Sample Payload: {"username" : "john_doe","password" : "securePassword123"}
  - Sample response: {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWhpbCIsImlhdCI6MTc0NDU4MTQ0MSwiZXhwIjoxNzQ0NjE3NDQxfQ.w_1R1GfUOpRy9inhPxnOCyctm-Zq63R9LXnbeAHyM6I"
}


So, with the JWT token generated we can be able to access all other endpoints

GET /apica/user/get/{id}
Description: Get user by ID.
Path Parameter:
id (Long) — User ID
Response:
200 OK
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com"
}


PUT /apica/user/update/{id}
Description: Update a user by ID.

Path Parameter:

id (Long)

Request Body:
{
  "username": "john_doe_updated",
  "email": "john_updated@example.com",
  "password": "newPassword"
}
Response:

200 OK

json
Copy
Edit
{
  "id": 1,
  "username": "john_doe_updated",
  "email": "john_updated@example.com"
}

DELETE /apica/user/delete/{id}
Description: Delete user by ID.

Path Parameter:

id (Long)

Response:

204 No Content


Consumer API:
Base Path: /apica/journal
🔐 Authentication Required:
All endpoints require a valid JWT token in the Authorization header (format: Bearer <token>).

📥 GET /events/{username}
Description:
Fetches all journal events for the given username. Only the authenticated user can access their own events.

URL:
GET /apica/journal/events/{username}

Path Parameter:

Name	Type	Description
username	String	Username to fetch events for
Headers:

Header	Value Example	Description
Authorization	Bearer eyJhbGciOiJIUzI1NiIsInR5cCI...	JWT token for auth
Response:
[
  {
    "id": 1,
    "username": "john_doe",
    "eventType": "USER_CREATED",
    "description": "User john_doe registered successfully",
    "timestamp": "2024-04-14T13:45:30.123Z"
  },
  ...
]
Success Status Code: 200 OK

Error Responses:

403 Forbidden – If the authenticated user tries to access someone else’s journal.

401 Unauthorized – If no valid token is provided.
