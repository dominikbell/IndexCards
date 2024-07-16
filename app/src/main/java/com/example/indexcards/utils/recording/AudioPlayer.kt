package com.example.indexcards.utils.recording

import android.net.Uri
import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun playFile(uri: Uri)
    fun stop()
}