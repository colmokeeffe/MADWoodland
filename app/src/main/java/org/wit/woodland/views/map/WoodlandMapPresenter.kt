package org.wit.woodland.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView
import org.wit.woodland.views.VIEW

class WoodlandMapPresenter(view: BaseView) : BasePresenter(view)
{
    fun doPopulateMap(map: GoogleMap, woodlands: List<WoodlandModel>)
    {
        map.uiSettings.setZoomControlsEnabled(true)
        woodlands.forEach{
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 6f))
        }
    }

    fun doMarkerSelected(marker: Marker)
    {
        val woodland = marker.tag as WoodlandModel
        doAsync {
            uiThread {
                view?.showWoodland(woodland)
                marker.showInfoWindow()
            }
        }

       /* fun doCancel()
        {
            view?.finish()
        }
        */
    }

    fun loadWoodlands()
    {
        doAsync {
            val woodlands = app.woodlands.findAll()
            uiThread {
                view?.showWoodlands(woodlands)
            }
        }
    }

    fun homeView()
    {
        view?.navigateTo(VIEW.LIST)
    }

    fun doAddWoodland()
    {
        view?.navigateTo(VIEW.WOODLAND)
    }

    fun doLogout()
    {
        FirebaseAuth.getInstance().signOut()
        app.woodlands.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doShowWoodlandsMap()
    {
        view?.navigateTo(VIEW.MAP)
    }


    fun doShowFavourites()
    {
        view?.navigateTo(VIEW.FAVOURITES)
    }

    fun doSettings()
    {
        view?.navigateTo(VIEW.SETTINGS)
    }
    /*
    fun doCancel()
    {
        view?.finish()
    }
     */
}

