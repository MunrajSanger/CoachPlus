package uk.ac.aber.dcs.cs31620.coachplus

import android.app.Application
import com.google.firebase.FirebaseApp

class CoachPlusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}