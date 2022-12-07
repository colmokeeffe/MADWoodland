package org.wit.woodland.views.splash

import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView
import org.wit.woodland.views.VIEW

class SplashPresenter(view: BaseView): BasePresenter(view) {


    fun doShowLogin(){
        view?.navigateTo(VIEW.LOGIN)
    }
}