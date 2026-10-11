#!/usr/bin/env python3
import os
import mimetypes
from http.server import HTTPServer, BaseHTTPRequestHandler
import urllib.parse

PORT = 3000
APK_PATH = "/app/applet/Elias-Stickers.apk"
if not os.path.exists(APK_PATH):
    APK_PATH = "./Elias-Stickers.apk"

HTML_TEMPLATE = """<!DOCTYPE html>
<html lang="bn">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Elias Stickers — Turn Your Photos into Amazing Stickers</title>
  <style>
    :root {
      --bg: #FBF8F2;
      --card-bg: #FFFFFF;
      --charcoal: #1C1917;
      --muted: #78716C;
      --amber: #D97706;
      --coral: #E11D48;
      --green: #059669;
      --border: #E7DFD5;
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      background-color: var(--bg);
      color: var(--charcoal);
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
      line-height: 1.5;
      padding-bottom: 60px;
    }
    .header-banner {
      background: linear-gradient(180deg, #F9F3EA 0%, #EFE4D4 100%);
      padding: 24px 16px 28px;
      border-bottom-left-radius: 28px;
      border-bottom-right-radius: 28px;
      text-align: center;
      box-shadow: 0 4px 12px rgba(0,0,0,0.05);
    }
    .top-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      max-width: 680px;
      margin: 0 auto 16px;
    }
    .profile-badge {
      display: flex;
      align-items: center;
      gap: 12px;
      text-align: left;
    }
    .profile-avatar {
      width: 54px;
      height: 54px;
      border-radius: 50%;
      border: 3px solid #FFF;
      box-shadow: 0 4px 10px rgba(0,0,0,0.15);
      object-fit: cover;
      background: #2B2723;
    }
    .profile-name {
      font-weight: 800;
      font-size: 18px;
      color: var(--charcoal);
    }
    .profile-sub {
      font-size: 12px;
      color: var(--green);
      font-weight: 600;
      display: flex;
      align-items: center;
      gap: 4px;
    }
    .profile-sub::before {
      content: "";
      width: 8px;
      height: 8px;
      background: var(--green);
      border-radius: 50%;
      display: inline-block;
    }
    .app-title {
      font-size: 32px;
      font-weight: 900;
      letter-spacing: 1px;
      color: var(--charcoal);
      margin-top: 4px;
    }
    .app-tagline {
      font-size: 14px;
      color: var(--muted);
      font-weight: 500;
      margin-top: 4px;
    }
    .download-hero-card {
      max-width: 680px;
      margin: -16px auto 20px;
      background: #FFFFFF;
      border-radius: 20px;
      padding: 20px;
      box-shadow: 0 8px 24px rgba(0,0,0,0.08);
      border: 2px solid #FEF3C7;
      text-align: center;
    }
    .btn-download-apk {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      background: var(--charcoal);
      color: #FFFFFF;
      text-decoration: none;
      font-size: 16px;
      font-weight: 800;
      padding: 16px 28px;
      border-radius: 16px;
      box-shadow: 0 6px 16px rgba(28,25,23,0.3);
      transition: all 0.2s ease;
      width: 100%;
      max-width: 440px;
    }
    .btn-download-apk:hover {
      background: #000000;
      transform: translateY(-2px);
      box-shadow: 0 8px 20px rgba(0,0,0,0.4);
    }
    .apk-meta {
      margin-top: 10px;
      font-size: 12px;
      color: var(--muted);
      display: flex;
      justify-content: center;
      gap: 16px;
      flex-wrap: wrap;
    }
    .container {
      max-width: 680px;
      margin: 0 auto;
      padding: 0 16px;
    }
    .section-title {
      font-size: 17px;
      font-weight: 800;
      text-align: center;
      margin: 20px 0 12px;
      color: var(--charcoal);
    }
    .action-buttons {
      display: flex;
      gap: 12px;
      justify-content: center;
      margin-bottom: 20px;
    }
    .action-btn {
      flex: 1;
      padding: 12px 18px;
      border-radius: 14px;
      border: 1.5px solid var(--charcoal);
      background: #FFF;
      color: var(--charcoal);
      font-weight: 700;
      font-size: 14px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
    }
    .styles-scroll {
      display: flex;
      gap: 14px;
      overflow-x: auto;
      padding: 8px 4px 16px;
      scroll-snap-type: x mandatory;
    }
    .style-card {
      flex: 0 0 95px;
      text-align: center;
      cursor: pointer;
      scroll-snap-align: start;
    }
    .style-icon-box {
      width: 80px;
      height: 80px;
      margin: 0 auto 6px;
      background: #FFF;
      border: 2px solid var(--border);
      border-radius: 18px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 34px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.04);
      transition: all 0.2s;
    }
    .style-card.active .style-icon-box {
      border: 3px solid var(--amber);
      background: #FFF7ED;
      transform: scale(1.05);
      box-shadow: 0 6px 16px rgba(217,119,6,0.25);
    }
    .style-name {
      font-size: 11px;
      font-weight: 700;
      color: var(--charcoal);
      line-height: 1.2;
    }
    .stickers-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 14px;
      margin-top: 16px;
    }
    .sticker-card {
      background: #FFF;
      border-radius: 20px;
      padding: 12px;
      text-align: center;
      box-shadow: 0 4px 12px rgba(0,0,0,0.05);
      border: 1px solid var(--border);
    }
    .sticker-preview {
      width: 100%;
      aspect-ratio: 1;
      border-radius: 16px;
      background: #F9F7F4;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 8px;
      position: relative;
      overflow: hidden;
    }
    .sticker-preview img {
      width: 85%;
      height: 85%;
      object-fit: contain;
      filter: drop-shadow(0 6px 12px rgba(0,0,0,0.2));
    }
    .btn-create {
      width: 100%;
      padding: 16px;
      border-radius: 16px;
      background: var(--charcoal);
      color: #FFF;
      font-size: 16px;
      font-weight: 800;
      border: none;
      cursor: pointer;
      margin: 16px 0;
      box-shadow: 0 4px 14px rgba(0,0,0,0.15);
    }
    .footer {
      text-align: center;
      margin-top: 36px;
      font-size: 13px;
      color: var(--muted);
      font-weight: 600;
    }
  </style>
</head>
<body>

  <div class="header-banner">
    <div class="top-bar">
      <div class="profile-badge">
        <img class="profile-avatar" src="/assets/elias-profile.jpg" alt="Elias" onerror="this.src='/elias_avatar.png'">
        <div>
          <div class="profile-name">Elias</div>
          <div class="profile-sub">Verified Developer</div>
        </div>
      </div>
      <div>
        <span style="background: #FFF; padding: 6px 12px; border-radius: 20px; font-weight: 700; font-size: 12px; box-shadow: 0 2px 6px rgba(0,0,0,0.06);">🌐 বাংলা / EN</span>
      </div>
    </div>
    <div class="app-title">ELIAS STICKERS</div>
    <div class="app-tagline">Turn Any Photo into a Custom Sticker Set with AI</div>
  </div>

  <div class="download-hero-card">
    <h3 style="font-size: 18px; font-weight: 800; margin-bottom: 6px;">📱 Android APK Installer Ready!</h3>
    <p style="font-size: 13px; color: var(--muted); margin-bottom: 14px;">গিটহাবের কোনো ঝামেলা ছাড়াই সরাসরি আপনার ফোনে অ্যাপটি ইনস্টল করুন:</p>
    <a href="/Elias-Stickers.apk" class="btn-download-apk" download="Elias-Stickers.apk">
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg>
      Download Elias-Stickers.apk (22 MB)
    </a>
    <div class="apk-meta">
      <span>✔️ Version: 1.0.0</span>
      <span>✔️ Package: com.elias.stickers</span>
      <span>✔️ Android 8.0+ Ready</span>
    </div>
  </div>

  <div class="container">
    <div class="action-buttons">
      <button class="action-btn" onclick="document.getElementById('photoInput').click()">
        📷 Take Photo
      </button>
      <button class="action-btn" onclick="document.getElementById('photoInput').click()">
        ☁️ Upload Photo
      </button>
      <input type="file" id="photoInput" accept="image/*" style="display:none" onchange="handlePhotoUpload(this)">
    </div>

    <div class="section-title">Choose a style</div>
    <div class="styles-scroll" id="stylesContainer"></div>

    <button class="btn-create" onclick="generateStickers()">✨ Create Stickers</button>

    <div class="section-title">Your Generated Stickers</div>
    <div class="stickers-grid" id="stickersGrid"></div>

    <div class="footer">
      Created with ❤️ by Elias<br>
      Elias Stickers v1.0.0 • All Rights Reserved
    </div>
  </div>

  <script>
    const STYLES = [
      { id: 'pop_art', name: 'Pop Art', emoji: '💥' },
      { id: 'retro_matchbox', name: 'Retro Matchbox', emoji: '📦' },
      { id: 'cartoon_dino', name: 'Cartoon Dino', emoji: '🦖' },
      { id: 'pixel_art', name: 'Pixel Art', emoji: '👾' },
      { id: 'royal', name: 'Royal', emoji: '👑' },
      { id: 'football', name: 'Football', emoji: '⚽' },
      { id: 'clay_3d', name: 'Clay 3D', emoji: '🎨' },
      { id: 'anime', name: 'Anime', emoji: '✨' },
      { id: 'chibi', name: 'Chibi', emoji: '🧸' },
      { id: 'watercolor', name: 'Watercolor', emoji: '🖌️' },
      { id: 'comic_book', name: 'Comic Book', emoji: '🗯️' },
      { id: 'bengali_comic', name: 'Bengali Comic', emoji: '📚' }
    ];

    const EMOTIONS = [
      { id: 'happy', name: 'Happy', badge: 'HAPPY! YAHOO!', color: '#F59E0B' },
      { id: 'sad', name: 'Sad', badge: 'TEARS... SOB!', color: '#3B82F6' },
      { id: 'angry', name: 'Angry', badge: 'AAARGH! #*&!', color: '#EF4444' },
      { id: 'surprised', name: 'Surprised', badge: 'OMG! GASP!', color: '#8B5CF6' },
      { id: 'laughing', name: 'Laughing', badge: 'HAHAHA! LOL!', color: '#10B981' },
      { id: 'love', name: 'Love', badge: 'LOVE! MY HEART!', color: '#EC4899' },
      { id: 'winking', name: 'Winking', badge: 'WINK! COOL!', color: '#F97316' },
      { id: 'confused', name: 'Confused', badge: 'HUH?! WHAT?', color: '#6366F1' }
    ];

    let selectedStyle = 'pop_art';
    let userPhotoSrc = '/assets/elias-profile.jpg';

    function initStyles() {
      const container = document.getElementById('stylesContainer');
      container.innerHTML = STYLES.map((s, idx) => `
        <div class="style-card ${s.id === selectedStyle ? 'active' : ''}" onclick="selectStyle('${s.id}')">
          <div class="style-icon-box">${s.emoji}</div>
          <div class="style-name">${s.name}</div>
        </div>
      `).join('');
    }

    function selectStyle(id) {
      selectedStyle = id;
      initStyles();
    }

    function handlePhotoUpload(input) {
      if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
          userPhotoSrc = e.target.result;
          generateStickers();
        };
        reader.readAsDataURL(input.files[0]);
      }
    }

    function generateStickers() {
      const grid = document.getElementById('stickersGrid');
      grid.innerHTML = EMOTIONS.map(e => `
        <div class="sticker-card">
          <div class="sticker-preview">
            <svg viewBox="0 0 200 200" width="100%" height="100%">
              <!-- White Die-Cut Contour Outer Border -->
              <circle cx="100" cy="88" r="62" fill="#FFFFFF" filter="drop-shadow(0 6px 8px rgba(0,0,0,0.18))" />
              <!-- Clip path for circular photo -->
              <defs>
                <clipPath id="clip-${e.id}">
                  <circle cx="100" cy="88" r="54" />
                </clipPath>
              </defs>
              <image href="${userPhotoSrc}" x="40" y="28" width="120" height="120" clip-path="url(#clip-${e.id})" preserveAspectRatio="xMidYMid slice" />
              <!-- Comic Emotion Banner with thick white border -->
              <rect x="18" y="146" width="164" height="34" rx="14" fill="#FFFFFF" />
              <rect x="22" y="150" width="156" height="26" rx="10" fill="${e.color}" />
              <text x="100" y="168" fill="#FFFFFF" font-size="12" font-weight="900" font-family="sans-serif" text-anchor="middle">${e.badge}</text>
            </svg>
          </div>
          <div style="font-weight: 700; font-size: 13px; margin: 4px 0;">${e.name}</div>
          <button style="padding: 6px 14px; border-radius: 12px; border: 1px solid var(--charcoal); background: #FFF; font-weight: 700; font-size: 11px; cursor: pointer;" onclick="alert('Sticker downloaded!')">
            ⬇️ Download PNG
          </button>
        </div>
      `).join('');
    }

    initStyles();
    generateStickers();
  </script>
</body>
</html>
"""

