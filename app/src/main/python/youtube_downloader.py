import yt_dlp

def download_youtube_video(url):
    try:
        # Create a YTDL object
        ydl_opts = {
            'outtmpl': '/storage/emulated/0/Download/DeadMedia/%(title)s.%(ext)s',  # Output path
            'noplaylist': True,  # Skip playlists
            'quiet': False       # Show debug info
        }

        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            # Fetch available formats
            info = ydl.extract_info(url, download=False)
            formats = info.get('formats', [])

            # Display available formats (optional for debugging)
            print("Available formats:")
            for f in formats:
                print(f"{f['format_id']}: {f['ext']} - {f['resolution']} - {f['vcodec']}")

            # Select the best MP4 format (or fallback to any available format)
            selected_format = next((f for f in formats if f['ext'] == 'mp4'), None)
            if not selected_format:
                selected_format = formats[0]  # Fallback to the first available format

            # Update the options with the selected format
            ydl_opts['format'] = selected_format['format_id']
            print(f"Selected format: {selected_format['format_id']}")

            # Download the video
            with yt_dlp.YoutubeDL(ydl_opts) as ydl:
                ydl.download([url])

        return "YouTube video downloaded successfully."

    except Exception as e:
        return f"Failed to download video: {str(e)}"
