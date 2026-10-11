@echo off
echo ==========================================
echo  Building Elias Stickers Android APK
echo ==========================================

cd /d "%~dp0\.."

call gradle assembleRelease
if %ERRORLEVEL% EQU 0 (
    echo Release APK build succeeded.
    if exist "app\build\outputs\apk\release\app-release.apk" (
        copy /Y "app\build\outputs\apk\release\app-release.apk" "Elias-Stickers.apk"
    ) else if exist "app\build\outputs\apk\release\app-release-unsigned.apk" (
        copy /Y "app\build\outputs\apk\release\app-release-unsigned.apk" "Elias-Stickers.apk"
    )
) else (
    echo Release build failed, trying assembleDebug...
    call gradle assembleDebug
    if exist "app\build\outputs\apk\debug\app-debug.apk" (
        copy /Y "app\build\outputs\apk\debug\app-debug.apk" "Elias-Stickers.apk"
    )
)

if exist "Elias-Stickers.apk" (
    echo ==========================================
    echo  SUCCESS: Elias-Stickers.apk created!
    echo ==========================================
) else (
    echo ERROR: APK build failed!
    exit /b 1
)
