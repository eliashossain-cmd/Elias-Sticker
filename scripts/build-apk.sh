#!/usr/bin/env bash
set -e

echo "=========================================="
echo " Building Elias Stickers Android APK"
echo "=========================================="

# Ensure script is executable from project root
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

echo "Project root: $ROOT_DIR"

# Assemble Release or Debug APK
if gradle assembleRelease; then
    echo "Release APK build succeeded."
    APK_SOURCE="$ROOT_DIR/app/build/outputs/apk/release/app-release.apk"
    if [ ! -f "$APK_SOURCE" ]; then
        APK_SOURCE="$ROOT_DIR/app/build/outputs/apk/release/app-release-unsigned.apk"
    fi
else
    echo "Release build failed, falling back to assembleDebug..."
    gradle assembleDebug
    APK_SOURCE="$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk"
fi

if [ -f "$APK_SOURCE" ]; then
    cp "$APK_SOURCE" "$ROOT_DIR/Elias-Stickers.apk"
    echo "=========================================="
    echo " SUCCESS: APK copied to $ROOT_DIR/Elias-Stickers.apk"
    ls -lh "$ROOT_DIR/Elias-Stickers.apk"
    echo "=========================================="
else
    echo "ERROR: APK build output not found!"
    exit 1
fi
