# Anify Backend

A Spring Boot backend service that integrates with Spotify to create and manage playlists based on anime series and their associated songs. This service allows users to create playlists of songs from their favorite anime series and sync them with their Spotify account.

## Features

- User authentication and management
- Series management (create, read, update, delete)
- Song management with Spotify integration
- Automatic playlist creation and management
- H2 database for data persistence
- RESTful API endpoints

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Spotify Developer Account with a registered application
- Spotify Client ID and Client Secret

## Installation

1. Create a `.env` file in the project root with the following variables:
```env
# Spotify Configuration
SPOTIFY_CLIENT_ID=your_client_id_here
SPOTIFY_CLIENT_SECRET=your_client_secret_here
SPOTIFY_REDIRECT_URI=http://localhost:5000/spotify/callback

# Server Configuration
SERVER_PORT=5000

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:h2:file:./database
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=password
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:5000`

## Project Structure

```
java-backend/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── anify/
│       │           └── backend/
│       │               ├── controller/
│       │               ├── model/
│       │               ├── repository/
│       │               ├── service/
│       │               └── AnifyApplication.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── .env
└── README.md
```

## API Endpoints

### Users
- `POST /api/users` - Create a new user
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Series
- `POST /api/series` - Create a new series
- `GET /api/series` - Get all series
- `GET /api/series/{id}` - Get series by ID
- `PUT /api/series/{id}` - Update series
- `DELETE /api/series/{id}` - Delete series

### Songs
- `POST /api/songs` - Create a new song
- `GET /api/songs` - Get all songs
- `GET /api/songs/{id}` - Get song by ID
- `PUT /api/songs/{id}` - Update song
- `DELETE /api/songs/{id}` - Delete song

### Spotify Integration
- `GET /spotify/login` - Initiate Spotify login
- `GET /spotify/callback` - Spotify OAuth callback
- `POST /spotify/playlists` - Create a new playlist
- `GET /spotify/playlists` - Get user's playlists

## Database

The application uses H2 database with the following default configuration:
- URL: `jdbc:h2:file:./database`
- Username: `sa`
- Password: `password`

The H2 console is available at `http://localhost:5000/h2-console` when the application is running.

## Development

The project uses:
- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database
- Lombok
- Spotify Web API Java

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 