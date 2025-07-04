package com.proyecto.ReUbica.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import com.proyecto.ReUbica.utils.getRealPathFromUri


fun getRealPathFromUri(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return ""
    val fileName = getFileName(context, uri)
    val file = File(context.cacheDir, fileName)
    val outputStream = FileOutputStream(file)
    inputStream.copyTo(outputStream)
    inputStream.close()
    outputStream.close()
    return file.absolutePath
}

fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) {
                    result = it.getString(index)
                }
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/') ?: -1
        if (cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result ?: "temp_image"
}

