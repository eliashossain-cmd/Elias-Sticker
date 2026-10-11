package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.GenerationState
import com.example.ui.theme.CreamBackground
import com.example.viewmodel.StickerViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StickerAppScreen(
    viewModel: StickerViewModel = viewModel()
) {
    val context = LocalContext.current

    val currentLanguage by viewModel.language.collectAsStateWithLifecycle()
    val selectedPhoto by viewModel.selectedPhoto.collectAsStateWithLifecycle()
    val selectedStyle by viewModel.selectedStyle.collectAsStateWithLifecycle()
    val generationState by viewModel.generationState.collectAsStateWithLifecycle()
    val editingSticker by viewModel.editingSticker.collectAsStateWithLifecycle()
    val showStickerSheetDialog by viewModel.showStickerSheetDialog.collectAsStateWithLifecycle()
    val showApiKeyDialog by viewModel.showApiKeyDialog.collectAsStateWithLifecycle()
    val customApiKey by viewModel.customApiKey.collectAsStateWithLifecycle()

    val strings = viewModel.currentStrings

    // Listen to toast messages
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        containerColor = CreamBackground,
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CreamBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Header & Hero Section with Elias circular avatar and branding
            HeaderHeroSection(
                strings = strings,
                currentLanguage = currentLanguage,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onOpenApiKeyDialog = { viewModel.setShowApiKeyDialog(true) }
            )

            // 2. Photo Upload & Camera System
            PhotoUploadSection(
                strings = strings,
                selectedPhoto = selectedPhoto,
                onPhotoSelected = { bitmap -> viewModel.setSelectedPhoto(bitmap) },
                onError = { errorMsg -> Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show() }
            )

            // 3. Choose a Style (15 Styles Horizontal Carousel)
            StyleSelectorSection(
                strings = strings,
                currentLanguage = currentLanguage,
                selectedStyle = selectedStyle,
                onStyleSelected = { style -> viewModel.setSelectedStyle(style) }
            )

            // 4. Create Stickers Button & Progress Indicator
            CreateButtonSection(
                strings = strings,
                isPhotoSelected = selectedPhoto != null,
                generationState = generationState,
                onCreateClick = { viewModel.generateStickers() }
            )

            // 5. Sticker Gallery (Empty state, 2-column grid, Download all, View Sheet, ZIP Export)
            StickerGallerySection(
                strings = strings,
                currentLanguage = currentLanguage,
                generationState = generationState,
                onStickerClick = { sticker -> viewModel.setEditingSticker(sticker) },
                onDownloadSingle = { sticker -> viewModel.downloadSingleSticker(context, sticker) },
                onRegenerateSingle = { sticker -> viewModel.regenerateSingleSticker(sticker) },
                onViewStickerSheet = { viewModel.setShowStickerSheetDialog(true) },
                onDownloadAll = { viewModel.downloadAllStickers(context) },
                onDownloadZip = { viewModel.downloadZipPack(context) }
            )

            // 6. Footer Branding ("Created with ❤️ by Elias")
            FooterBrandingSection(strings = strings)
        }

        // Dialogs
        if (editingSticker != null) {
            StickerEditorDialog(
                strings = strings,
                currentLanguage = currentLanguage,
                sticker = editingSticker,
                onDismiss = { viewModel.setEditingSticker(null) },
                onSaveCustomization = { thickness, colorHex, caption ->
                    editingSticker?.let {
                        viewModel.updateStickerCustomization(it, thickness, colorHex, caption)
                    }
                },
                onDownload = { sticker -> viewModel.downloadSingleSticker(context, sticker) }
            )
        }

        if (showStickerSheetDialog && generationState is GenerationState.Success) {
            val stickers = (generationState as GenerationState.Success).stickers
            StickerSheetDialog(
                strings = strings,
                stickers = stickers,
                onDismiss = { viewModel.setShowStickerSheetDialog(false) },
                onDownloadSheet = { viewModel.downloadStickerSheet(context) }
            )
        }

        if (showApiKeyDialog) {
            ApiKeyDialog(
                strings = strings,
                currentKey = customApiKey,
                onDismiss = { viewModel.setShowApiKeyDialog(false) },
                onSaveKey = { key -> viewModel.setCustomApiKey(key) }
            )
        }
    }
}
