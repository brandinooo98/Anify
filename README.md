# Anify

Anify is a full-stack application that helps anime fans discover and create Spotify playlists from their favorite anime series. The application integrates with AniList to fetch user's anime lists and creates curated Spotify playlists based on song recommendations from aniplaylist.com.

## Project Overview

Anify consists of three main components:

1. **Frontend** (`/frontend`): A React application that provides the user interface for interacting with Anify
2. **Backend** (`/java-backend`): A Spring Boot application that handles data processing, API integrations, and business logic

## Features

- **Anime List Integration**: Connect your AniList account to import your anime collection
- **Song Discovery**: Automatically find song recommendations for your favorite anime series
- **Spotify Integration**: Create and manage Spotify playlists with your discovered songs
- **User Management**: Track your series and song collections
- **Modern UI**: Clean and intuitive interface for managing your anime music

## Getting Started

Each component has its own setup instructions and requirements. Please refer to the respective READMEs:

- [Frontend Documentation](frontend/README.md)
- [Backend Documentation](java-backend/README.md)

## Development

### Prerequisites

- Node.js 18+ (for frontend)
- Java 17+ (for backend)
- Python 3.8+ (for scraper)
- Maven (for backend)
- Spotify Developer Account
- AniList Account

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/anify.git
   cd anify
   ```

2. Set up each component following their respective READMEs:
   - Start the backend server
   - Run the frontend development server

3. Configure environment variables:
   - Create `.env` files in each component directory
   - Set up Spotify API credentials

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.