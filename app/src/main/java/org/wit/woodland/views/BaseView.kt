package org.wit.woodland.views


import android.content.Intent


import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.jetbrains.anko.AnkoLogger
import org.wit.woodland.views.settings.SettingsView
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.models.Location
import org.wit.woodland.views.favourites.FavouriteView
import org.wit.woodland.views.location.EditLocationView
import org.wit.woodland.views.map.WoodlandMapView
import org.wit.woodland.views.woodland.WoodlandView
import org.wit.woodland.views.woodlandlist.WoodlandListView
import org.wit.woodland.views.login.LoginView

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, WOODLAND, MAPS, LIST, LOGIN, SETTINGS, FAVOURITES
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, WoodlandListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.WOODLAND -> intent = Intent(this, WoodlandView::class.java)
            VIEW.MAPS -> intent = Intent(this, WoodlandMapView::class.java)
            VIEW.LIST -> intent = Intent(this, WoodlandListView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.FAVOURITES-> intent = Intent(this, FavouriteView::class.java)
            VIEW.LOGIN -> {
                intent = Intent(this, LoginView::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
        /*val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            toolbar.title = "${title}: ${user.email}"
        }

         */
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }

    fun doShareWoodland(woodland: WoodlandModel){
        val intent= Intent()
        var locationURL = "https://www.google.ie/maps/@"+woodland.location.lat+","+woodland.location.lng+","+woodland.location.zoom+"z"
        intent.action=Intent.ACTION_SEND
        if(woodland.images.size>0) {
            intent.putExtra(
                Intent.EXTRA_TEXT, "Woodland Title" + woodland.title +
                        "\nWoodland Description: " + woodland.description +
                        "\nWoodland Image: " + woodland.images.get(0)+
                        "\nWoodland Location: "+ locationURL
            )
        }
        else{
            intent.putExtra(
                Intent.EXTRA_TEXT, "Woodland Title" + woodland.title +
                        "\nWoodland Description: " + woodland.description+
                        "\nWoodland Location: "+ locationURL
            )
        }
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showWoodland(woodland: WoodlandModel) {}
    open fun showWoodlands(woodlands: List<WoodlandModel>) {}
    open fun showLocation(location : Location) {}
    open fun showVisitDate(res: String){}
    open fun showLocation(latitude : Double, longitude : Double) {}
    open fun showProgress(){}
    open fun hideProgress(){}
    open fun showStats(stats: String){}
    open fun showImages(images: ArrayList<String>){}

}