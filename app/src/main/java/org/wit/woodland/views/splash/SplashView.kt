package org.wit.woodland.views.splash

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import org.wit.woodland.R
import org.wit.woodland.views.BaseView
import android.view.animation.AnimationUtils
import android.widget.ImageView

@Suppress("DEPRECATION")
class SplashView : BaseView()
{
    lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val backgroundImage: ImageView = findViewById(R.id.imageView)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        //passing in the animation id side_slide to move image id imageView from left to right by 50%
        backgroundImage.startAnimation(slideAnimation)
        presenter = initPresenter(SplashPresenter(this)) as SplashPresenter


        Handler().postDelayed({
            presenter.doShowLogin()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3000)// set delay 3sec before fade_out to doShowLogin()
    }
}
