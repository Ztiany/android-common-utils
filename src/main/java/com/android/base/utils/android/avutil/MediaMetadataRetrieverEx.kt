package com.android.base.utils.android.avutil

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.annotation.WorkerThread

@WorkerThread
fun createVideoThumbnail(context: Context?, uri: Uri?): Bitmap? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, uri)
    val bitmap = retriever.frameAtTime
    retriever.release()
    return bitmap
}