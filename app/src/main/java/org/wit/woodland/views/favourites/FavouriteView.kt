package org.wit.woodland.views.favourites

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_woodland_list.*
import org.jetbrains.anko.info
import org.wit.woodland.R
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.utils.SwipeToDeleteCallback
import org.wit.woodland.utils.SwipeToEdit
import org.wit.woodland.views.BaseView


class FavouriteView : BaseView(), FavouriteListener
{
    lateinit var presenter: FavouritePresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woodland_list)
        super.init(toolbar, true)
        presenter = initPresenter(FavouritePresenter(this)) as FavouritePresenter


        setSwipeRefresh()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadFavourites()

        val swipeEditHandler = object : SwipeToEdit(this)
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val woodlandSwiped =
                    presenter.app.woodlands.findByFbId(viewHolder.itemView.tag as String)
                onWoodlandClick(woodlandSwiped!!)
            }
        }

        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(recyclerView)

        val swipeDeleteHandler = object : SwipeToDeleteCallback(this)
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
            {
                deleteWoodland(viewHolder.itemView.tag as String)
            }
        }

        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId) {
            R.id.item_home -> presenter.homeView()
            R.id.item_add -> presenter.doAddWoodland()
            R.id.item_settings -> presenter.doSettings()
            R.id.item_logout -> presenter.doLogout()
            R.id.item_map -> presenter.doShowWoodlandsMap()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onWoodlandClick(woodland: WoodlandModel)
    {
        presenter.doEditWoodland(woodland)
    }

    override fun onFavouriteClick(woodland: WoodlandModel, favourite: Boolean)
    {
        presenter.doFavourite(woodland, favourite)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        presenter.loadFavourites()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showWoodlands (woodlands: List<WoodlandModel>)
    {
        recyclerView.adapter = FavouriteAdapter(woodlands as ArrayList<WoodlandModel>, this)
        recyclerView.adapter?.notifyDataSetChanged()
        checkSwipeRefresh()
    }

    fun deleteWoodland(id: String)
    {
        info("Delete: deleteWoodland "+id)
        presenter.doDeleteWoodland(id)
    }

    fun setSwipeRefresh()
    {
        swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh()
            {
                swiperefresh.isRefreshing = true
                presenter.loadFavourites()

            }
        })
    }

    fun checkSwipeRefresh()
    {
        if (swiperefresh.isRefreshing) swiperefresh.isRefreshing = false
    }

    override fun onBackPressed()
    {
        presenter.doCancel()
    }
}

