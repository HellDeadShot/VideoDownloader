package com.example.videodownloader

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        // Initialize YoutubeDL
        try {
            YoutubeDL.getInstance().init(this)
        } catch (e: YoutubeDLException) {
            e.printStackTrace()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Chaquopy Python
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        val urlInput: EditText = findViewById(R.id.urlInput)
        val downloadButton: Button = findViewById(R.id.downloadButton)
        val statusText: TextView = findViewById(R.id.statusText)
        val statusBar: TextView = findViewById(R.id.statusBar) // Status bar for updates

        // Download button action
        downloadButton.setOnClickListener {
            val videoUrl = urlInput.text.toString()
            if (videoUrl.isNotEmpty()) {
                statusBar.text = "Status: Processing..."
                if (isYoutubeVideoLink(videoUrl)) {
                    downloadYouTubeVideo(videoUrl, statusText, statusBar)
                } else {
                    downloadVideo(videoUrl, statusText, statusBar)
                }
            } else {
                statusText.text = "Please enter a valid URL."
                statusBar.text = "Status: Invalid input."
            }
        }
    }


    // Check if the URL is a YouTube video link
    private fun isYoutubeVideoLink(url: String): Boolean {
        val lowerCaseUrl = url.lowercase()
        return lowerCaseUrl.startsWith("https://www.youtube.com/") ||
                lowerCaseUrl.startsWith("https://youtu.be/") ||
                lowerCaseUrl.contains("youtube.com/watch?") ||
                lowerCaseUrl.contains("youtube.com/shorts")
    }

    // Download YouTube video using YoutubeDL
    private fun downloadYouTubeVideo(videoUrl: String, statusText: TextView, statusBar: TextView) {
        val downloadDir = File("/storage/emulated/0/Download/DeadMedia/YT") // Directory for downloads
        if (!downloadDir.exists()) downloadDir.mkdirs()

        val request = YoutubeDLRequest(videoUrl).apply {
            addOption("-o", "${downloadDir.absolutePath}/%(title)s.%(ext)s") // Output template
            addOption("-f", "bestvideo+bestaudio/best") // Download best video and audio
            addOption("--merge-output-format", "mp4") // Merge video and audio into MP4
        }

        Thread {
            try {
                runOnUiThread { statusBar.text = "Status: Downloading..." }
                val response = YoutubeDL.getInstance().execute(request)
                runOnUiThread {
                    statusText.text = "Download complete: ${response.out}"
                    statusBar.text = "Status: Download complete."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    statusText.text = "Failed to download: ${e.message}"
                    statusBar.text = "Status: Download failed."
                }
            }
        }.start()
    }


    // General video download method (uses Chaquopy and a Python script)
    private fun downloadVideo(videoUrl: String, statusText: TextView, statusBar: TextView) {
        val python = Python.getInstance()
        val pyModule = python.getModule("downloader") // Reference to Python script

        try {
            runOnUiThread { statusBar.text = "Status: Downloading..." }
            val result = pyModule.callAttr("download_video", videoUrl).toString()
            runOnUiThread {
                statusText.text = result
                statusBar.text = "Status: Download complete."
            }
        } catch (e: Exception) {
            runOnUiThread {
                statusText.text = "Error: ${e.message}"
                statusBar.text = "Status: Download failed."
            }
        }
    }
}
