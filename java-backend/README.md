# Anify Backend

A Spring Boot application that integrates AniList and Spotify APIs to create music playlists from anime series. The backend handles user authentication, anime data scraping, and playlist management.

## Architecture

### Core Components
- **Spring Boot 3.2.3**: Main application framework
- **Spring Data JPA**: Database access and entity management
- **Spring GraphQL**: GraphQL API implementation
- **H2 Database**: File-based database for data persistence
- **Maven**: Dependency management and build tool

### External Integrations
- **AniList API**: Fetches user's anime list and series information
- **Spotify API**: Handles playlist creation and track management
- **AniPlaylist Scraping**: Extracts song recommendations from aniplaylist.com

### Key Features
- Anime series management
- Song discovery and management
- Spotify playlist creation
- GraphQL and REST API endpoints

## Technical Stack

### Backend
- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- Spring GraphQL
- H2 Database (file-based)
- Maven

### Dependencies
- `spring-boot-starter-web`: REST API support
- `spring-boot-starter-data-jpa`: Database access
- `spring-boot-starter-graphql`: GraphQL support
- `spotify-web-api-java`: Spotify API integration
- `selenium-java`: Dynamic content scraping
- `h2`: Database

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Spotify Developer Account
- AniList Account

### Setup
1. Clone the repository
2. Navigate to the backend directory:
   ```bash
   cd java-backend
   ```
3. Create a `data` directory for the H2 database:
   ```bash
   mkdir -p data
   ```
4. Configure your Spotify credentials in `src/main/resources/application.properties`:
   ```properties
   spotify.client.id=your_client_id
   spotify.client.secret=your_client_secret
   spotify.redirect.uri=your_redirect_uri
   ```
5. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Documentation

### GraphQL Endpoints
- `/graphql` - Main GraphQL endpoint
- `/graphiql` - GraphQL Playground for testing queries

### REST Endpoints

#### Spotify Integration
- `GET /api/spotify/login` - Get Spotify authorization URL
- `GET /api/spotify/callback?code={code}` - Handle Spotify OAuth callback
- `POST /api/spotify/playlist?username={username}&playlistName={name}` - Create playlist from user's songs

#### User Management
- `POST /api/users?username={username}` - Create new user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/by-username/{username}` - Get user ID by username
- `PUT /api/users/{id}` - Update user details
- `DELETE /api/users/{id}` - Delete user
- `POST /api/users/{id}/scrape-songs` - Scrape songs for user's series

#### Series Management
- `POST /api/series/users/{userId}` - Create series for user
- `POST /api/series/{seriesId}/users/{userId}` - Add series to user
- `DELETE /api/series/{seriesId}/users/{userId}` - Remove series from user
- `GET /api/series` - Get all series
- `GET /api/series/{id}` - Get series by ID
- `DELETE /api/series/{id}` - Delete series
- `GET /api/series/users/{userId}` - Get user's series

#### Song Management
- `POST /api/songs/users/{userId}` - Create song for user
- `GET /api/songs` - Get all songs
- `GET /api/songs/{id}` - Get song by ID
- `PUT /api/songs/{id}` - Update song details
- `DELETE /api/songs/{id}` - Delete song
- `GET /api/songs/users/{userId}` - Get user's songs
- `GET /api/songs/series/{seriesId}` - Get series' songs

#### Anime Integration
- `GET /api/anime/list?username={username}` - Get user's anime list from AniList

## Database Schema

### Entities

#### User
- `id`: Long (Primary Key)
- `username`: String
- `spotifyAccessToken`: String
- `spotifyRefreshToken`: String
- `spotifyTokenExpiry`: Long
- `series`: List<Series> (Many-to-Many)

#### Series
- `id`: Long (Primary Key)
- `name`: String
- `songs`: List<Song> (One-to-Many)
- `users`: List<User> (Many-to-Many)

#### Song
- `id`: Long (Primary Key)
- `title`: String
- `artist`: String
- `uri`: String (Spotify URI)
- `series`: Series (Many-to-One)
- `user`: User (Many-to-One)

#### UserSeries
- `id`: Long (Primary Key)
- `user`: User (Many-to-One)
- `series`: Series (Many-to-One)

## Development

### Project Structure
```
java-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/anify/backend/
│   │   │       ├── controller/    # REST controllers
│   │   │       ├── model/         # Entity classes
│   │   │       ├── repository/    # JPA repositories
│   │   │       ├── service/       # Business logic
│   │   │       ├── resolver/      # GraphQL resolvers
│   │   │       └── config/        # Configuration classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── graphql/           # GraphQL schema files
│   └── test/                      # Test files
└── pom.xml                        # Maven configuration
```

### Building
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```