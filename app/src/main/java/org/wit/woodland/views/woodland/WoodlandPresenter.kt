package org.wit.woodland.views.woodland

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.*
import org.wit.woodland.helpers.checkLocationPermissions
import org.wit.woodland.helpers.createDefaultLocationRequest
import org.wit.woodland.helpers.isPermissionGranted
import org.wit.woodland.helpers.showImagePicker
import org.wit.woodland.models.Location
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.*
import java.text.SimpleDateFormat
import java.util.*

class WoodlandPresenter(view: BaseView): BasePresenter(view), AnkoLogger
{
    val sdf = SimpleDateFormat("dd/MMM/yyyy")
    var woodland = WoodlandModel()
    var defaultLocation = Location(52.245696, -7.139102, 5f)
    var edit = false;
    var map: GoogleMap? = null
    val locationRequest = createDefaultLocationRequest()
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    var locationManuallyChanged = false;

    init
    {
        if (view.intent.hasExtra("woodland_edit")) {
            edit = true
            woodland = view.intent.extras?.getParcelable<WoodlandModel>("woodland_edit")!!
            view.showWoodland(woodland)
        }else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults))
        {
            doSetCurrentLocation()
        }
        else
        {
            locationUpdate(defaultLocation)
        }
    }

    fun doAddOrSave(title: String, description: String, notes: String)
    {
        woodland.title = title
        woodland.description = description
        woodland.notes = notes
        doAsync{
            if (edit) {
                app.woodlands.update(woodland)
            } else {
                app.woodlands.create(woodland)
            }
            uiThread {
                view?.finish()
            }
        }
    }

    fun doCancel()
    {
        view?.finish()
    }

    fun doShareWoodland()
    {
        view?.doShareWoodland(woodland)
    }

    fun doDelete()
    {
        app.woodlands.delete(woodland)
        view?.finish()
    }

    fun doSelectImage()
    {
        view?.let {showImagePicker(view!!, IMAGE_REQUEST)}
    }

    fun loadImages()
    {
        doAsync{
            val images = woodland.images
            uiThread{
                view?.showImages(images)
            }
        }
    }


    fun doCheckVisited(isChecked: Boolean)
    {
        var res = "";
        if (isChecked) {
            woodland.visited = true
            woodland.date = sdf.format(Date())
            res = "Date Visited: ${woodland.date}"
        }
        else
        {
            woodland.visited = false
            woodland.date = ""
        }
        view?.showVisitDate(res)
    }

    fun doCheckCarpark(isChecked: Boolean)
    {
        woodland.carpark = isChecked
    }

    fun doCheckShop(isChecked: Boolean)
    {
        woodland.shop = isChecked
    }

    fun doCheckConifer(isChecked: Boolean)
    {
        woodland.rb_conifer = isChecked
    }

    fun doCheckBroadleaf(isChecked: Boolean)
    {
        woodland.rb_broadleaf = isChecked
    }

    fun doCheckMixed(isChecked: Boolean)
    {
        woodland.rb_mixed = isChecked
    }

    fun doCheckToilets(isChecked: Boolean)
    {
        woodland.toilets = isChecked
    }

    fun doDeleteImage(image: String)
    {
        woodland.images.remove(image)
        loadImages()
    }

    fun doSetLocation()
    {
        locationManuallyChanged = true;
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(woodland.location.lat, woodland.location.lng, woodland.location.zoom))
    }

    fun cacheWoodland (title: String, description: String, notes: String)
    {
        woodland.title = title;
        woodland.description = description;
        woodland.notes = notes;
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {
        when (requestCode) {
            IMAGE_REQUEST -> {
                woodland.images.add(data.getData().toString())
                view?.showWoodland(woodland)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                woodland.location = location
                locationUpdate(woodland.location)
            }
        }
    }

    fun doConfigureMap(m: GoogleMap)
    {
        info("Running WoodlandPresenter doConfigureMap")
        map = m
        locationUpdate(woodland.location)
    }

    fun locationUpdate(location: Location)
    {
        woodland.location = location
        woodland.location.zoom = 10f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(woodland.title).position(LatLng(woodland.location.lat, woodland.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(woodland.location.lat, woodland.location.lng), woodland.location.zoom))
        view?.showWoodland(woodland)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation()
    {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    fun doSetRating(rating: Float)
    {
        woodland.rating = rating
    }



    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates()
    {
        var locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult != null && locationResult.locations != null)
                {
                    val l = locationResult.locations.last()
                    if (!locationManuallyChanged) {
                        locationUpdate(Location(l.latitude, l.longitude))
                    }
                }
            }
        }
        if (!edit)
        {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
}