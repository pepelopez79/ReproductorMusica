package com.daw.reproductoraudio

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.daw.reproductoraudio.ui.theme.ReproductorAudioTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReproductorAudioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AudioPlayer()
                }
            }
        }
    }
}

@Composable
fun AudioPlayer() {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf(MediaPlayer.create(context, R.raw.musica)) }
    var currentPosition by remember { mutableIntStateOf(0) }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = mediaPlayer.currentPosition
            delay(100)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.caratula),
            contentDescription = "Imagen",
            modifier = Modifier
                .size(320.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .padding(bottom = 40.dp)
        )

        Slider(
            value = currentPosition.toFloat(),
            onValueChange = {
                mediaPlayer.seekTo(it.toInt())
                currentPosition = it.toInt()
            },
            valueRange = 0f..mediaPlayer.duration.toFloat()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (!isPlaying) {
                        mediaPlayer.start()
                        isPlaying = true
                    }
                },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.play), contentDescription = "Play")
            }

            Spacer(modifier = Modifier.width(30.dp))

            Button(
                onClick = {
                    if (isPlaying) {
                        mediaPlayer.pause()
                        isPlaying = false
                    }
                },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.pause), contentDescription = "Pause")
            }

            Spacer(modifier = Modifier.width(30.dp))

            Button(
                onClick = {
                    if (isPlaying) {
                        mediaPlayer.stop()
                        mediaPlayer.release()
                        mediaPlayer = MediaPlayer.create(context, R.raw.musica)
                        currentPosition = 0
                        isPlaying = false
                    }
                },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.stop), contentDescription = "Stop")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReproductorAudioTheme {
        AudioPlayer()
    }
}
