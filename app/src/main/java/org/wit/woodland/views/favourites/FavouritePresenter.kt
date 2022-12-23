package org.wit.woodland.views.favourites

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView
import org.wit.woodland.views.VIEW

//Presenter acts as a bridge that connects a Model and a View, serves in the middle
class FavouritePresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    fun doAddWoodland()
    {
        view?.navigateTo(VIEW.WOODLAND)
    }

    fun doDeleteWoodland(id: String)
    {
        var woodland = app.woodlands.findByFbId(id)
        if(woodland != null) {
            app.woodlands.delete(woodland)
        }
        loadFavourites()
    }

    fun doFavourite(woodland: WoodlandModel, favourite: Boolean)
    {
        woodland.favourite = favourite
        app.woodlands.setFavourite(woodland)
        loadFavourites()
    }

    fun doEditWoodland(woodland: WoodlandModel)
    {
        view?.navigateTo(VIEW.WOODLAND,0 ,"woodland_edit", woodland)
    }

 /*   fun loadWoodlands()
    {
        doAsync{
            val woodlands = app.woodlands.findAll()
            uiThread{
                view?.showWoodlands(woodlands)
            }
        }
    }
  */
    fun homeView()
    {
        view?.navigateTo(VIEW.LIST)
    }

    fun doLogout()
    {
        FirebaseAuth.getInstance().signOut()
        app.woodlands.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doCancel()
    {
        view?.finish()
    }

    fun doSettings()
    {
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doShowWoodlandsMap()
    {
        view?.navigateTo(VIEW.MAP)
    }

    fun loadFavourites(){
        doAsync{
            val woodlands = app.woodlands.findFavourites()
            uiThread{
                view?.showWoodlands(woodlands)
            }
        }
    }
}