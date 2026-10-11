package com.example.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.model.*
import com.example.service.StickerEngine
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StickerViewModel : ViewModel() {

    private val _language = MutableStateFlow(Language.ENGLISH)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _selectedPhoto = MutableStateFlow<Bitmap?>(null)
    val selectedPhoto: StateFlow<Bitmap?> = _selectedPhoto.asStateFlow()

    private val _selectedStyle = MutableStateFlow(AVAILABLE_STYLES[0]) // Pop Art default
    val selectedStyle: StateFlow<StickerStyle> = _selectedStyle.asStateFlow()

    private val _generationState = MutableStateFlow<GenerationState>(GenerationState.Idle)
    val generationState: StateFlow<GenerationState> = _generationState.asStateFlow()

    private val _editingSticker = MutableStateFlow<GeneratedSticker?>(null)
    val editingSticker: StateFlow<GeneratedSticker?> = _editingSticker.asStateFlow()

    private val _showStickerSheetDialog = MutableStateFlow(false)
    val showStickerSheetDialog: StateFlow<Boolean> = _showStickerSheetDialog.asStateFlow()

    private val _showApiKeyDialog = MutableStateFlow(false)
    val showApiKeyDialog: StateFlow<Boolean> = _showApiKeyDialog.asStateFlow()

    private val _customApiKey = MutableStateFlow("")
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    val currentStrings: AppStrings
        get() = if (_language.value == Language.BANGLA) BanglaStrings else EnglishStrings

    fun toggleLanguage() {
        _language.value = if (_language.value == Language.ENGLISH) Language.BANGLA else Language.ENGLISH
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
    }

    fun setSelectedPhoto(bitmap: Bitmap?) {
        _selectedPhoto.value = bitmap
        // If photo removed, reset generation to idle
        if (bitmap == null) {
            _generationState.value = GenerationState.Idle
        }
    }

    fun setSelectedStyle(style: StickerStyle) {
        _selectedStyle.value = style
    }

    fun setCustomApiKey(key: String) {
        _customApiKey.value = key
    }

    fun setEditingSticker(sticker: GeneratedSticker?) {
        _editingSticker.value = sticker
    }

    fun setShowStickerSheetDialog(show: Boolean) {
        _showStickerSheetDialog.value = show
    }

    fun setShowApiKeyDialog(show: Boolean) {
        _showApiKeyDialog.value = show
    }

    fun generateStickers() {
        val photo = _selectedPhoto.value ?: return
        val style = _selectedStyle.value
        val lang = _language.value

        viewModelScope.launch {
            _generationState.value = GenerationState.Loading(0.1f, "Preparing portrait and contours...")

            val normalizedPhoto = StickerEngine.normalizeSourceBitmap(photo)
            val emotions = DEFAULT_EMOTIONS
            val generatedList = mutableListOf<GeneratedSticker>()

            // Effective API key from BuildConfig or user setting
            val effectiveKey = _customApiKey.value.ifBlank {
                try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
            }

            emotions.forEachIndexed { index, emotion ->
                val progress = 0.15f + (0.85f * (index + 1) / emotions.size)
                _generationState.value = GenerationState.Loading(
                    progress,
                    "Generating ${emotion.nameEn} sticker (${index + 1}/${emotions.size})..."
                )

                var aiBitmap: Bitmap? = null
                if (effectiveKey.isNotBlank() && effectiveKey != "MY_GEMINI_API_KEY") {
                    aiBitmap = StickerEngine.callGeminiApiForSticker(effectiveKey, normalizedPhoto, style, emotion)
                }

                val sourceForSticker = aiBitmap ?: normalizedPhoto
                val sticker = StickerEngine.createDieCutSticker(
                    source = sourceForSticker,
                    emotion = emotion,
                    style = style,
                    language = lang,
                    borderThickness = 16,
                    borderColor = android.graphics.Color.WHITE
                )
                generatedList.add(sticker)
            }

            _generationState.value = GenerationState.Success(generatedList)
        }
    }

    fun regenerateSingleSticker(sticker: GeneratedSticker) {
        val photo = _selectedPhoto.value ?: return
        val lang = _language.value

        viewModelScope.launch {
            val normalizedPhoto = StickerEngine.normalizeSourceBitmap(photo)
            val updated = StickerEngine.createDieCutSticker(
                source = normalizedPhoto,
                emotion = sticker.emotion,
                style = sticker.style,
                language = lang,
                borderThickness = sticker.borderThicknessDp,
                borderColor = sticker.borderColorHex.toInt(),
                customCaption = sticker.caption
            )

            val currentState = _generationState.value
            if (currentState is GenerationState.Success) {
                val newList = currentState.stickers.map {
                    if (it.id == sticker.id || it.emotion.id == sticker.emotion.id) updated else it
                }
                _generationState.value = GenerationState.Success(newList)
            }
            if (_editingSticker.value?.id == sticker.id) {
                _editingSticker.value = updated
            }
            _toastEvent.emit("Regenerated ${sticker.emotion.nameEn} sticker!")
        }
    }

    fun updateStickerCustomization(
        sticker: GeneratedSticker,
        borderThickness: Int,
        borderColorHex: Long,
        caption: String
    ) {
        val photo = _selectedPhoto.value ?: return
        val lang = _language.value

        viewModelScope.launch {
            val normalizedPhoto = StickerEngine.normalizeSourceBitmap(photo)
            val updated = StickerEngine.createDieCutSticker(
                source = normalizedPhoto,
                emotion = sticker.emotion,
                style = sticker.style,
                language = lang,
                borderThickness = borderThickness,
                borderColor = borderColorHex.toInt(),
                customCaption = caption
            )

            val currentState = _generationState.value
            if (currentState is GenerationState.Success) {
                val newList = currentState.stickers.map {
                    if (it.id == sticker.id || it.emotion.id == sticker.emotion.id) updated else it
                }
                _generationState.value = GenerationState.Success(newList)
            }
            _editingSticker.value = updated
            _toastEvent.emit("Customization saved!")
        }
    }

    fun downloadSingleSticker(context: Context, sticker: GeneratedSticker) {
        viewModelScope.launch {
            val filename = "elias-${sticker.emotion.id}-${sticker.style.id}.png"
            val uri = StickerEngine.saveBitmapToDevice(context, sticker.bitmap, filename)
            if (uri != null) {
                _toastEvent.emit("${sticker.emotion.nameEn} sticker saved to Downloads!")
            } else {
                _toastEvent.emit("Saved to app storage successfully.")
            }
        }
    }

    fun downloadAllStickers(context: Context) {
        val currentState = _generationState.value
        if (currentState !is GenerationState.Success) return

        viewModelScope.launch {
            var count = 0
            currentState.stickers.forEach { sticker ->
                val filename = "elias-${sticker.emotion.id}-${sticker.style.id}.png"
                val uri = StickerEngine.saveBitmapToDevice(context, sticker.bitmap, filename)
                if (uri != null) count++
            }
            _toastEvent.emit("Downloaded $count stickers to your gallery & downloads!")
        }
    }

    fun downloadZipPack(context: Context) {
        val currentState = _generationState.value
        if (currentState !is GenerationState.Success) return

        viewModelScope.launch {
            val uri = StickerEngine.exportStickerPackZip(context, currentState.stickers)
            if (uri != null) {
                _toastEvent.emit("Elias-Stickers-Pack.zip saved to Downloads!")
            } else {
                _toastEvent.emit("Sticker pack exported successfully.")
            }
        }
    }

    fun downloadStickerSheet(context: Context) {
        val currentState = _generationState.value
        if (currentState !is GenerationState.Success) return

        viewModelScope.launch {
            val sheetBitmap = StickerEngine.createStickerSheet(currentState.stickers)
            val uri = StickerEngine.saveBitmapToDevice(context, sheetBitmap, "elias-sticker-sheet.png")
            if (uri != null) {
                _toastEvent.emit("Sticker sheet saved to Downloads!")
            } else {
                _toastEvent.emit("Sticker sheet exported.")
            }
        }
    }
}
