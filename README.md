# Online Coding Contest Platform

A full-stack coding contest platform with real-time judging, live leaderboards, and Docker-based code execution.

## Tech Stack

### Backend

- **Spring Boot 3.2.0** - Java framework
- **MongoDB Atlas** - Database
- **Docker Java Client** - For code execution
- **Maven** - Build tool

### Frontend

- **React 18** - UI framework
- **React Router** - Navigation
- **Monaco Editor** - Code editor
- **Axios** - HTTP client
- **Tailwind CSS** - Styling

## Features

### Backend Features

- RESTful API endpoints for contests, submissions, and leaderboard
- Asynchronous code judging with Docker containers
- Support for Java, Python, C++, and C
- Real-time submission status updates
- Secure code execution with resource limits
- Live leaderboard calculation

### Frontend Features

- Contest join page with user registration
- Interactive problem browser
- Monaco code editor with syntax highlighting
- Real-time submission status polling
- Live leaderboard updates (every 20 seconds)
- Responsive design with Tailwind CSS

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Node.js 18+** and npm
- **Docker** and Docker daemon running
- **MongoDB Atlas** account

## Setup Instructions

### 1. Database Setup

1. Create a MongoDB Atlas account at https://www.mongodb.com/cloud/atlas
2. Create a new cluster
3. Create a database user with read/write permissions
4. Whitelist your IP address
5. Get your connection string (should look like: `mongodb+srv://<username>:<password>@<cluster>.mongodb.net/`)

### 2. Backend Setup

1. Navigate to the backend directory:

```bash
cd backend
```

2. Update `src/main/resources/application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/coding_contest?retryWrites=true&w=majority
```

3. Build the project:

```bash
mvn clean install
```

4. Build the judge Docker image:

```bash
docker build -t judge-runtime:latest -f ../Dockerfile.judge ..
```

5. Create the temporary directory for judge execution:

```bash
mkdir -p /tmp/judge
```

6. Run the backend:

```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

1. Navigate to the frontend directory:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Install Tailwind CSS:

```bash
npm install -D tailwindcss postcss autoprefixer
```

4. Start the development server:

```bash
npm start
```

The frontend will start on `http://localhost:3000`

### 4. Docker Compose Setup (Alternative)

For a complete setup with Docker Compose:

1. Create a `.env` file in the root directory:

```env
MONGODB_URI=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/coding_contest?retryWrites=true&w=majority
```

2. Build and start all services:

```bash
docker-compose up --build
```

## Usage

### Joining a Contest

1. Open `http://localhost:3000`
2. Enter a contest ID (use the ID from the pre-populated contest in MongoDB)
3. Provide your username and email
4. Click "Join Contest"

### Getting Contest ID

After starting the backend, check the logs for the generated contest ID, or query MongoDB:

```bash
# Using MongoDB Atlas UI or MongoDB Compass
# Connect to your database and run:
db.contests.find({})
```

The sample contest "Summer Coding Challenge 2024" will be automatically created with 3 problems.

### Submitting Code

1. Select a problem from the problem list
2. Choose your programming language (Java, Python, C++, or C)
3. Write your solution in the code editor
4. Click "Submit Code"
5. Watch real-time status updates (Pending → Running → Accepted/Wrong Answer)

### Sample Solutions

#### Problem 1: Sum of Two Numbers

**Java:**

```java
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        System.out.println(a + b);
    }
}
```

**Python:**

```python
a, b = map(int, input().split())
print(a + b)
```

#### Problem 2: Even or Odd

**Java:**

```java
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println(n % 2 == 0 ? "Even" : "Odd");
    }
}
```

**Python:**

```python
n = int(input())
print("Even" if n % 2 == 0 else "Odd")
```

#### Problem 3: Factorial

**Java:**

```java
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        System.out.println(fact);
    }
}
```

**Python:**

```python
import math
n = int(input())
print(math.factorial(n))
```

## API Endpoints

### Contest Endpoints

- **GET** `/api/contests/{contestId}` - Get contest with problems
- **GET** `/api/contests/{contestId}/leaderboard` - Get live leaderboard

### Submission Endpoints

- **POST** `/api/submissions` - Submit code
  ```json
  {
    "userId": "string",
    "username": "string",
    "contestId": "string",
    "problemId": "string",
    "code": "string",
    "language": "java|python|cpp|c"
  }
  ```
- **GET** `/api/submissions/{submissionId}` - Get submission status

### User Endpoints

- **POST** `/api/users/join` - Join contest
  ```json
  {
    "username": "string",
    "email": "string"
  }
  ```

## Architecture

### Code Judging Flow

1. User submits code through the frontend
2. Backend creates a submission with status "PENDING"
3. Asynchronous judge service picks up the submission
4. Code is saved to a temporary directory
5. Docker container is created with the judge runtime image
6. Code is executed with test case inputs via stdin
7. Output is captured and compared with expected output
8. Test case results are stored and submission status is updated
9. Container is cleaned up
10. Frontend polls for updates and displays results

### Security Features

- Docker containers run with limited resources (256MB memory)
- Network isolation (--network=none)
- Execution timeout (10 seconds)
- Non-root user execution
- Temporary file cleanup after execution

## Troubleshooting

### Docker Issues

If you encounter Docker connection issues:

1. Ensure Docker daemon is running:

```bash
docker ps
```

2. Check Docker socket permissions:

```bash
sudo chmod 666 /var/run/docker.sock
```

3. Verify judge image exists:

```bash
docker images | grep judge-runtime
```

### MongoDB Connection Issues

1. Check your IP is whitelisted in MongoDB Atlas
2. Verify connection string format
3. Ensure database user has correct permissions
4. Check network connectivity

### Build Issues

If Maven build fails:

1. Ensure Java 17+ is installed:

```bash
java -version
```

2. Clean Maven cache:

```bash
mvn clean
rm -rf ~/.m2/repository
```

If npm install fails:

1. Clear npm cache:

```bash
npm cache clean --force
```

2. Delete node_modules and reinstall:

```bash
rm -rf node_modules package-lock.json
npm install
```

## Performance Optimization

- Async execution prevents blocking on code judging
- Thread pool manages concurrent submissions (5-10 threads)
- Leaderboard polling reduces server load (20-second intervals)
- Submission status polling is lightweight (2-second intervals)

## Future Enhancements

- [ ] Add more programming languages (Go, Rust, JavaScript)
- [ ] Implement partial scoring for test cases
- [ ] Add contest timer and auto-submission
- [ ] Implement team contests
- [ ] Add problem difficulty filters
- [ ] Implement code plagiarism detection
- [ ] Add submission history
- [ ] Implement real-time notifications with WebSockets
- [ ] Add admin panel for contest management
- [ ] Implement user profiles and statistics

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## Support

For issues or questions, please open an issue on GitHub.
