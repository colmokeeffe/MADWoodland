package org.wit.woodland.views.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.woodland.R
import org.wit.woodland.views.BaseView


class SettingsView : BaseView(), AnkoLogger
{
    lateinit var presenter: SettingsPresenter
    //lateinit var pieChart: PieChart
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        super.init(toolbar, true)
        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null)
        {
            settingsEmail.setText(user.email)
        }
        presenter.doShowStats()
        //presenter.doShowPieChart()

    btnUpdate.setOnClickListener() {
            val email = settingsEmail.text.toString()
            val password = settingsPassword.text.toString()
            if(email.length>0 && email != user?.email) {
                user!!.updateEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            toast("User email address updated.")
                        }
                    }
            }
            if(password.length>0)
            {
                user!!.updatePassword(password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            toast("User password updated.")
                        }
                    }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId) {
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

    override fun showStats (stats: String)
    {
        userStats.setText(stats)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed()
    {
        presenter.doCancel()
    }

}



