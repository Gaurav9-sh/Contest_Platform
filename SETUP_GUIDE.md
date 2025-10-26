# Quick Setup Guide

## Step-by-Step Setup

### 1. MongoDB Atlas Setup (5 minutes)

1. Go to https://www.mongodb.com/cloud/atlas
2. Sign up or log in
3. Click "Build a Database"
4. Choose FREE tier (M0)
5. Select a cloud provider and region
6. Click "Create"
7. Set up database access:
   - Click "Database Access" in left sidebar
   - Click "Add New Database User"
   - Choose "Password" authentication
   - Enter username and password (save these!)
   - Set privileges to "Read and write to any database"
   - Click "Add User"
8. Set up network access:
   - Click "Network Access" in left sidebar
   - Click "Add IP Address"
   - Click "Allow Access from Anywhere" (for development)
   - Click "Confirm"
9. Get connection string:
   - Click "Database" in left sidebar
   - Click "Connect" on your cluster
   - Click "Connect your application"
   - Copy the connection string
   - Replace `<password>` with your actual password
   - Should look like: `mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/`

### 2. Backend Setup (10 minutes)

```bash
# Navigate to backend
cd backend

# Update application.properties with your MongoDB URI
# Edit: src/main/resources/application.properties
# Replace the mongodb URI with yours

# Make sure Docker is running
docker --version
docker ps

# Build judge Docker image
cd ..
docker build -t judge-runtime:latest -f Dockerfile.judge .
cd backend

# Create temp directory
mkdir -p /tmp/judge

# Build and run
mvn clean install
mvn spring-boot:run
```

Backend will start on: http://localhost:8080

### 3. Frontend Setup (5 minutes)

Open a NEW terminal:

```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Install Tailwind CSS
npm install -D tailwindcss postcss autoprefixer

# Start development server
npm start
```

Frontend will open automatically at: http://localhost:3000

### 4. Get Your Contest ID

After backend starts, check the console logs for the generated contest ID.

Or open MongoDB Atlas:
1. Go to "Database" → "Browse Collections"
2. Select "coding_contest" database
3. Select "contests" collection
4. Copy the "_id" value

### 5. Test the Platform

1. Open http://localhost:3000
2. Enter the contest ID from step 4
3. Enter any username (e.g., "testuser")
4. Enter any email (e.g., "test@example.com")
5. Click "Join Contest"
6. Select a problem
7. Write and submit code
8. Watch the live judging!

## Quick Test Solutions

### Test Problem 1: Sum of Two Numbers

Python:
```python
a, b = map(int, input().split())
print(a + b)
```

Java:
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

## Troubleshooting

### "Cannot connect to Docker"
- Make sure Docker Desktop is running
- Run: `docker ps` to verify
- On Linux: `sudo chmod 666 /var/run/docker.sock`

### "MongoDB connection failed"
- Check your IP is whitelisted in MongoDB Atlas
- Verify connection string is correct
- Ensure password doesn't contain special characters (or URL encode them)

### "Port 8080 already in use"
- Kill the process: `lsof -ti:8080 | xargs kill -9`
- Or change port in application.properties

### "Port 3000 already in use"
- Kill the process: `lsof -ti:3000 | xargs kill -9`
- Or set custom port: `PORT=3001 npm start`

## System Requirements

- **OS**: Windows 10+, macOS 10.15+, or Linux
- **RAM**: 4GB minimum (8GB recommended)
- **Disk**: 2GB free space
- **Internet**: Required for MongoDB Atlas

## Next Steps

1. Try submitting solutions to all 3 problems
2. Open multiple browser tabs to simulate multiple users
3. Watch the leaderboard update in real-time
4. Experiment with different programming languages

## Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review application logs in both terminals
3. Verify all prerequisites are installed
4. Check MongoDB Atlas connection

Enjoy the platform! 🚀
