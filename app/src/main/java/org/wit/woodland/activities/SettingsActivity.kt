package org.wit.woodland.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.wit.woodland.R
import android.app.Activity
import timber.log.Timber.i

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}
/* THIS IS NOT WORKING ----COMMENT OUT
        val user = FirebaseAuth.getInstance().currentUser!!.uid
        if (user != null) {
            updateUserEmail.setText(FirebaseAuth.getInstance().currentUser?.email)
            updateUserPassword.setText("********")
        } else {
            i("Not logged in")
        }

        save_user_settings.setOnClickListener() {
            val user = FirebaseAuth.getInstance().currentUser
            user!!.updateEmail(updateUserEmail.toString())
            user!!.updatePassword(updateUserPassword.toString())
            i(user.email)

            startActivity(Intent(this, WoodlandListActivity::class.java))

        }
    }
    //ADD IN STATISTICS
    //CHANGE USER NAME, TIE IN WITH THE SIGNUP ACTIVITY
*/
