package org.wit.woodland.views.splash


import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import org.wit.woodland.R
import org.wit.woodland.views.BaseView


class SplashView : BaseView() {

    lateinit var presenter: SplashPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.activity_splash)
        presenter = initPresenter(SplashPresenter(this)) as SplashPresenter

        // Using a handler to delay loading the MainActivity
        Handler().postDelayed({
            // Start activity
            //startActivity(Intent(this, LoginView::class.java))
            presenter.doShowLogin()

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            // Close this activity
            finish()
        }, 2000)


    }

    private fun makeFullScreen() {
        // Remove Title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // Make Fullscreen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // Hide the toolbar
        supportActionBar?.hide()
    }
}
