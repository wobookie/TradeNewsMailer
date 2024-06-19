# Trade News Mailer Application

This is a sample Spring Boot application for sending market information emails with attachments using an SMTP server.

## Prerequisites

- Java 11 or higher installed
- An SMTP server with credentials (e.g., Gmail, company SMTP server)

## Running the Application

1. Download the `tradenewsmailer-0.0.1-SNAPSHOT.jar` file.
2. Open a terminal (or Command Prompt) and navigate to the directory where the JAR file is located.
3. Run the following command to start the application:

    ```sh
    java -jar marketmailer-0.0.1-SNAPSHOT.jar
    ```

4. Open a web browser and go to `http://localhost:8080` to access the application.

## Configuring SMTP Settings

1. Navigate to `http://localhost:8080/configuration`.
2. Enter the SMTP settings, including host, port, username, and password.
3. Save the configuration.

## Testing Email Sending

1. Navigate to `http://localhost:8080/upload`.
2. Upload an Excel file with email addresses and an attachment file.
3. Click "Send Emails" to send the emails.

## Notes

- Ensure that the SMTP server settings are correct, especially if using a server with SSL/TLS and authentication.
- For Gmail users with 2FA enabled, generate an App Password and use it as the SMTP password.
