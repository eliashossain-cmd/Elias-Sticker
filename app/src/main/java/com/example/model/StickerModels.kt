package com.example.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

enum class Language(val displayName: String, val code: String) {
    ENGLISH("English", "en"),
    BANGLA("বাংলা", "bn")
}

data class AppStrings(
    val appTitle: String,
    val appTagline: String,
    val creatorName: String,
    val footerText: String,
    val takePhoto: String,
    val uploadPhoto: String,
    val retakePhoto: String,
    val changePhoto: String,
    val removePhoto: String,
    val chooseStyle: String,
    val createStickers: String,
    val generatingStickers: String,
    val generatingStep: String,
    val emptyTitle: String,
    val emptySubtitle: String,
    val downloadAll: String,
    val viewStickerSheet: String,
    val downloadZip: String,
    val downloadSingle: String,
    val regenerate: String,
    val editSticker: String,
    val stickerSaved: String,
    val zipSaved: String,
    val errorPhotoRequired: String,
    val errorCameraPermission: String,
    val errorCameraFailed: String,
    val retry: String,
    val customizeTitle: String,
    val borderThickness: String,
    val borderColor: String,
    val backgroundPreview: String,
    val transparent: String,
    val solidCream: String,
    val darkCheckered: String,
    val addCaption: String,
    val captionPlaceholder: String,
    val captionSize: String,
    val saveChanges: String,
    val close: String,
    val apiKeySettings: String,
    val geminiCloudMode: String,
    val localFilterMode: String,
    val enterApiKey: String,
    val saveKey: String,
    val usingKeyFromEnv: String,
    val shareSticker: String
)

val EnglishStrings = AppStrings(
    appTitle = "ELIAS STICKERS",
    appTagline = "Turn Any Photo into a Custom Sticker Set",
    creatorName = "Elias",
    footerText = "Created with ❤️ by Elias",
    takePhoto = "Take Photo",
    uploadPhoto = "Upload Photo",
    retakePhoto = "Retake Photo",
    changePhoto = "Change Photo",
    removePhoto = "Remove Photo",
    chooseStyle = "Choose a style",
    createStickers = "Create Stickers",
    generatingStickers = "Generating Stickers...",
    generatingStep = "Crafting custom emotion stickers with die-cut borders...",
    emptyTitle = "Your generated stickers will appear here.",
    emptySubtitle = "Upload a photo and choose a style to get started!",
    downloadAll = "Download all",
    viewStickerSheet = "View Sticker Sheet",
    downloadZip = "Download ZIP Pack",
    downloadSingle = "Download",
    regenerate = "Regenerate",
    editSticker = "Edit Sticker",
    stickerSaved = "Sticker saved to Downloads & Pictures!",
    zipSaved = "Sticker Pack saved as ZIP to Downloads!",
    errorPhotoRequired = "Please upload or capture a photo first!",
    errorCameraPermission = "Camera permission is needed to take a photo",
    errorCameraFailed = "Could not capture image. Please try uploading instead.",
    retry = "Try Again",
    customizeTitle = "Customize Sticker",
    borderThickness = "Border Thickness",
    borderColor = "Border Color",
    backgroundPreview = "Background Preview",
    transparent = "Transparent",
    solidCream = "Cream",
    darkCheckered = "Dark Grid",
    addCaption = "Custom Caption Text",
    captionPlaceholder = "e.g. Elias, Boss, Cool, Wow...",
    captionSize = "Caption Size",
    saveChanges = "Apply & Save",
    close = "Close",
    apiKeySettings = "AI Settings",
    geminiCloudMode = "Gemini AI Mode (Cloud)",
    localFilterMode = "Instant Studio Mode (Fast)",
    enterApiKey = "Enter Gemini API Key (Optional)",
    saveKey = "Save Key",
    usingKeyFromEnv = "Active: Server-side Gemini API ready",
    shareSticker = "Share Sticker"
)

