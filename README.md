# Video Player & Downloader App
Overview
This Android application allows users to play videos from a given URL and download them directly to their device. The app is designed with a simple user interface, allowing users to input the video URL, view the video, and download it with just a few taps.

Features
Video Playback: Play videos directly from a URL using the built-in VideoView component.
Video Download: Download videos to your device's external storage using Android's DownloadManager.
URL Input: Simple input field for users to paste the video URL.
Getting Started
Prerequisites
Android Studio installed on your development machine.
Android device or emulator running Android API level 21 (Lollipop) or higher.
Installation
Clone the repository:

bash
Copy code
git clone [https://github.com/yourusername/your-repo-name.git](https://github.com/HellDeadShot/VideoDownloader.git)
Open the project in Android Studio:

Go to File > Open... and select the project directory.
Build the project:

Wait for Gradle to sync and build the project.
Run the application:

Connect an Android device or start an emulator.
Click the "Run" button in Android Studio.
How to Use
Launch the app on your Android device.
Paste the video URL into the input field.
Press "Download Now" to start playing the video in the VideoView.
The video will automatically start playing.
The video will also be downloaded to the Downloads directory on your device.
File Structure
MainActivity.kt: Contains the main logic for video playback and download functionality.
activity_main.xml: The layout file containing the EditText, VideoView, and Button components.
Limitations
Supported Video Sources: The app can only download videos from direct URLs (e.g., .mp4 files) that are publicly accessible.
YouTube and Other Platforms: This app does not support downloading videos from YouTube, Netflix, or any platform that does not provide direct video file URLs due to legal restrictions.
Permissions
Make sure to add the following permissions to your AndroidManifest.xml file:

xml
Copy code
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

Contributing
Feel free to submit issues or pull requests if you have ideas for improvements or find any bugs.

Contact
For any inquiries or feedback, you can reach me at happynautiyal24@gmail.com

