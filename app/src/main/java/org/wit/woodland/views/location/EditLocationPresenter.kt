package org.wit.woodland.views.location

import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.woodland.models.Location
import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView

class EditLocationPresenter(view: BaseView): BasePresenter(view)
{
    var location = Location()
    init {location = view.intent.extras?.getParcelable<Location>("location")!!}

    fun doConfigureMap(map: GoogleMap)
    {
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("New Woodland")
            .snippet(loc.toString())
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.uiSettings.setZoomControlsEnabled(true)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        view?.showLocation(loc.latitude, loc.longitude)
    }

    fun doUpdateLocation(lat: Double, lng: Double)
    {
        location.lat = lat
        location.lng = lng
    }

    fun doSave()
    {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        view?.setResult(0, resultIntent)
        view?.finish()
    }

    fun doUpdateMarker(marker: Marker)
    {
        val loc = LatLng(location.lat, location.lng)
        marker.setSnippet(loc.toString())
    }

   /* fun doCancel()
    {
        view?.finish()
    }
    */
}