class SimpleServer(BaseHTTPRequestHandler):
    def do_HEAD(self):
        self.do_GET(head_only=True)

    def do_GET(self, head_only=False):
        url_path = urllib.parse.urlparse(self.path).path

        if url_path in ["/Elias-Stickers.apk", "/app-release.apk", "/download"]:
            if os.path.exists(APK_PATH):
                file_size = os.path.getsize(APK_PATH)
                self.send_response(200)
                self.send_header("Content-Type", "application/vnd.android.package-archive")
                self.send_header("Content-Disposition", 'attachment; filename="Elias-Stickers.apk"')
                self.send_header("Content-Length", str(file_size))
                self.send_header("Cache-Control", "no-cache")
                self.end_headers()
                if not head_only:
                    with open(APK_PATH, "rb") as f:
                        while chunk := f.read(65536):
                            self.wfile.write(chunk)
                return
            else:
                self.send_error(404, "APK not found")
                return

        elif url_path == "/assets/elias-profile.jpg":
            asset_path = "/app/applet/app/src/main/assets/elias-profile.jpg"
            if not os.path.exists(asset_path):
                asset_path = "./app/src/main/assets/elias-profile.jpg"
            if os.path.exists(asset_path):
                file_size = os.path.getsize(asset_path)
                self.send_response(200)
                self.send_header("Content-Type", "image/jpeg")
                self.send_header("Content-Length", str(file_size))
                self.end_headers()
                if not head_only:
                    with open(asset_path, "rb") as f:
                        self.wfile.write(f.read())
                return
            else:
                self.send_error(404, "Asset not found")
                return

        # Default serve landing page
        content_bytes = HTML_TEMPLATE.encode("utf-8")
        self.send_response(200)
        self.send_header("Content-Type", "text/html; charset=utf-8")
        self.send_header("Content-Length", str(len(content_bytes)))
        self.end_headers()
        if not head_only:
            self.wfile.write(content_bytes)

    def log_message(self, format, *args):
        pass

def run():
    server_address = ("0.0.0.0", PORT)
    httpd = HTTPServer(server_address, SimpleServer)
    print(f"Elias Stickers Web & APK Server listening on port {PORT}")
    httpd.serve_forever()

if __name__ == "__main__":
    run()
