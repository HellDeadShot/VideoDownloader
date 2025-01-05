import yt_dlp

def download_youtube_video(url):
    try:
        ydl_opts = {
            'format': 'bestvideo+bestaudio/best',  # Get best video and audio streams
            'merge_output_format': 'mp4',         # Merge them into MP4
            'outtmpl': '/storage/emulated/0/Download/DeadMedia/YT/%(title)s.%(ext)s',  # Save to desired path
            'quiet': False,
            'noplaylist': True,
        }

        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            ydl.download([url])

        return "YouTube video downloaded and merged successfully."

    except Exception as e:
        return f"Failed to download video: {str(e)}"
