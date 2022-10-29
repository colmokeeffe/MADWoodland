package org.wit.woodland.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import org.wit.woodland.R
import org.wit.woodland.adapters.WoodlandAdapter
import org.wit.woodland.adapters.WoodlandListener
import org.wit.woodland.databinding.ActivityWoodlandListBinding
import org.wit.woodland.main.MainApp
import org.wit.woodland.models.WoodlandModel

class WoodlandListActivity : AppCompatActivity(), WoodlandListener/*, MultiplePermissionsListener*/ {

    lateinit var app: MainApp
    private lateinit var binding: ActivityWoodlandListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWoodlandListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadWoodlands()
        registerRefreshCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, WoodlandActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, WoodlandMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
            R.id.item_logout -> {
                val launcherIntent = Intent(this, LoginActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_edit -> {
                val launcherIntent = Intent(this, SettingsActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onWoodlandClick(woodland: WoodlandModel) {
        val launcherIntent = Intent(this, WoodlandActivity::class.java)
        launcherIntent.putExtra("woodland_edit", woodland)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadWoodlands() }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

    private fun loadWoodlands() {
        showWoodlands(app.woodlands.findAll())
    }

    fun showWoodlands (woodlands: List<WoodlandModel>) {
        binding.recyclerView.adapter = WoodlandAdapter(woodlands, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}