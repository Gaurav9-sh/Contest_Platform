# Project Structure

```
coding-contest-platform/
│
├── backend/                                    # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/codeplatform/
│   │   │   │   ├── CodingContestApplication.java    # Main application class
│   │   │   │   │
│   │   │   │   ├── config/                          # Configuration classes
│   │   │   │   │   ├── AsyncConfig.java             # Async execution config
│   │   │   │   │   ├── CorsConfig.java              # CORS configuration
│   │   │   │   │   └── DataInitializer.java         # Sample data loader
│   │   │   │   │
│   │   │   │   ├── controller/                      # REST Controllers
│   │   │   │   │   ├── ContestController.java       # Contest endpoints
│   │   │   │   │   ├── SubmissionController.java    # Submission endpoints
│   │   │   │   │   └── UserController.java          # User endpoints
│   │   │   │   │
│   │   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   │   │   ├── ContestResponse.java         # Contest response DTO
│   │   │   │   │   ├── LeaderboardEntry.java        # Leaderboard entry DTO
│   │   │   │   │   └── SubmissionRequest.java       # Submission request DTO
│   │   │   │   │
│   │   │   │   ├── model/                           # Domain Models
│   │   │   │   │   ├── Contest.java                 # Contest entity
│   │   │   │   │   ├── Problem.java                 # Problem entity
│   │   │   │   │   ├── Submission.java              # Submission entity
│   │   │   │   │   ├── TestCase.java                # Test case model
│   │   │   │   │   ├── TestCaseResult.java          # Test result model
│   │   │   │   │   └── User.java                    # User entity
│   │   │   │   │
│   │   │   │   ├── repository/                      # MongoDB Repositories
│   │   │   │   │   ├── ContestRepository.java
│   │   │   │   │   ├── ProblemRepository.java
│   │   │   │   │   ├── SubmissionRepository.java
│   │   │   │   │   └── UserRepository.java
│   │   │   │   │
│   │   │   │   └── service/                         # Business Logic
│   │   │   │       ├── ContestService.java          # Contest operations
│   │   │   │       ├── JudgeService.java            # Code judging engine
│   │   │   │       ├── LeaderboardService.java      # Leaderboard calculation
│   │   │   │       ├── SubmissionService.java       # Submission handling
│   │   │   │       └── UserService.java             # User operations
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties           # Application config
│   │   │
│   │   └── test/                                    # Test directory
│   │
│   ├── Dockerfile                                   # Backend Docker image
│   ├── pom.xml                                      # Maven dependencies
│   └── .gitignore
│
├── frontend/                                   # React Frontend
│   ├── public/
│   │   └── index.html                              # HTML template
│   │
│   ├── src/
│   │   ├── components/                             # React Components
│   │   │   ├── CodeEditor.js                       # Monaco code editor
│   │   │   ├── Leaderboard.js                      # Live leaderboard
│   │   │   ├── ProblemList.js                      # Problem selector
│   │   │   └── ProblemView.js                      # Problem display
│   │   │
│   │   ├── pages/                                  # Page Components
│   │   │   ├── ContestPage.js                      # Main contest page
│   │   │   └── JoinPage.js                         # Join contest page
│   │   │
│   │   ├── services/                               # API Services
│   │   │   └── api.js                              # API client
│   │   │
│   │   ├── App.js                                  # Root component
│   │   ├── index.css                               # Global styles
│   │   └── index.js                                # Entry point
│   │
│   ├── Dockerfile                                  # Frontend Docker image
│   ├── nginx.conf                                  # Nginx configuration
│   ├── package.json                                # NPM dependencies
│   ├── tailwind.config.js                          # Tailwind config
│   └── .gitignore
│
├── Dockerfile.judge                            # Judge runtime image
├── docker-compose.yml                          # Multi-container setup
├── README.md                                   # Full documentation
├── SETUP_GUIDE.md                              # Quick setup guide
└── PROJECT_STRUCTURE.md                        # This file

```

## Component Descriptions

### Backend Components

#### Controllers
- Handle HTTP requests and responses
- Validate input data
- Return appropriate status codes

#### Services
- **ContestService**: Manages contest data and problems
- **SubmissionService**: Handles code submissions
- **JudgeService**: Executes code in Docker containers (async)
- **LeaderboardService**: Calculates rankings and scores
- **UserService**: Manages user registration

#### Repositories
- MongoDB data access layer
- Spring Data MongoDB interfaces
- Custom query methods

#### Models
- **Contest**: Contest information and timing
- **Problem**: Problem details and test cases
- **Submission**: User submissions and results
- **User**: User information
- **TestCase**: Input/output test data

### Frontend Components

#### Pages
- **JoinPage**: Contest entry form
- **ContestPage**: Main contest interface

#### Components
- **ProblemList**: Displays available problems
- **ProblemView**: Shows problem description and test cases
- **CodeEditor**: Monaco editor with language selection
- **Leaderboard**: Live ranking display

#### Services
- **api.js**: Axios-based API client

## Data Flow

### Submission Flow
```
User → CodeEditor → API → SubmissionService → JudgeService
                                                    ↓
                                            Docker Container
                                                    ↓
                                            Test Execution
                                                    ↓
Frontend ← API ← Submission Result ← Database Update
```

### Leaderboard Flow
```
Frontend → API → LeaderboardService → Calculate Rankings
    ↑                                         ↓
    └──────── Poll every 20s ←───── Return Sorted List
```

## Key Files

### Configuration
- `application.properties` - Spring Boot config
- `tailwind.config.js` - Tailwind CSS config
- `docker-compose.yml` - Multi-container orchestration

### Entry Points
- `CodingContestApplication.java` - Backend main class
- `index.js` - Frontend entry point

### Docker
- `backend/Dockerfile` - Backend container
- `frontend/Dockerfile` - Frontend container with Nginx
- `Dockerfile.judge` - Judge runtime with compilers

## Technology Stack

### Backend
- Spring Boot 3.2.0
- Spring Data MongoDB
- Docker Java Client
- Lombok
- Maven

### Frontend
- React 18
- React Router DOM
- Monaco Editor
- Axios
- Tailwind CSS

### Infrastructure
- Docker
- MongoDB Atlas
- Nginx (for production frontend)

## Build Artifacts

### Backend
- Target: `backend/target/coding-contest-platform-1.0.0.jar`

### Frontend
- Build: `frontend/build/` (production)
- Development: Runs on webpack dev server

## Environment Variables

### Backend
```properties
spring.data.mongodb.uri         # MongoDB connection string
judge.docker.image              # Judge Docker image name
judge.timeout.seconds           # Execution timeout
judge.memory.limit              # Container memory limit
```

### Frontend
```
REACT_APP_API_URL               # Backend API URL (optional)
```

## Ports

- Backend: 8080
- Frontend: 3000 (dev), 80 (prod)
- MongoDB: 27017 (Atlas managed)

## Security Features

- CORS configuration for cross-origin requests
- Docker network isolation
- Resource limits on containers
- Non-root user execution in containers
- Input validation on all endpoints
