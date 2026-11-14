# Waifu-Domi — Android starter

This is a first native Android prototype. It displays the supplied character PNGs and plays the supplied MP4 files inside the app.

## Upload to GitHub
1. Open your repository: https://github.com/cataliniulian21-netizen/Waifu-domi
2. Choose **Add file → Upload files**.
3. Extract this ZIP first, then upload the contents of the `Waifu-Domi` folder (including the `.github` folder) into the repository root.
4. Commit the upload to the `main` branch.
5. Open **Actions** in the repository. Select **Build Waifu-Domi APK** and run it, or wait for the push-triggered build.
6. When it finishes, open the workflow run, scroll to **Artifacts**, and download `Waifu-Domi-debug-apk`.

## Important
- All supplied PNG and MP4 assets are included under `app/src/main/assets/waifu-media/`.
- This first milestone is a visual/media prototype; it does not yet include AI chat, voice, long-term memory or Android app-control features.
- Do not put an OpenAI API key in this repository or inside the APK. The API connection needs a secure backend, which is a later milestone.
- The APK produced by this workflow is a debug build for testing, not a Play Store release.
