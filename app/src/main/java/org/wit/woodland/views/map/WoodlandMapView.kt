package org.wit.woodland.views.map

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_woodland_maps.*
import org.wit.woodland.R
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.BaseView

class WoodlandMapView : BaseView(), GoogleMap.OnMarkerClickListener
{

    lateinit var presenter: WoodlandMapPresenter
    lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woodland_maps)
        super.init(toolbar, true)
        presenter = initPresenter (WoodlandMapPresenter(this)) as WoodlandMapPresenter
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadWoodlands()
        }
    }

    override fun showWoodland(woodland: WoodlandModel)
    {
        currentTitle.text = woodland.title
        currentDescription.text = woodland.description
        currentLat.text=woodland.location.lat.toString()
        currentLng.text=woodland.location.lng.toString()
        if(woodland.images.size > 0)
        {
            Glide.with(this).load(woodland.images.get(0)).into(currentImage)
        }
        else
        {
            currentImage.setImageBitmap(null)
        }
    }

    override fun showWoodlands(woodlands: List<WoodlandModel>)
    {
        presenter.doPopulateMap(map, woodlands)
    }

    override fun onMarkerClick(marker: Marker): Boolean
    {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.item_home -> presenter.homeView()
            R.id.item_add -> presenter.doAddWoodland()
            R.id.item_settings -> presenter.doSettings()
            R.id.item_map -> presenter.doShowWoodlandsMap()
            R.id.item_logout -> presenter.doLogout()
            R.id.item_favourites -> presenter.doShowFavourites()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory()
    {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause()
    {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume()
    {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}