val BanglaStrings = AppStrings(
    appTitle = "ইলিয়াস স্টিকার্স",
    appTagline = "যেকোনো ছবি দিয়ে তৈরি করুন কাস্টম স্টিকার সেট",
    creatorName = "ইলিয়াস",
    footerText = "Created with ❤️ by Elias",
    takePhoto = "ছবি তুলুন",
    uploadPhoto = "ছবি আপলোড",
    retakePhoto = "আবার তুলুন",
    changePhoto = "ছবি পরিবর্তন",
    removePhoto = "মুছে ফেলুন",
    chooseStyle = "স্টাইল নির্বাচন করুন",
    createStickers = "স্টিকার তৈরি করুন",
    generatingStickers = "স্টিকার তৈরি হচ্ছে...",
    generatingStep = "ডাই-কাট বর্ডারসহ বিভিন্ন এক্সপ্রেশনের স্টিকার প্রস্তুত হচ্ছে...",
    emptyTitle = "আপনার তৈরি করা স্টিকারগুলো এখানে দেখা যাবে।",
    emptySubtitle = "শুরু করতে একটি ছবি আপলোড করুন এবং স্টাইল বাছাই করুন!",
    downloadAll = "সব ডাউনলোড করুন",
    viewStickerSheet = "স্টিকার শিট দেখুন",
    downloadZip = "ZIP প্যাক ডাউনলোড",
    downloadSingle = "ডাউনলোড",
    regenerate = "পুনরায় তৈরি",
    editSticker = "স্টিকার এডিট করুন",
    stickerSaved = "স্টিকার Downloads ও Pictures ফোল্ডারে সেভ হয়েছে!",
    zipSaved = "স্টিকার প্যাক ZIP হিসেবে সেভ হয়েছে!",
    errorPhotoRequired = "অনুগ্রহ করে প্রথমে একটি ছবি নির্বাচন করুন!",
    errorCameraPermission = "ছবি তোলার জন্য ক্যামেরার অনুমতি প্রয়োজন",
    errorCameraFailed = "ক্যামেরা চালু করা যায়নি। গ্যালারি থেকে ছবি দিন।",
    retry = "আবার চেষ্টা করুন",
    customizeTitle = "স্টিকার কাস্টমাইজ",
    borderThickness = "বর্ডারের পুরুত্ব",
    borderColor = "বর্ডারের রঙ",
    backgroundPreview = "ব্যাকগ্রাউন্ড প্রিভিউ",
    transparent = "ট্রান্সপারেন্ট",
    solidCream = "ক্রিম",
    darkCheckered = "ডার্ক গ্রিড",
    addCaption = "কাস্টম ক্যাপশন লিখুন",
    captionPlaceholder = "যেমন: Elias, সেরা, ওয়াও...",
    captionSize = "ক্যাপশনের আকার",
    saveChanges = "প্রয়োগ করুন",
    close = "বন্ধ করুন",
    apiKeySettings = "AI সেটিংস",
    geminiCloudMode = "জেমিনাই AI ক্লাউড মোড",
    localFilterMode = "ইনস্ট্যান্ট স্টুডিও মোড (দ্রুত)",
    enterApiKey = "Gemini API Key প্রদান করুন (ঐচ্ছিক)",
    saveKey = "সংরক্ষণ করুন",
    usingKeyFromEnv = "সক্রিয়: ক্লাউড জেমিনাই API সংযুক্ত",
    shareSticker = "শেয়ার করুন"
)

data class StickerStyle(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val badgeColorHex: Long,
    val emoji: String
)

