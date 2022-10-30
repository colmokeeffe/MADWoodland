package org.wit.woodland.activities

import android.os.Bundle
import org.wit.woodland.R
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}

/*THIS IS NOT WORKING, LOOK AT IT AGAIN LATER ----COMMENT OUT

val user = FirebaseAuth.getInstance().currentUser


fun doUpdateEmail(email: String) {
    user!!.updateEmail("user@google.com")
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                i("User email address updated.")
        } else {
            i("Login failed:")
            }
}


    val newPassword = "SOME-SECURE-PASSWORD"
    fun doUpdatePassword(password: String) {
        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    i("User password address updated.")
                }
            }


        fun doDeleteUser() {
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        i("User deleted.")
                    }
                }
        }
        //ADD IN STATISTICS, GRAPHS??
    }

*/