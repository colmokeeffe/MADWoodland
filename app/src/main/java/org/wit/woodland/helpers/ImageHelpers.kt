package org.wit.woodland

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {

    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_woodland_image.toString())
    intentLauncher.launch(chooseFile)
}
