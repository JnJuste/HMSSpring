# Hospital Management System

## Overview

The Hospital Management System is a web application designed to manage hospital operations including the management of doctors, nurses, and patients. The system allows for the scheduling of appointments and includes features for user authentication and role management.

## Features

- **Doctor Management**: Create, update, delete, and view doctors. Each doctor has a unique registration number in the format `DRxxxx-xx`.
- **Nurse Management**: Create, update, delete, and view nurses. Each nurse has a unique registration number in the format `NRxxxx-xx`.
- **Patient Management**: Create, update, delete, and view patients.
- **Appointment Scheduling**: Schedule and manage appointments between doctors and patients.
- **User Authentication**: Login functionality with role-based access control.
- **Email Notifications**: Send notifications via email for important events.
- **Roles**: Different roles such as doctors, nurses, and admin with specific permissions.

## Technologies Used

- **Backend**: Spring Boot, Spring Data JPA (Hibernate), Spring Security
- **Frontend**: React.js
- **Database**: MySQL
- **Build Tool**: Maven
- **Other Tools**: Lombok

## Getting Started

### Prerequisites

- Java 17 or higher
- Node.js (for frontend)
- MySQL
- Maven

### Setup Instructions

#### Backend

1. **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/hospital-management-system.git
    cd hospital-management-system
    ```

2. **Update application properties:**
    Configure your database connection in `src/main/resources/application.properties`.
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/hospitaldb
    spring.datasource.username=root
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

3. **Run the backend application:**
    ```bash
    mvn spring-boot:run
    ```

#### Frontend

1. **Navigate to the frontend directory:**
    ```bash
    cd frontend
    ```

2. **Install dependencies:**
    ```bash
    npm install
    ```

3. **Run the frontend application:**
    ```bash
    npm start
    ```

## API Endpoints

### Doctor

- **Create Doctor:**
    - **URL**: `/api/doctors`
    - **Method**: POST
    - **Body**:
      ```json
      {
          "firstName": "Jean",
          "lastName": "Doe",
          "password": "password123",
          "email": "jean.doe@example.com",
          "role": "DOCTOR",
          "gender": "MALE",
          "specialty": "CARDIOLOGY",
          "employmentType": "FULL_TIME",
          "nationalID": 12345678
      }
      ```

- **Get All Doctors:**
    - **URL**: `/api/doctors`
    - **Method**: GET

- **Get Doctor by ID:**
    - **URL**: `/api/doctors/{id}`
    - **Method**: GET

- **Get Doctor by Email:**
    - **URL**: `/api/doctors/email/{email}`
    - **Method**: GET

- **Get Doctor by National ID:**
    - **URL**: `/api/doctors/nationalID/{nationalID}`
    - **Method**: GET

- **Get Doctor by Registration Number:**
    - **URL**: `/api/doctors/regNumber/{regNumber}`
    - **Method**: GET

- **Update Doctor:**
    - **URL**: `/api/doctors/{id}`
    - **Method**: PUT
    - **Body**:
      ```json
      {
          "firstName": "Jean",
          "lastName": "Doe",
          "email": "jean.doe@example.com",
          "gender": "MALE",
          "specialty": "NEUROLOGY",
          "employmentType": "PART_TIME"
      }
      ```

- **Delete Doctor:**
    - **URL**: `/api/doctors/{id}`
    - **Method**: DELETE

### Nurse

- **Create Nurse:**
    - **URL**: `/api/nurses`
    - **Method**: POST
    - **Body**:
      ```json
      {
          "firstName": "Mary",
          "lastName": "Jane",
          "password": "password123",
          "email": "mary.jane@example.com",
          "role": "NURSE",
          "gender": "FEMALE",
          "doctor": {
              "userID": "uuid-of-doctor"
          },
          "nationalID": 23456789
      }
      ```

- **Get All Nurses:**
    - **URL**: `/api/nurses`
    - **Method**: GET

- **Get Nurse by ID:**
    - **URL**: `/api/nurses/{id}`
    - **Method**: GET

- **Get Nurse by Email:**
    - **URL**: `/api/nurses/email/{email}`
    - **Method**: GET

- **Get Nurse by National ID:**
    - **URL**: `/api/nurses/nationalID/{nationalID}`
    - **Method**: GET

- **Get Nurse by Registration Number:**
    - **URL**: `/api/nurses/regNumber/{regNumber}`
    - **Method**: GET

- **Update Nurse:**
    - **URL**: `/api/nurses/{id}`
    - **Method**: PUT
    - **Body**:
      ```json
      {
          "firstName": "Mary",
          "lastName": "Jane",
          "email": "mary.jane@example.com",
          "gender": "FEMALE",
          "doctor": {
              "userID": "new-uuid-of-doctor"
          }
      }
      ```

- **Delete Nurse:**
    - **URL**: `/api/nurses/{id}`
    - **Method**: DELETE

### Patient

- **Create Patient:**
    - **URL**: `/api/patients`
    - **Method**: POST
    - **Body**:
      ```json
      {
          "firstName": "John",
          "lastName": "Smith",
          "dateOfBirth": "1990-01-01",
          "phoneNumber": "+250789123456",
          "email": "john.smith@example.com",
          "gender": "MALE",
          "reason": "Routine check-up",
          "nationalID": 34567890
      }
      ```

- **Get All Patients:**
    - **URL**: `/api/patients`
    - **Method**: GET

- **Get Patient by ID:**
    - **URL**: `/api/patients/{id}`
    - **Method**: GET

- **Get Patient by National ID:**
    - **URL**: `/api/patients/nationalID/{nationalID}`
    - **Method**: GET

- **Update Patient:**
    - **URL**: `/api/patients/{id}`
    - **Method**: PUT
    - **Body**:
      ```json
      {
          "firstName": "John",
          "lastName": "Smith",
          "dateOfBirth": "1990-01-01",
          "phoneNumber": "+250789123456",
          "email": "john.smith@example.com",
          "gender": "MALE",
          "reason": "Updated reason"
      }
      ```

- **Delete Patient:**
    - **URL**: `/api/patients/{id}`
    - **Method**: DELETE

## Contribution

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add new feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Create a new Pull Request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
