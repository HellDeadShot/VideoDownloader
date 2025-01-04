import yt_dlp

def download_video(url):
    ydl_opts = {
        'outtmpl': '/storage/emulated/0/Download/DeadMedia/%(title)s.%(ext)s',  # Save to Downloads folder
        'format': 'best',
    }
    try:
        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            ydl.download([url])
        return "Download completed successfully!"
    except Exception as e:
        return f"Download failed: {e}"