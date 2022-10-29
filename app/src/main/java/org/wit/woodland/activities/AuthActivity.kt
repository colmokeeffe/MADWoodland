package org.wit.woodland.activities

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth


class AuthActivity (val view: LoginActivity)  {
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>

    init{
        registerLoginCallback()
    }

    //Authentication with Firebase
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doLogin(email: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                val launcherIntent = Intent(view, WoodlandListActivity::class.java)
                loginIntentLauncher.launch(launcherIntent)
            } else {
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }

    }
//Sign-up with just email an password
    fun doSignUp(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                val launcherIntent = Intent(view, WoodlandListActivity::class.java)
                loginIntentLauncher.launch(launcherIntent)
            } else {
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }
    private fun registerLoginCallback(){
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}