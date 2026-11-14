package com.waifudomi.app

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WaifuDomiScreen() }
    }
}

@Composable
private fun WaifuDomiScreen() {
    val context = LocalContext.current
    val mediaFiles = remember {
        (context.assets.list("waifu-media") ?: emptyArray())
            .filter { it.lowercase().endsWith(".png") || it.lowercase().endsWith(".mp4") }
            .sorted()
    }
    val images = remember(mediaFiles) { mediaFiles.filter { it.lowercase().endsWith(".png") } }
    val videos = remember(mediaFiles) { mediaFiles.filter { it.lowercase().endsWith(".mp4") } }
    var imageIndex by remember { mutableIntStateOf(0) }
    var selectedVideo by remember { mutableStateOf<String?>(null) }
    val player = remember { ExoPlayer.Builder(context).build() }

    DisposableEffect(Unit) { onDispose { player.release() } }
    LaunchedEffect(selectedVideo) {
        val file = selectedVideo
        if (file != null) {
            player.setMediaItem(MediaItem.fromUri(Uri.parse("asset:///waifu-media/$file")))
            player.prepare()
            player.playWhenReady = true
        } else {
            player.stop()
        }
    }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("WAIFU-DOMI", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Your companion prototype", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))
                if (images.isNotEmpty()) {
                    val current = images[imageIndex.coerceIn(images.indices)]
                    val bitmap = remember(current) {
                        context.assets.open("waifu-media/$current").use { BitmapFactory.decodeStream(it) }
                    }
                    bitmap?.let {
                        Image(bitmap = it.asImageBitmap(), contentDescription = "Waifu-Domi character image", modifier = Modifier.fillMaxWidth().height(420.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Image ${imageIndex + 1} of ${images.size}")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { imageIndex = (imageIndex - 1 + images.size) % images.size }) { Text("Previous") }
                        Button(onClick = { imageIndex = (imageIndex + 1) % images.size }) { Text("Next") }
                    }
                } else Text("No character images found in app assets.")

                Spacer(Modifier.height(24.dp))
                Text("Character videos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                videos.forEach { video ->
                    OutlinedButton(onClick = { selectedVideo = video }, modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                        Text(if (selectedVideo == video) "Playing: $video" else "Play: $video")
                    }
                }
                if (selectedVideo != null) {
                    Spacer(Modifier.height(10.dp))
                    AndroidView(
                        factory = { viewContext -> PlayerView(viewContext).apply { this.player = player; useController = true } },
                        update = { it.player = player },
                        modifier = Modifier.fillMaxWidth().height(230.dp)
                    )
                    TextButton(onClick = { selectedVideo = null }) { Text("Stop video") }
                }
                Spacer(Modifier.height(20.dp))
                Text("Next milestone: connect the chat, personality, voice and memory systems.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