val AVAILABLE_STYLES = listOf(
    StickerStyle("pop_art", "Pop Art", "পপ আর্ট", "Bold comic dots, vivid contrast, graphic lines", "উজ্জ্বল রঙের কমিক গ্রাফিক স্টাইল", 0xFFE11D48, "💥"),
    StickerStyle("retro_matchbox", "Retro Japanese Matchbox", "রেট্রো ম্যাচবক্স", "Vintage print texture, nostalgic paper warm tone", "ভিন্টেজ নস্টালজিক জাপানি প্রিন্ট স্টাইল", 0xFFD97706, "📦"),
    StickerStyle("cartoon_dino", "Cartoon Dino", "কার্টুন ডাইনো", "Playful dragon anime mascot, whimsical cute vibe", "মজার কিউট ডাইনোসর অ্যানিমে মাস্কট", 0xFF10B981, "🦖"),
    StickerStyle("pixel_art", "Pixel Art", "পিক্সেল আর্ট", "16-bit retro arcade pixelated gaming charm", "ক্লাসিক ১৬-বিট রেট্রো গেম পিক্সেল লুক", 0xFF8B5CF6, "👾"),
    StickerStyle("royal", "Royal", "রয়্যাল", "Regal golden crown, monarch cloak and cards", "রাজকীয় স্বর্ণমুকুট ও আভিজাত্যপূর্ণ লুক", 0xFFF59E0B, "👑"),
    StickerStyle("football", "Football Sticker", "ফুটবল স্টিকার", "Vintage sports trading card, athletic action frame", "ভিন্টেজ স্পোর্টস ট্রেডিং কার্ড স্টাইল", 0xFFEF4444, "⚽"),
    StickerStyle("clay_3d", "Clay 3D", "ক্লে ৩ডি", "Tactile stop-motion plasticine sculpted look", "হাতে গড়া নরম ক্লেমেশন ৩ডি মডেল", 0xFF06B6D4, "🎨"),
    StickerStyle("anime", "Anime", "অ্যানিমে", "Cel-shaded vibrant Japanese animation aesthetics", "জাপানি অ্যানিমেশন ও উজ্জ্বল চোখ-মুখের রূপ", 0xFFEC4899, "✨"),
    StickerStyle("chibi", "Chibi", "চিবি", "Cute super-deformed big-head kawaii chibi", "বড় মাথার মিষ্টি ও কিউট চিবি রূপ", 0xFFF43F5E, "🧸"),
    StickerStyle("watercolor", "Watercolor", "জলরং", "Soft artistic washes with natural pigment bleeds", "নরম জলরং ও শিল্পময় স্প্ল্যাশ আর্ট", 0xFF3B82F6, "🖌️"),
    StickerStyle("comic_book", "Comic Book", "কমিক বুক", "Dynamic halftone ink, dramatic action lines", "অ্যাকশন কমিক হ্যালফ-টোন প্রিন্ট স্টাইল", 0xFFEAB308, "🗯️"),
    StickerStyle("minimalist", "Minimalist", "মিনিমালিস্ট", "Clean pastel outlines, ultra-modern simplicity", "পরিচ্ছন্ন ও মার্জিত সাধারণ রেখাচিত্র", 0xFF64748B, "🪄"),
    StickerStyle("3d_cartoon", "3D Cartoon", "৩ডি কার্টুন", "Pixar-inspired stylized cinematic 3D character", "পিক্সার ঘরানার আকর্ষণীয় থ্রি-ডি চরিত্র", 0xFF0EA5E9, "🎬"),
    StickerStyle("realistic_portrait", "Realistic Portrait Sticker", "রিয়েলিস্টিক পোর্ট্রেট", "Stylized photo with ultra-thick die-cut cutout", "বাস্তবধর্মী ছবিসহ স্পষ্ট ডাই-কাট স্টিকার", 0xFF14B8A6, "📸"),
    StickerStyle("bengali_comic", "Bengali Comic Style", "বাংলা কমিক স্টাইল", "Classic boi-mela illustration comic aesthetic", "ঐতিহ্যবাহী বাংলা কমিক ও চিত্রণ ধারা", 0xFF84CC16, "📚")
)

data class StickerEmotion(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val comicBadgeEn: String,
    val comicBadgeBn: String,
    val badgeColorHex: Long,
    val secondaryColorHex: Long,
    val emoji: String
)

