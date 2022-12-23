package org.wit.woodland.views


import android.content.Intent
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.wit.woodland.R
import org.wit.woodland.models.Location
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.favourites.FavouriteView
import org.wit.woodland.views.location.EditLocationView
import org.wit.woodland.views.login.LoginView
import org.wit.woodland.views.map.WoodlandMapView
import org.wit.woodland.views.settings.SettingsView
import org.wit.woodland.views.woodland.WoodlandView
import org.wit.woodland.views.woodlandlist.WoodlandListView

@Suppress("DEPRECATION")
const val IMAGE_REQUEST = 1
const val LOCATION_REQUEST = 2

enum class VIEW
{
    LOCATION, WOODLAND, MAP, LIST, LOGIN, SETTINGS, FAVOURITES
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger
{
    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null)
    {
        var intent = Intent(this, WoodlandListView::class.java)
        when (view)
        {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.WOODLAND -> intent = Intent(this, WoodlandView::class.java)
            VIEW.MAP -> intent = Intent(this, WoodlandMapView::class.java)
            VIEW.LIST -> intent = Intent(this, WoodlandListView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.FAVOURITES-> intent = Intent(this, FavouriteView::class.java)
            VIEW.LOGIN -> {
                intent = Intent(this, LoginView::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }
        if (key != "")
        {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter
    {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean)
    {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
    }

    fun initDrawerNavigation(toolbar: Toolbar, drawerLayout: DrawerLayout, navigationView: NavigationView) {
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

            }
        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener((drawerToggle))
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.woodlandNew -> navigateTo(VIEW.WOODLAND)
                R.id.woodlandList-> navigateTo(VIEW.LIST)
                R.id.share -> navigateTo(VIEW.WOODLAND)
                R.id.nav_sign_out ->
                {
                    FirebaseAuth.getInstance().signOut()
                    navigateTo(VIEW.LOGIN)
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true

        }

    }



   /* private lateinit var appBarConfiguration: AppBarConfiguration
    fun onCreate(toolbar: Toolbar, upEnabled: Boolean) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        ///
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
*/
    override fun onDestroy()
    {
        basePresenter?.onDestroy()
        super.onDestroy()
    }

    fun doShareWoodland(woodland: WoodlandModel)
    {
        val intent= Intent()
        var locationURL = "https://www.google.ie/maps/@"+woodland.location.lat+","+woodland.location.lng+","+woodland.location.zoom+"z"
        intent.action=Intent.ACTION_SEND
        intent.putExtra(
                Intent.EXTRA_TEXT, "Woodland Title: " + woodland.title +
                        "\nWoodland Description: " + woodland.description +
                        "\nWoodland Location: "+ locationURL
            )
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showProgress(){}
    open fun hideProgress(){}
    open fun showStats(stats: String){}
    open fun showImages(images: ArrayList<String>){}
    open fun showWoodland(woodland: WoodlandModel) {}
    open fun showWoodlands(woodlands: List<WoodlandModel>) {}
    open fun showLocation(location : Location) {}
    open fun showVisitDate(res: String){}
    open fun showLocation(latitude : Double, longitude : Double) {}
}