# 🚀 START HERE - Coding Contest Platform

Welcome! You have a complete full-stack coding contest platform ready to run.

## 📋 What You Got

A production-ready platform with:
- **Spring Boot Backend** (Java 17)
- **React Frontend** (JavaScript + Tailwind CSS)
- **MongoDB Atlas** database integration
- **Docker-based** code execution engine
- **4 programming languages** supported (Java, Python, C++, C)
- **Live leaderboard** with real-time updates
- **Sample contest** with 3 problems pre-loaded

## ⚡ Quick Start (15 minutes)

### Step 1: MongoDB Setup (5 min)
```bash
1. Go to: https://www.mongodb.com/cloud/atlas
2. Create FREE account (M0 tier)
3. Create cluster
4. Add database user (save username/password!)
5. Allow access from anywhere (Network Access)
6. Copy connection string
```

### Step 2: Configure Backend (2 min)
```bash
# Edit: backend/src/main/resources/application.properties
# Replace this line with your MongoDB connection string:
spring.data.mongodb.uri=mongodb+srv://YOUR_USERNAME:YOUR_PASSWORD@cluster0.xxxxx.mongodb.net/coding_contest
```

### Step 3: Build Judge Image (3 min)
```bash
docker build -t judge-runtime:latest -f Dockerfile.judge .
```

### Step 4: Start Backend (3 min)
```bash
cd backend
mvn clean install
mvn spring-boot:run

# Keep this terminal open!
# Backend runs on: http://localhost:8080
```

### Step 5: Start Frontend (2 min)
```bash
# Open NEW terminal
cd frontend
npm install
npm start

# Frontend opens at: http://localhost:3000
```

## 🎮 Test It Out

1. Frontend should open automatically at http://localhost:3000
2. Enter any Contest ID (check backend logs for the ID)
3. Choose username: "testuser"
4. Enter email: "test@example.com"
5. Click "Join Contest"
6. Select a problem
7. Try this Python solution for "Sum of Two Numbers":
```python
a, b = map(int, input().split())
print(a + b)
```
8. Click "Submit Code"
9. Watch it get judged in real-time!

## 📂 Project Structure

```
project/
├── backend/          Spring Boot API (Port 8080)
├── frontend/         React UI (Port 3000)
├── Dockerfile.judge  Judge runtime image
└── Documentation files
```

## 📚 Documentation

- **README.md** - Complete documentation
- **SETUP_GUIDE.md** - Detailed setup instructions
- **PROJECT_STRUCTURE.md** - Architecture details
- **FILE_CHECKLIST.md** - All files created

## 🔧 Prerequisites

Make sure you have installed:
- Java 17 or higher
- Maven 3.6+
- Node.js 18+
- Docker Desktop (running)

Check with:
```bash
java -version    # Should be 17+
mvn -version     # Should be 3.6+
node -version    # Should be 18+
docker ps        # Should show running containers
```

## ❓ Troubleshooting

### "Cannot connect to Docker"
```bash
# Make sure Docker Desktop is running
docker ps
```

### "MongoDB connection failed"
- Check MongoDB Atlas → Network Access → Add your IP
- Verify connection string has correct password
- Ensure no special characters in password (or URL encode them)

### "Port already in use"
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Kill process on port 3000
lsof -ti:3000 | xargs kill -9
```

## 🎯 Sample Solutions

### Problem 1: Sum of Two Numbers

**Python:**
```python
a, b = map(int, input().split())
print(a + b)
```

**Java:**
```java
import java.util.Scanner;
public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println(sc.nextInt() + sc.nextInt());
    }
}
```

### Problem 2: Even or Odd

**Python:**
```python
n = int(input())
print("Even" if n % 2 == 0 else "Odd")
```

### Problem 3: Factorial

**Python:**
```python
import math
print(math.factorial(int(input())))
```

## 🌟 Features

### For Users
- Browse contest problems
- Write code in 4 languages
- Real-time submission feedback
- Live leaderboard
- Test case visibility
- Score tracking

### For Developers
- RESTful API
- Async code judging
- Docker isolation
- MongoDB database
- React + Tailwind UI
- Easy to extend

## 📦 What's Included

**Backend (Spring Boot):**
- 31 Java files
- REST Controllers
- MongoDB repositories
- Docker-based judge engine
- Async execution
- Sample data

**Frontend (React):**
- 14 JavaScript files
- Monaco code editor
- Live updates
- Responsive design
- Tailwind CSS

**Infrastructure:**
- Docker support
- Docker Compose
- Nginx config
- Judge runtime

## 🚢 Deployment Options

### Local Development (Current)
- Backend: `mvn spring-boot:run`
- Frontend: `npm start`

### Docker Compose
```bash
# Set MongoDB URI in .env file
docker-compose up --build
```

### Production
- Build JAR: `mvn clean package`
- Build React: `npm run build`
- Deploy with Docker images

## 🎓 Next Steps

1. ✅ Get the platform running
2. ✅ Submit test solutions
3. ✅ Check leaderboard updates
4. ✅ Try all 3 problems
5. 🔄 Add your own problems
6. 🔄 Customize the UI
7. 🔄 Deploy to production

## 💡 Tips

- Open multiple browser tabs to simulate multiple users
- Leaderboard updates every 20 seconds
- Submission status polls every 2 seconds
- All sample problems have 5 test cases each
- 2 test cases are visible, 3 are hidden

## 🆘 Need Help?

1. Check SETUP_GUIDE.md for detailed instructions
2. Review troubleshooting section above
3. Verify all prerequisites are installed
4. Check application logs in both terminals

## 🎉 You're Ready!

Everything is set up and ready to go. Just follow the Quick Start steps above and you'll have a fully functional coding contest platform running in 15 minutes!

Happy Coding! 🚀
