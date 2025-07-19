# Hospital Management System

This is a comprehensive, full-stack Hospital Management System (HMS) designed to streamline and automate hospital operations. This web-based application provides a centralized platform for managing patient information, appointments, billing, and more. It offers distinct portals for administrators, doctors, and patients, each with tailored functionalities to meet their specific needs.

## About The Project

This project was developed to address the inefficiencies of manual or fragmented digital processes in hospitals. By automating core operations and centralizing data, the HMS aims to streamline workflows, minimize errors, and improve the quality of patient care. The system enhances operational accuracy and patient satisfaction by reducing manual tasks and enabling real-time data access. This project was undertaken as part of a 16-week internship to address these challenges and improve healthcare service delivery through technology.

### Key Features

* **Role-Based Access Control:** Separate dashboards and functionalities for Admins, Doctors, and Patients.
* **Admin Dashboard:**
    * Manage doctors and patients (create, view, delete).
    * Schedule and manage appointments.
    * Create and manage billing records.
    * View and approve/decline appointment requests from patients.
* **Doctor Dashboard:**
    * View and manage their appointments (accept/decline).
    * Access patient details and medical history.
    * Write and manage prescriptions.
    * Check for the availability of medicines in the pharmacy.
* **Patient Dashboard:**
    * View personal information and medical history.
    * Request and view appointments with doctors.
    * View and download prescriptions.
    * Purchase medicines from the pharmacy.
    * View and purchase insurance plans.
    * Download bills.
* **External Service Integration:**
    * Seamless integration with external **pharmacy** and **insurance** services through REST APIs.
* **Security:**
    * Secure authentication and authorization using JWT (JSON Web Tokens).

### Technologies Used

This project is built with a modern technology stack, ensuring scalability, security, and a user-friendly experience.

* **Backend:**
    * **Spring Boot:** A robust Java-based framework for creating microservices and web applications.
    * **Spring Security:** For implementing authentication and access control.
    * **JPA (Java Persistence API) / Hibernate:** For object-relational mapping and database interaction.
    * **MySQL:** A reliable and widely-used relational database management system.
    * **Maven:** For project build and dependency management.
* **Frontend:**
    * **Angular:** A popular and powerful TypeScript-based framework for building single-page applications.
    * **HTML, CSS, TypeScript:** The core technologies for building the user interface.
* **API & Communication:**
    * **RESTful APIs:** For enabling communication between the frontend and backend, as well as with external services.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

* **Java JDK (version 17 or higher)**
* **Node.js and npm**
* **MySQL**
* **Maven**
* **Angular CLI**

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/hospital-management-system.git](https://github.com/your-username/hospital-management-system.git)
    ```
2.  **Backend Setup:**
    * Navigate to the `backend` directory.
    * Create a MySQL database named `hospital_management`.
    * Update the `application.properties` file with your MySQL username and password.
    * Build and run the Spring Boot application using Maven.
3.  **Frontend Setup:**
    * Navigate to the `frontend` directory.
    * Install the required npm packages:
        ```sh
        npm install
        ```
    * Run the Angular development server:
        ```sh
        ng serve
        ```
    * Open your browser and navigate to `http://localhost:4200/`.

## Usage

* **Admin Login:** Use the admin credentials to log in and access the admin dashboard to manage the hospital's operations.
* **Doctor Login:** Log in as a doctor to manage appointments, view patient records, and write prescriptions.
* **Patient Login:** Patients can log in to view their health records, book appointments, and access other patient-centric features.

## Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement". Don't forget to give the project a star! Thanks again!

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request
