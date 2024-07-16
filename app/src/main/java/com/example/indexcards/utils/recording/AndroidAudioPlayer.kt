package com.example.indexcards.utils.recording

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
) : AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun playFile(uri: Uri) {
        MediaPlayer.create(context, uri).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}