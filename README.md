# 🌿 EcoTrackU

**EcoTrackU** is an Android app designed to encourage eco-friendly living through small, trackable actions.  
It rewards users with **Eco Points** for completing daily green challenges and integrates features like walking and cycling tracking using Google Fit (to promote sustainable habits).

---

## ✨ Features

### 🌍 Splash Screen
- A smooth animated splash screen with the **EcoTrackU** logo and name.
- Transitions elegantly into the home screen.

### 🏠 Home Screen
- Displays **Eco Points** earned by users.
- Offers quick access buttons for:
  - 🚶‍♂️ **Walking Distance Tracking**
  - 🚴‍♀️ **Cycling Stats**
  - 🌱 **Daily Green Challenges**
  - 🏆 **Leaderboard (Coming Soon)**
  - 📊 **Analytics Graphs (Coming Soon)**

### 🎯 Daily Green Challenges
- Users can complete eco-friendly tasks such as:
  - Using public transport
  - Avoiding plastic
  - Recycling
- Completing these tasks **adds Eco Points** to their total.

### 💪 Gamification
- Eco Points encourage competition and consistency.
- Points can be displayed on a leaderboard (future enhancement).

---

## 🧠 Tech Stack

| Component | Technology Used |
|------------|----------------|
| Language | **Java** |
| IDE | **Android Studio** |
| UI Design | **XML** (Material 3, ConstraintLayout) |
| Animation | **Fade-in, XML animations** |
| Database | (Future) Firebase Realtime Database |
| API Integration | (Future) Google Fit API |



## ⚙️ Project Structure
EcoTrackU/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/ecotracku/
│   │   │   │   ├── MainActivity.java              # Home screen & navigation
│   │   │   │   ├── SplashActivity.java            # Intro splash screen
│   │   │   │   ├── EcoTasksActivity.java          # Daily eco tasks screen
│   │   │   │   ├── CycleActivity.java             # Cycling tracker (Google Fit)
│   │   │   │   ├── WalkActivity.java              # Walking tracker (Google Fit)
│   │   │   │   ├── LeaderboardActivity.java       # Firebase leaderboard
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── layout/                        # All XML UI files
│   │   │   │   │   ├── activity_home.xml
│   │   │   │   │   ├── activity_splash.xml
│   │   │   │   │   ├── activity_ecotasks.xml
│   │   │   │   │   ├── activity_cycle.xml
│   │   │   │   │   ├── activity_walk.xml
│   │   │   │   │   ├── activity_leaderboard.xml
│   │   │   │   ├── drawable/                      # Icons and backgrounds
│   │   │   │   ├── anim/                          # Fade-in & slide animations
│   │   │   │   ├── values/                        # Styles and themes
│   │   │   │
│   │   │   ├── AndroidManifest.xml                # App activity declarations
│   │   │
│   │   ├── google-services.json                   # Firebase connection config
│   │
│   ├── build.gradle (Module: app)
│   ├── build.gradle (Project)
│
├── README.md                                      # Project documentation
└── .gitignore


##🚀 How to Run the Project
1. Clone this repository
   Copy code
   git clone https://github.com/YourUsername/EcoTrackU.git
2. Open in Android Studio
   Select Open an Existing Project → choose the EcoTrackU folder.
3. Sync Gradle
   Click 🐘 (Elephant icon) to sync project with Gradle files.
4. Connect Firebase (optional)
   Add your own google-services.json file to /app/.
5. Run the app
   Choose a virtual or physical Android device → click ▶️ Run.


##🔮 Future Enhancements
✨ Firebase Leaderboard Sync
Real-time leaderboard with user authentication and Eco Points storage.
🚴 Google Fit Integration
Track actual distance, steps, and calories burned during eco activities.
🛍️ Eco Shop
Users can redeem Eco Points for coupons, vouchers, and rewards.
📊 Graphs & Analytics
Visual charts for weekly activity and performance using MPAndroidChart.
🎖️ Achievements & Badges
Unlock badges for milestones like “First 10 km cycled” or “Plastic-Free Week”.
🌎 Community Section
Global leaderboard and sharing eco achievements.

##🧑‍💻 Developers
Krish Agarwal, Mehak Gupta, Shreesh Goyal


