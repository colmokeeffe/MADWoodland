package org.wit.woodland.views.woodlandlist

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView
import org.wit.woodland.views.VIEW


class WoodlandListPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    fun doAddWoodland() {
        view?.navigateTo(VIEW.WOODLAND)
    }

    fun doDeleteWoodland(id: String){
        var woodland = app.woodlands.findByFbId(id)
        if(woodland != null) {
            app.woodlands.delete(woodland)
        }
        loadWoodlands()
    }

    fun doShowFavourites(){
        view?.navigateTo(VIEW.FAVOURITES)
    }

    fun doReturnResults(text: String){
        doAsync{
            val woodlands = app.woodlands.findSearchResults(text)
            uiThread{
                view?.showWoodlands(woodlands)
            }
        }
    }

    fun doFavourite(woodland: WoodlandModel, favourite: Boolean){
        woodland.favourite = favourite
        app.woodlands.setFavourite(woodland)
    }

    fun doEditWoodland(woodland: WoodlandModel) {
        view?.navigateTo(VIEW.WOODLAND,0 ,"woodland_edit", woodland)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.woodlands.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doShowWoodlandsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadWoodlands(){
        doAsync{
            val woodlands = app.woodlands.findAll()
            uiThread{
                view?.showWoodlands(woodlands)
            }
        }
    }
}