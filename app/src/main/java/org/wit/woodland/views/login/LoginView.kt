package org.wit.woodland.views.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.woodland.R
import org.wit.woodland.models.firebase.WoodlandFireStore
import org.wit.woodland.views.BaseView


class LoginView : BaseView(), AnkoLogger
{
    lateinit var startForResult : ActivityResultLauncher<Intent>
    lateinit var presenter: LoginPresenter
    //nor used now
    // var fireStore: WoodlandFireStore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init(toolbar, false)
        //create google button
        googleSignInButton.setSize(SignInButton.SIZE_WIDE)
        googleSignInButton.setColorScheme(0)//initialize the presenter here
        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter
        progressBar.visibility = View.GONE

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null)
        {
            presenter.doSkipLogin()
        }

        setupGoogleSignInCallback()

        signUp.setOnClickListener{
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else
            {
                presenter.doSignUp(email,password)
            }
        }
//google sign in listener
        googleSignInButton.setOnClickListener {
            googleSignIn()
        }

        logIn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else
            {
                presenter.doLogin(email,password)
            }
        }
    }

//check to ensure that this is working correctly
    private fun googleSignIn()
    {
        val signInIntent = presenter.googleSignInClient.value!!.signInIntent
        startForResult.launch(signInIntent)
    }

    private fun setupGoogleSignInCallback()
    {
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode)
                {
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try
                        {
                            val account = task.getResult(ApiException::class.java)
                            presenter.authWithGoogle(account!!)
                        }
                        catch (_: ApiException)
                        {}
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    override fun showProgress()
    {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress()
    {
        progressBar.visibility = View.GONE
    }
}

