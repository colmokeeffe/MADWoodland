package org.wit.woodland.views.woodland

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_woodland.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.woodland.R
import org.wit.woodland.models.Location
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.BaseView


class WoodlandView : BaseView(), AnkoLogger, ImageListener
{
  lateinit var presenter: WoodlandPresenter
  lateinit var map: GoogleMap
  var woodland = WoodlandModel()

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_woodland)
    super.init(toolbarAdd, true)
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      map = it
      presenter.doConfigureMap(map)
      it.setOnMapClickListener { presenter.doSetLocation() }
    }

    presenter = initPresenter(WoodlandPresenter(this)) as WoodlandPresenter


    val layoutManager = LinearLayoutManager(this)
    recyclerView2.layoutManager = layoutManager

    checkBox.setOnCheckedChangeListener{ buttonView, isChecked ->
      presenter.doCheckVisited(isChecked)
    }

    checkBoxCarpark.setOnCheckedChangeListener { buttonView, isChecked ->
      presenter.doCheckCarpark(isChecked)
    }

    checkBoxShop.setOnCheckedChangeListener { buttonView, isChecked ->
      presenter.doCheckShop(isChecked)
    }

    checkBoxToilets.setOnCheckedChangeListener { buttonView, isChecked ->
      presenter.doCheckToilets(isChecked)
    }

    rb_conifer.setOnCheckedChangeListener { buttonView, isChecked ->
      presenter.doCheckConifer(isChecked)
    }

    rb_mixed.setOnCheckedChangeListener { buttonView, isChecked ->
      presenter.doCheckMixed(isChecked)
    }

    rb_broadleaf.setOnCheckedChangeListener { buttonView, isChecked ->
      presenter.doCheckBroadleaf(isChecked)
    }

    chooseImage.setOnClickListener{
      presenter.cacheWoodland(woodlandTitle.text.toString(), description.text.toString(), notes.text.toString())
      presenter.doSelectImage()
    }

    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
      presenter.doSetRating(rating)
    }
  }

  @SuppressLint("ResourceAsColor")
  override fun showWoodland(woodland: WoodlandModel)
  {
    if(woodlandTitle.text.isEmpty()) {
      woodlandTitle.setText(woodland.title)
    }
    if(description.text.isEmpty()){
      description.setText(woodland.description)
    }
    if(notes.text.isEmpty()){
        notes.setText(woodland.notes)
      }

    checkBox.setChecked(woodland.visited)
    checkBoxCarpark.setChecked(woodland.carpark)
    checkBoxToilets.setChecked(woodland.toilets)
    checkBoxShop.setChecked(woodland.shop)
    rb_conifer.setChecked(woodland.rb_conifer)
    rb_mixed.setChecked(woodland.rb_mixed)
    rb_broadleaf.setChecked(woodland.rb_broadleaf)

    if(woodland.rating>0)
    {
      ratingBar.setRating(woodland.rating)
    }
    if(woodland.visited ==true)
    {
      visitDate.setText("Date Visited: ${woodland.date}")
    }
    showImages(woodland.images)


    if (woodland.images.size < 4)
    {
      chooseImage.setText(R.string.change_woodland_image)
    }
    if(woodland.images.size >3)
    {
      chooseImage.setEnabled(false)
      chooseImage.setBackgroundColor(R.color.design_default_color_error)
    }
    this.showLocation(woodland.location)
  }

  override fun showLocation(loc: Location)
  {
    lat.setText("%.6f".format(loc.lat))
    lng.setText("%.6f".format(loc.lng))
  }


  override fun onDeleteClick(image: String)
  {
    presenter.doDeleteImage(image)
  }

  override fun showImages (images: ArrayList<String>)
  {
    woodland.images = images
    recyclerView2.adapter = ImageAdapter(images, this)
    recyclerView2.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean
  {
    menuInflater.inflate(R.menu.menu_woodland, menu)
    if(presenter.edit) {
      menu.getItem(1).setVisible(true)
      menu.getItem(3).setVisible(true)
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun showVisitDate(res: String)
  {
    visitDate.setText(res)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean
  {
    when (item.itemId) {
      R.id.item_cancel -> {
        presenter.doCancel()
      }
      R.id.item_delete ->{
        presenter.doDelete()
      }
      R.id.item_save -> {
        if (woodlandTitle.text.toString().isEmpty())
        {
          toast(R.string.enter_woodland_title)
        }
        else
        {
          presenter.doAddOrSave(
            woodlandTitle.text.toString(),
            description.text.toString(),
            notes.text.toString()
          )
        }
      }
      R.id.item_share ->{presenter.doShareWoodland()}
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
  {
    super.onActivityResult(requestCode, resultCode, data)
    if(data != null){}
  }

  override fun onBackPressed()
  {
    presenter.doCancel()
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
    presenter.doResartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle)
  {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}

