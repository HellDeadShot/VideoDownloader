package com.example.videodownloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.videodownloader.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to the UI components
        val videoView: VideoView = findViewById(R.id.video_view)
        val downloadButton: Button = findViewById(R.id.download)
        val videoUrlEditText: EditText = findViewById(R.id.videoUrlEditText)

        // Set up the download button click listener
        downloadButton.setOnClickListener {
            val videoUrl = videoUrlEditText.text.toString()
            if (videoUrl.isNotEmpty()) {
                val videoUri: Uri = Uri.parse(videoUrl)
                playVideo(videoUri, videoView)
                downloadVideo(videoUri)
            }
        }
    }

    private fun playVideo(videoUri: Uri, videoView: VideoView) {
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.start()
    }

    private fun downloadVideo(videoUri: Uri) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(videoUri)

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "yourvideo.mp4")
        downloadManager.enqueue(request)
    }
}
