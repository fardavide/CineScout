package cinescout.android

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.setConsent

fun CineScoutAnalytics(context: Context) = FirebaseAnalytics.getInstance(context)
    .apply {
        setAnalyticsCollectionEnabled(true)
        setConsent {
            analyticsStorage = FirebaseAnalytics.ConsentStatus.GRANTED
        }
    }
