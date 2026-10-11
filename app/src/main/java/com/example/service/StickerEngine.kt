package com.example.service

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import com.example.model.GeneratedSticker
import com.example.model.Language
import com.example.model.StickerEmotion
import com.example.model.StickerStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object StickerEngine {

    /**
     * Prepares and normalizes the input photo to a square 600x600 bitmap.
     */
    fun normalizeSourceBitmap(source: Bitmap, targetSize: Int = 600): Bitmap {
        val width = source.width
        val height = source.height
        val minDim = width.coerceAtMost(height)
        val xOffset = (width - minDim) / 2
        val yOffset = (height - minDim) / 2

        val square = Bitmap.createBitmap(source, xOffset, yOffset, minDim, minDim)
        return Bitmap.createScaledBitmap(square, targetSize, targetSize, true)
    }

    /**
     * Applies artistic styling to the portrait according to selected style.
     */
    fun applyArtStyle(source: Bitmap, style: StickerStyle): Bitmap {
        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        when (style.id) {
            "pixel_art" -> {
                // Downscale to 64x64 then upscale nearest neighbor
                val down = Bitmap.createScaledBitmap(source, 64, 64, false)
                val up = Bitmap.createScaledBitmap(down, source.width, source.height, false)
                canvas.drawBitmap(up, 0f, 0f, paint)
            }
            "pop_art" -> {
                // High contrast, saturated color matrix
                val cm = ColorMatrix(floatArrayOf(
                    1.6f, 0f, 0f, 0f, 15f,
                    0f, 1.5f, 0f, 0f, 10f,
                    0f, 0f, 1.8f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                ))
                paint.colorFilter = ColorMatrixColorFilter(cm)
                canvas.drawBitmap(source, 0f, 0f, paint)
            }
            "retro_matchbox" -> {
                // Warm vintage sepia tone
                val cm = ColorMatrix()
                cm.setSaturation(0.7f)
                val sepia = ColorMatrix(floatArrayOf(
                    1.1f, 0.1f, 0f, 0f, 25f,
                    0.1f, 0.95f, 0f, 0f, 15f,
                    0f, 0.1f, 0.8f, 0f, -10f,
                    0f, 0f, 0f, 1f, 0f
                ))
                cm.postConcat(sepia)
                paint.colorFilter = ColorMatrixColorFilter(cm)
                canvas.drawBitmap(source, 0f, 0f, paint)
            }
            "watercolor" -> {
                // Soft painterly tone
                val cm = ColorMatrix()
                cm.setSaturation(1.2f)
                paint.colorFilter = ColorMatrixColorFilter(cm)
                canvas.drawBitmap(source, 0f, 0f, paint)
            }
            "comic_book", "bengali_comic" -> {
                // Punchy comic contrast
                val cm = ColorMatrix(floatArrayOf(
                    1.4f, 0f, 0f, 0f, 10f,
                    0f, 1.3f, 0f, 0f, 5f,
                    0f, 0f, 1.4f, 0f, 15f,
                    0f, 0f, 0f, 1f, 0f
                ))
                paint.colorFilter = ColorMatrixColorFilter(cm)
                canvas.drawBitmap(source, 0f, 0f, paint)
            }
            else -> {
                // Default high vibrance
                val cm = ColorMatrix()
                cm.setSaturation(1.3f)
                paint.colorFilter = ColorMatrixColorFilter(cm)
                canvas.drawBitmap(source, 0f, 0f, paint)
            }
        }

        return output
    }

    /**
     * Builds a die-cut sticker with a solid white contour border, drop shadow,
     * portrait cutout, and dynamic emotion comic badge!
     */
    fun createDieCutSticker(
        source: Bitmap,
        emotion: StickerEmotion,
        style: StickerStyle,
        language: Language,
        borderThickness: Int = 16,
        borderColor: Int = Color.WHITE,
        customCaption: String = ""
    ): GeneratedSticker {
        val size = 600
        val styledSubject = applyArtStyle(source, style)

        val stickerBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(stickerBitmap)

        // 1. Create masked circular/portrait silhouette
        val subjectMasked = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val maskCanvas = Canvas(subjectMasked)
        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        val center = size / 2f
        val radius = size * 0.36f

        // Draw portrait in masked rounded region
        maskPaint.color = Color.BLACK
        maskCanvas.drawCircle(center, center - 20f, radius, maskPaint)

        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        val srcRect = Rect(0, 0, styledSubject.width, styledSubject.height)
        val destRect = RectF(center - radius, center - 20f - radius, center + radius, center - 20f + radius)
        maskCanvas.drawBitmap(styledSubject, srcRect, destRect, maskPaint)
        maskPaint.xfermode = null

        // 2. Die-cut white border + drop shadow behind subject
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = borderColor
            this.style = Paint.Style.FILL
            setShadowLayer(14f, 0f, 8f, Color.argb(90, 0, 0, 0))
        }

        // Outer sticker silhouette circle with extra border padding
        canvas.drawCircle(center, center - 20f, radius + borderThickness, borderPaint)
        borderPaint.clearShadowLayer()

        // 3. Draw the masked subject onto sticker canvas
        canvas.drawBitmap(subjectMasked, 0f, 0f, null)

        // 4. Draw Emotion Comic Banner Badge at bottom
        drawEmotionComicBanner(canvas, center, size, emotion, language, customCaption)

        // 5. Draw decorative emotion accents (hearts, tears, lightning, stars)
        drawEmotionAccents(canvas, center, size, emotion)

        // Encode as transparent PNG bytes
        val stream = ByteArrayOutputStream()
        stickerBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val pngBytes = stream.toByteArray()

        return GeneratedSticker(
            id = "${style.id}_${emotion.id}_${System.currentTimeMillis()}",
            emotion = emotion,
            style = style,
            bitmap = stickerBitmap,
            transparentPngBytes = pngBytes,
            caption = customCaption,
            borderThicknessDp = borderThickness,
            borderColorHex = borderColor.toLong()
        )
    }

    private fun drawEmotionComicBanner(
        canvas: Canvas,
        centerX: Float,
        size: Int,
        emotion: StickerEmotion,
        language: Language,
        customCaption: String
    ) {
        val bannerText = if (customCaption.isNotBlank()) {
            customCaption.uppercase()
        } else if (language == Language.BANGLA) {
            emotion.comicBadgeBn
        } else {
            emotion.comicBadgeEn
        }

        val bannerY = size * 0.82f
        val bannerWidth = size * 0.76f
        val bannerHeight = size * 0.16f

        val rect = RectF(
            centerX - bannerWidth / 2f,
            bannerY - bannerHeight / 2f,
            centerX + bannerWidth / 2f,
            bannerY + bannerHeight / 2f
        )

        // Outer white border for the comic badge
        val badgeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            this.style = Paint.Style.FILL
            setShadowLayer(8f, 0f, 4f, Color.argb(100, 0, 0, 0))
        }
        canvas.drawRoundRect(
            RectF(rect.left - 6f, rect.top - 6f, rect.right + 6f, rect.bottom + 6f),
            24f, 24f, badgeBorderPaint
        )

        // Filled comic banner
        val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = emotion.badgeColorHex.toInt()
            this.style = Paint.Style.FILL
        }
        canvas.drawRoundRect(rect, 20f, 20f, badgeBgPaint)

        // Badge inner border accent
        val badgeStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            this.style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawRoundRect(
            RectF(rect.left + 4f, rect.top + 4f, rect.right - 4f, rect.bottom - 4f),
            16f, 16f, badgeStrokePaint
        )

        // Bold comic text with outline
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val textY = rect.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2f)

        // Text black drop shadow / outline
        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.STROKE
        textPaint.strokeWidth = 6f
        canvas.drawText(bannerText, centerX, textY, textPaint)

        // Text white fill
        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        canvas.drawText(bannerText, centerX, textY, textPaint)
    }

    private fun drawEmotionAccents(
        canvas: Canvas,
        centerX: Float,
        size: Int,
        emotion: StickerEmotion
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        when (emotion.id) {
            "love" -> {
                // Floating hearts top left & top right
                paint.color = Color.rgb(236, 72, 153)
                canvas.drawCircle(centerX - 130f, 110f, 18f, paint)
                canvas.drawCircle(centerX - 110f, 110f, 18f, paint)
                val path = Path().apply {
                    moveTo(centerX - 145f, 115f)
                    lineTo(centerX - 120f, 145f)
                    lineTo(centerX - 95f, 115f)
                    close()
                }
                canvas.drawPath(path, paint)
            }
            "angry" -> {
                // Lightning bolt icon top-right
                paint.color = Color.rgb(239, 68, 68)
                val path = Path().apply {
                    moveTo(centerX + 110f, 80f)
                    lineTo(centerX + 135f, 80f)
                    lineTo(centerX + 120f, 105f)
                    lineTo(centerX + 140f, 105f)
                    lineTo(centerX + 105f, 145f)
                    lineTo(centerX + 118f, 115f)
                    lineTo(centerX + 100f, 115f)
                    close()
                }
                canvas.drawPath(path, paint)
            }
            "surprised", "happy", "winking" -> {
                // Starburst sparkle accent
                paint.color = Color.rgb(253, 224, 71)
                val sx = centerX + 120f
                val sy = 110f
                val star = Path().apply {
                    moveTo(sx, sy - 20f)
                    lineTo(sx + 6f, sy - 6f)
                    lineTo(sx + 20f, sy)
                    lineTo(sx + 6f, sy + 6f)
                    lineTo(sx, sy + 20f)
                    lineTo(sx - 6f, sy + 6f)
                    lineTo(sx - 20f, sy)
                    lineTo(sx - 6f, sy - 6f)
                    close()
                }
                canvas.drawPath(star, paint)
            }
            "sad", "crying" -> {
                // Tear drops
                paint.color = Color.rgb(56, 189, 248)
                canvas.drawCircle(centerX - 110f, 130f, 12f, paint)
                val tear = Path().apply {
                    moveTo(centerX - 110f, 105f)
                    lineTo(centerX - 98f, 126f)
                    lineTo(centerX - 122f, 126f)
                    close()
                }
                canvas.drawPath(tear, paint)
            }
        }
    }

    /**
     * Calls Gemini API (gemini-2.5-flash-image) with user photo and prompt.
     */
    suspend fun callGeminiApiForSticker(
        apiKey: String,
        source: Bitmap,
        style: StickerStyle,
        emotion: StickerEmotion
    ): Bitmap? = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) return@withContext null

        try {
            val bos = ByteArrayOutputStream()
            source.compress(Bitmap.CompressFormat.JPEG, 85, bos)
            val base64Image = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP)

            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-image:generateContent?key=$apiKey"
            val url = URL(endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 40000
            conn.readTimeout = 40000

            val prompt = "Create a custom die-cut sticker of the person in this photo in '${style.nameEn}' style with a '${emotion.nameEn}' emotion. Crisp die-cut white contour border around the character, transparent or white clean background, high resolution cartoon sticker art."

            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                            put(JSONObject().put("inlineData", JSONObject().apply {
                                put("mimeType", "image/jpeg")
                                put("data", base64Image)
                            }))
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
                put("generationConfig", JSONObject().apply {
                    put("imageConfig", JSONObject().apply {
                        put("aspectRatio", "1:1")
                        put("imageSize", "1K")
                    })
                    put("responseModalities", JSONArray().apply {
                        put("TEXT")
                        put("IMAGE")
                    })
                })
            }

            conn.outputStream.use { os ->
                os.write(jsonBody.toString().toByteArray())
            }

            if (conn.responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(responseText)
                val candidates = root.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val parts = candidates.getJSONObject(0).optJSONObject("content")?.optJSONArray("parts")
                    if (parts != null) {
                        for (i in 0 until parts.length()) {
                            val part = parts.getJSONObject(i)
                            val inlineData = part.optJSONObject("inlineData")
                            if (inlineData != null) {
                                val b64Data = inlineData.optString("data")
                                if (b64Data.isNotBlank()) {
                                    val bytes = Base64.decode(b64Data, Base64.DEFAULT)
                                    return@withContext BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                }
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // Handled gracefully, engine uses local high-res renderer
        }
        return@withContext null
    }

    /**
     * Saves a sticker bitmap to device MediaStore (Pictures/EliasStickers) or Downloads.
     */
    suspend fun saveBitmapToDevice(context: Context, bitmap: Bitmap, fileName: String): Uri? = withContext(Dispatchers.IO) {
        val cleanName = if (fileName.endsWith(".png")) fileName else "$fileName.png"
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, cleanName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/EliasStickers")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    context.contentResolver.openOutputStream(uri)?.use { os ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    }
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    context.contentResolver.update(uri, values, null, null)
                    return@withContext uri
                }
            } else {
                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "EliasStickers")
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, cleanName)
                FileOutputStream(file).use { os ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                }
                return@withContext Uri.fromFile(file)
            }
        } catch (e: Exception) {
            // Fallback to internal/external app files dir
            val fallbackDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "EliasStickers")
            if (!fallbackDir.exists()) fallbackDir.mkdirs()
            val file = File(fallbackDir, cleanName)
            FileOutputStream(file).use { os ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            }
            return@withContext Uri.fromFile(file)
        }
        return@withContext null
    }

    /**
     * Exports all generated stickers as a ZIP archive to Downloads folder.
     */
    suspend fun exportStickerPackZip(
        context: Context,
        stickers: List<GeneratedSticker>,
        zipName: String = "Elias-Stickers-Pack.zip"
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val bos = ByteArrayOutputStream()
            ZipOutputStream(bos).use { zos ->
                stickers.forEach { sticker ->
                    val entryName = "elias-${sticker.emotion.id}-${sticker.style.id}.png"
                    zos.putNextEntry(ZipEntry(entryName))
                    zos.write(sticker.transparentPngBytes)
                    zos.closeEntry()
                }
            }
            val zipBytes = bos.toByteArray()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, zipName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/zip")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    context.contentResolver.openOutputStream(uri)?.use { os: OutputStream ->
                        os.write(zipBytes)
                    }
                    values.clear()
                    values.put(MediaStore.Downloads.IS_PENDING, 0)
                    context.contentResolver.update(uri, values, null, null)
                    return@withContext uri
                }
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(dir, zipName)
                FileOutputStream(file).use { os -> os.write(zipBytes) }
                return@withContext Uri.fromFile(file)
            }
        } catch (_: Exception) {
            // Fallback to app directory
            val fallbackDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.cacheDir
            val file = File(fallbackDir, zipName)
            val bos = ByteArrayOutputStream()
            ZipOutputStream(bos).use { zos ->
                stickers.forEach { sticker ->
                    zos.putNextEntry(ZipEntry("elias-${sticker.emotion.id}.png"))
                    zos.write(sticker.transparentPngBytes)
                    zos.closeEntry()
                }
            }
            FileOutputStream(file).use { it.write(bos.toByteArray()) }
            return@withContext Uri.fromFile(file)
        }
        return@withContext null
    }

    /**
     * Composes all generated stickers into a printable A4-ratio sheet with cutlines.
     */
    fun createStickerSheet(
        stickers: List<GeneratedSticker>,
        sheetTitle: String = "ELIAS STICKERS — OFFICIAL PACK"
    ): Bitmap {
        val width = 1240
        val height = 1754
        val sheetBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(sheetBitmap)

        // Warm Cream Background matching app aesthetic
        val bgPaint = Paint().apply { color = Color.rgb(251, 246, 238) }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Header Title
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(30, 27, 24)
            textSize = 48f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(sheetTitle, width / 2f, 100f, titlePaint)

        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(110, 93, 79)
            textSize = 24f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Turn Any Photo into a Custom Sticker Set • Created by Elias", width / 2f, 140f, subPaint)

        // Grid of Stickers (4 columns x 2 rows, or up to 8/12 stickers)
        val cols = 3
        val rows = (stickers.size + cols - 1) / cols
        val cellWidth = (width - 120) / cols
        val cellHeight = (height - 240) / rows.coerceAtLeast(1)

        val dashPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(200, 185, 170)
            style = Paint.Style.STROKE
            strokeWidth = 2f
            pathEffect = DashPathEffect(floatArrayOf(10f, 8f), 0f)
        }

        stickers.forEachIndexed { index, sticker ->
            val col = index % cols
            val row = index / cols
            val left = 60f + col * cellWidth
            val top = 180f + row * cellHeight

            // Cutting guideline box
            canvas.drawRoundRect(RectF(left + 10f, top + 10f, left + cellWidth - 10f, top + cellHeight - 10f), 16f, 16f, dashPaint)

            // Scaled sticker bitmap
            val padding = 24f
            val stickerRect = RectF(
                left + padding,
                top + padding,
                left + cellWidth - padding,
                top + cellHeight - padding - 20f
            )
            canvas.drawBitmap(sticker.bitmap, null, stickerRect, null)

            // Emotion label below
            val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(30, 27, 24)
                textSize = 20f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(sticker.emotion.nameEn, left + cellWidth / 2f, top + cellHeight - 16f, labelPaint)
        }

        return sheetBitmap
    }
}
