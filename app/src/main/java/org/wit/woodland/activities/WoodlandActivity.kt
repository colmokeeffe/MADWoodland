package org.wit.woodland.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.woodland.R
import org.wit.woodland.databinding.ActivityWoodlandBinding
import org.wit.woodland.main.MainApp
import org.wit.woodland.models.Location
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.showImagePicker
import timber.log.Timber.i

class WoodlandActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWoodlandBinding
    var woodland = WoodlandModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWoodlandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Woodland Activity started...")

        if (intent.hasExtra("woodland_edit")) {
            edit = true
            woodland = intent.extras?.getParcelable("woodland_edit")!!
            binding.woodlandTitle.setText(woodland.title)
            binding.woodlandDescription.setText(woodland.description)
            binding.btnAdd.setText(R.string.save_woodland)
            Picasso.get()
                .load(woodland.image)
                .into(binding.woodlandImage)
            if (woodland.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_woodland_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            woodland.title = binding.woodlandTitle.text.toString()
            woodland.description = binding.woodlandDescription.text.toString()
            if (woodland.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_woodland_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.woodlands.update(woodland.copy())
                } else {
                    app.woodlands.create(woodland.copy())
                }
            }
            i("add Button Pressed: $woodland")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.woodlandLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (woodland.zoom != 0f) {
                location.lat =  woodland.lat
                location.lng = woodland.lng
                location.zoom = woodland.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_woodland, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.woodlands.delete(woodland)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            woodland.image = result.data!!.data!!
                            Picasso.get()
                                .load(woodland.image)
                                .into(binding.woodlandImage)
                            binding.chooseImage.setText(R.string.change_woodland_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            woodland.lat = location.lat
                            woodland.lng = location.lng
                            woodland.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}