package org.wit.woodland.views.settings

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.*
import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView
import org.wit.woodland.views.VIEW

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

    fun doUpdateSettings(email: String) {
        /*val findByEmail = app.users.findByEmail(email)
        val user = FirebaseAuth.getInstance().currentUser
        if (findByEmail != null && !email.equals(user)) {
            // todo toast(R.string.update_failed)
        }
        else{

            val password = settingsPassword.text.toString()
            user.email = email
            user.password = password
            info("Woodland User Updated with email: $email and password: $password")
            app.users.update(user)
            setResult(AppCompatActivity.RESULT_OK)
            startActivityForResult<WoodlandListView>(0)
            todo

        }
        //view?.navigateTo(VIEW.WOODLAND)*/
    }

    fun doShowStats(){
        doAsync {
            val countWoodlands = app.woodlands.findAll().size
            val countVisited = app.woodlands.countVisited()
            var statsString = ""
            if (countWoodlands > 0) {
                val percentage = (countVisited * 100) / countWoodlands
                statsString = """Total Woodlands: ${countWoodlands} 
            |Number Visited: ${countVisited}
            |Percentage Visited: $percentage%
        """.trimMargin()
            } else {
                statsString = """Total Woodlands: ${countWoodlands} 
                |Number Visited: ${countVisited}
            """.trimMargin()
            }
            uiThread { view?.showStats(statsString) }
        }
    }

    fun doAddWoodland() {
        view?.navigateTo(VIEW.WOODLAND)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.woodlands.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doShowWoodlandsMap() {
        view?.navigateTo(VIEW.MAPS)
    }
    fun doShowFavourites(){
        view?.navigateTo(VIEW.FAVOURITES)
    }

    fun doSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doCancel() {
        view?.finish()
    }

}