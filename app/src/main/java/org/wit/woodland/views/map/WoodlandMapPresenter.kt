package org.wit.woodland.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView

class WoodlandMapPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, woodlands: List<WoodlandModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        woodlands.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val woodland = marker.tag as WoodlandModel
        doAsync {
            uiThread {
                if (woodland != null) view?.showWoodland(woodland)
            }
        }
    }

    fun loadWoodlands() {
        doAsync {
            val woodlands = app.woodlands.findAll()
            uiThread {
                view?.showWoodlands(woodlands)
            }
        }
    }
}

