package org.wit.woodland.views.map

import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_woodland_maps.*
import org.wit.woodland.R
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.BaseView

class WoodlandMapView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: WoodlandMapPresenter
    lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woodland_maps)
        super.init(toolbar, true);

        presenter = initPresenter (WoodlandMapPresenter(this)) as WoodlandMapPresenter

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadWoodlands()
        }
    }

    override fun showWoodland(woodland: WoodlandModel){
        currentTitle.text = woodland.title
        currentDescription.text = woodland.description
        if(woodland!!.images.size > 0) {
            //currentImage.setImageBitmap(readImageFromPath(this, woodland!!.images.get(0)))
            Glide.with(this).load(woodland!!.images.get(0)).into(currentImage);
        }
        else{
            currentImage.setImageBitmap(null)
        }
    }

    override fun showWoodlands(woodlands: List<WoodlandModel>) {
        presenter.doPopulateMap(map, woodlands)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}

