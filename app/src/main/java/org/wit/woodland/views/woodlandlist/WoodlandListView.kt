package org.wit.woodland.views.woodlandlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_woodland_list.*
import kotlinx.android.synthetic.main.card_woodland.*
import org.jetbrains.anko.info
import org.wit.woodland.R
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.utils.SwipeToDeleteCallback
import org.wit.woodland.views.BaseView



class WoodlandListView : BaseView(), WoodlandListener {

    lateinit var presenter: WoodlandListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woodland_list)
        super.init(toolbar, false)
        presenter = initPresenter(WoodlandListPresenter(this)) as WoodlandListPresenter


        setSwipeRefresh()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadWoodlands()


        val swipeDeleteHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as WoodlandAdapter
                deleteWoodland(viewHolder.itemView.tag as String)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(recyclerView)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.getItem(0).setVisible(true)
        //return super.onCreateOptionsMenu(menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                info("Search has been submitted" + query)
                if (query != null) {
                    presenter.doReturnResults(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    //presenter.doReturnResults(newText)
                    //TODO("Not yet implemented")
                }
                return true
            }
        })
        searchView.setOnCloseListener(object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                presenter.loadWoodlands()
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddWoodland()
            R.id.item_settings -> presenter.doSettings()
            R.id.item_favourites -> presenter.doShowFavourites()
            R.id.item_logout -> presenter.doLogout()
            R.id.item_map -> presenter.doShowWoodlandsMap()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onWoodlandClick(woodland: WoodlandModel) {
        presenter.doEditWoodland(woodland)
    }

    override fun onFavouriteClick(woodland: WoodlandModel, favourite: Boolean) {
        presenter.doFavourite(woodland, favourite)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadWoodlands()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showWoodlands (woodlands: List<WoodlandModel>) {
        recyclerView.adapter = WoodlandAdapter(woodlands as ArrayList<WoodlandModel>, this)
        recyclerView.adapter?.notifyDataSetChanged()
        checkSwipeRefresh()
    }

    fun deleteWoodland(id: String){
        info("Delete: deleteWoodland "+id)
        presenter.doDeleteWoodland(id)
    }



    fun setSwipeRefresh() {
        swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                swiperefresh.isRefreshing = true
                presenter.loadWoodlands()

            }
        })
    }



    fun checkSwipeRefresh() {
        if (swiperefresh.isRefreshing) swiperefresh.isRefreshing = false
    }


}

