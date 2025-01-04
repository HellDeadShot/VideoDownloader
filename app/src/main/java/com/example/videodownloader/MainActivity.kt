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
import java.io.FileOutputStream

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

        // Download button action
        downloadButton.setOnClickListener {
            val videoUrl = urlInput.text.toString()
            if (videoUrl.isNotEmpty()) {
                if (isYoutubeVideoLink(videoUrl)) {
                    downloadYouTubeVideo(videoUrl, statusText)
                } else {
                    downloadVideo(videoUrl, statusText)
                }
            } else {
                statusText.text = "Please enter a valid URL."
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
    private fun downloadYouTubeVideo(videoUrl: String, statusText: TextView) {
        val downloadDir = File(getExternalFilesDir(null), "downloads") // Directory for downloads
        if (!downloadDir.exists()) downloadDir.mkdirs()

        val request = YoutubeDLRequest(videoUrl).apply {
            addOption("-o", "${downloadDir.absolutePath}/%(title)s.%(ext)s") // Output template
            addOption("-f", "bestvideo+bestaudio/best") // Download best video+audio
        }

        Thread {
            try {
                val response = YoutubeDL.getInstance().execute(request)
                runOnUiThread { statusText.text = "Download complete: ${response.out}" }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { statusText.text = "Failed to download: ${e.message}" }
            }
        }.start()
    }

    // General video download method (uses Chaquopy and a Python script)
    private fun downloadVideo(videoUrl: String, statusText: TextView) {
        val python = Python.getInstance()
        val pyModule = python.getModule("downloader") // Reference to Python script

        try {
            val result = pyModule.callAttr("download_video", videoUrl).toString()
            statusText.text = result
        } catch (e: Exception) {
            statusText.text = "Error: ${e.message}"
        }
    }
}
