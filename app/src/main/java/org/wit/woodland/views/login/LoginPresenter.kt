package org.wit.woodland.views.login


import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.jetbrains.anko.toast
import org.wit.woodland.R
import org.wit.woodland.models.firebase.WoodlandFireStore
import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView
import org.wit.woodland.views.VIEW


class LoginPresenter(view: BaseView) : BasePresenter(view)
{
    var fireStore: WoodlandFireStore? = null
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()
    var errorStatus = MutableLiveData<Boolean>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        auth = FirebaseAuth.getInstance()
        if (app.woodlands is WoodlandFireStore)
            {
                fireStore = app.woodlands as WoodlandFireStore
                liveFirebaseUser.postValue(auth.currentUser)
                errorStatus.postValue(false)
            }
            configureGoogleSignIn()
        }
//skip tthe login process
    fun doSkipLogin()
    {
        if (fireStore != null)
        {
            fireStore!!.fetchWoodlands{
                view?.hideProgress()
                view?.navigateTo(VIEW.LIST) }
        }
        else
        {
            view?.hideProgress()
            view?.navigateTo(VIEW.LIST)
        }
    }

   fun configureGoogleSignIn()
   {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient.value = GoogleSignIn.getClient(app.applicationContext,gso)
    }


    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount)
    {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(app.mainExecutor) { task ->
                if (task.isSuccessful)
                {
                    fireStore!!.fetchWoodlands {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                    liveFirebaseUser.postValue(auth.currentUser)
                }
                else
                {
                    errorStatus.postValue(true)
                }
            }
    }

    fun doLogin(email: String, password: String)
    {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful)
            {
                if (fireStore != null)
                {
                    fireStore!!.fetchWoodlands {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST) }
                }
                else
                {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.LIST)
                }
            }
            else
            //not logged in
            {
                view?.hideProgress()
                view?.toast("Sign up Failure: ${task.exception?.message}")
            }
        }
    }

    /*fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            firebaseAuthWithGoogle(account!!)
                        } catch (e: ApiException) {
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {

                    } else -> { }
                }
            }
    }
*/

    fun doSignUp(email: String, password: String)
    {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful)
            {
                fireStore!!.fetchWoodlands{
                    view?.hideProgress()
                    view?.navigateTo(VIEW.LIST)
                }
            }
            else
            {
                view?.hideProgress()
                view?.toast("Sign up Failure: ${task.exception?.message}")
            }
        }
    }


    fun authWithGoogle(acct: GoogleSignInAccount)
    {
        firebaseAuthWithGoogle(acct)
    }

}
