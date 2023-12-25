====================================================================
                    INSTALLATION REQUIREMENTS
====================================================================
- JDK 17
- Maven  v3.9.2

====================================================================
                   HOW TO RUN THE APPLICATION ?
====================================================================
-> the application runs in the 8080 port.

====================================================================
                             EXERCISE
====================================================================
- Create a CRUD for Movie.
- Create unitary tests for MovieService.
- Create an integration test to get a movie.

====================================================================

_____________________________________________________________________

# Movie API Documentation

## Overview

This API provides endpoints to manage movie information, including retrieving, creating, updating, and deleting movies.

To add information on how to run the API in your README, you can provide clear instructions for developers to follow. Below is an example template you can use:

```markdown
# Movie API Documentation

## Overview

This API provides endpoints to manage movie information, including retrieving, creating, updating, and deleting movies.

...

## Running the API

Follow the steps below to run the Movie API locally:

### Prerequisites

Make sure you have the following software installed on your machine:

-- JDK 17
- Maven  v3.9.2

### Clone the Repository

### Build the Project

```bash
mvn clean install
```

### Run the API

```bash
mvn spring-boot:run
```

The API will be accessible at [http://localhost:8080](http://localhost:8080).

## Endpoints

### Get All Movies

- **Endpoint:** `GET /movies`
- **Description:** Retrieves all movies stored in the system.
- **Response:**
    - 200 OK: Returns a list of movies.
    - 204 No Content: No movies are available.

### Get a Movie by ID

- **Endpoint:** `GET /movies/{id}`
- **Description:** Retrieves a specific movie based on the provided ID.
- **Request Parameters:**
    - `id` (type: Long) - The unique ID of the movie to retrieve.
- **Response:**
    - 200 OK: Returns the movie with the specified ID.
    - 404 Not Found: No movie is found with the provided ID.

### Create a New Movie

- **Endpoint:** `POST /movies`
- **Description:** Creates a new movie in the system.
- **Request Body:**
  ```json
  {
    "title": "Movie Title",
    "director": "Movie Director",
    "year": 2022
  }
  ```
- **Response:**
    - 201 Created: Returns the newly created movie.
    - 400 Bad Request: The request is incorrect or incomplete.

### Update a Movie by ID

- **Endpoint:** `PUT /movies/{id}`
- **Description:** Updates a specific movie based on the provided ID.
- **Request Parameters:**
    - `id` (type: Long) - The unique ID of the movie to update.
- **Request Body:**
  ```json
  {
    "title": "Updated Title",
    "director": "Updated Director",
    "year": 2023
  }
  ```
- **Response:**
    - 200 OK: Returns the updated movie.
    - 400 Bad Request: The request is incorrect or incomplete.
    - 404 Not Found: No movie is found with the provided ID.

### Delete a Movie by ID

- **Endpoint:** `DELETE /movies/{id}`
- **Description:** Deletes a specific movie based on the provided ID.
- **Request Parameters:**
    - `id` (type: Long) - The unique ID of the movie to delete.
- **Response:**
    - 200 OK: Movie successfully deleted.
    - 404 Not Found: No movie is found with the provided ID.

### Delete All Movies

- **Endpoint:** `DELETE /movies`
- **Description:** Deletes all movies stored in the system.
- **Response:**
    - 200 OK: All movies successfully deleted.
    
