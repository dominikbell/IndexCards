package com.example.indexcards.utils.recording

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}