val DEFAULT_EMOTIONS = listOf(
    StickerEmotion("happy", "Happy", "আনন্দিত", "HAPPY! YAHOO!", "আনন্দ! ইয়াহু!", 0xFFF59E0B, 0xFFFEF3C7, "😄"),
    StickerEmotion("sad", "Sad", "দুঃখিত", "TEARS... SOB!", "চোখের জল... উফ!", 0xFF3B82F6, 0xFFDBEAFE, "😢"),
    StickerEmotion("angry", "Angry", "রাগান্বিত", "AAARGH! #*&!", "রাগ! ওরে বাবা!", 0xFFEF4444, 0xFFFEE2E2, "😡"),
    StickerEmotion("surprised", "Surprised", "বিস্মিত", "OMG! GASP!", "বিস্ময়! ওয়াও!", 0xFF8B5CF6, 0xFFEDE9FE, "😲"),
    StickerEmotion("laughing", "Laughing", "হাস্যোজ্জ্বল", "HAHAHA! LOL!", "হাহা! খিলখিল!", 0xFF10B981, 0xFFD1FAE5, "😂"),
    StickerEmotion("love", "Love", "ভালোবাসা", "LOVE! MY HEART!", "ভালোবাসা! হৃদয়!", 0xFFEC4899, 0xFFFCE7F3, "❤️"),
    StickerEmotion("winking", "Winking", "ইশারা", "WINK! COOL!", "ইশারা! দারুণ!", 0xFFF97316, 0xFFFFEDD5, "😉"),
    StickerEmotion("confused", "Confused", "বিভ্রান্ত", "HUH?! WHAT?", "কী?! বুঝলাম না!", 0xFF6366F1, 0xFFE0E7FF, "🤔")
)

val EXTRA_EMOTIONS = listOf(
    StickerEmotion("crying", "Crying", "কান্নাকাটি", "SOB! WHY?!", "কান্না! কেন?!", 0xFF0284C7, 0xFFE0F2FE, "😭"),
    StickerEmotion("excited", "Excited", "উত্তেজিত", "YESSS! LET'S GO!", "সাবাশ! এগিয়ে চল!", 0xFFD97706, 0xFFFEF3C7, "🤩"),
    StickerEmotion("thinking", "Thinking", "চিন্তিত", "HMM... LET'S SEE", "হুম... দেখা যাক", 0xFF64748B, 0xFFF1F5F9, "🧐"),
    StickerEmotion("sleepy", "Sleepy", "ঘুমকাতুরে", "ZZZ... SLEEPY", "ঘুম পাচ্ছে... zzz", 0xFF475569, 0xFFE2E8F0, "😴"),
    StickerEmotion("shy", "Shy", "লাজুক", "BLUSH! AWW", "লজ্জা! আহারে", 0xFFFB7185, 0xFFFFE4E6, "😳"),
    StickerEmotion("thumbs_up", "Thumbs Up", "সাবাশ", "GREAT JOB!", "দারুণ কাজ!", 0xFF059669, 0xFFD1FAE5, "👍"),
    StickerEmotion("facepalm", "Facepalm", "হতাশা", "OH NO! NO WAY", "হায়রে! কী হলো", 0xFF9333EA, 0xFFF3E8FF, "🤦"),
    StickerEmotion("thank_you", "Thank You", "ধন্যবাদ", "THANKS A LOT!", "অনেক ধন্যবাদ!", 0xFF0D9488, 0xFFCCFBF1, "🙏")
)

data class GeneratedSticker(
    val id: String,
    val emotion: StickerEmotion,
    val style: StickerStyle,
    val bitmap: Bitmap,
    val transparentPngBytes: ByteArray,
    val caption: String = "",
    val borderThicknessDp: Int = 10,
    val borderColorHex: Long = 0xFFFFFFFF
)

sealed class GenerationState {
    object Idle : GenerationState()
    data class Loading(val progress: Float, val statusMessage: String) : GenerationState()
    data class Success(val stickers: List<GeneratedSticker>) : GenerationState()
    data class Error(val errorMessage: String) : GenerationState()
}
