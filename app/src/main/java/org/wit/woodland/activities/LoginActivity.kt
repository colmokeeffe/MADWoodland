package org.wit.woodland.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.woodland.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(){
    lateinit var presenter: AuthActivity
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        presenter = AuthActivity( this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        binding.signUp.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email == "" || password == "") {
                showSnackBar("please provide email and password")
            }
            else {
                presenter.doSignUp(email,password)
            }
        }

        binding.logIn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email == "" || password == "") {
                showSnackBar("please provide email and password")
            }
            else {
                presenter.doLogin(email,password)
            }
        }
    }
    fun showSnackBar(message: CharSequence){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .show()
    }
    fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }
}