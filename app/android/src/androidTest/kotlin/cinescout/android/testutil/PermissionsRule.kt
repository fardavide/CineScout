package cinescout.android.testutil

import android.Manifest
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import cinescout.android.CineScoutApplication
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class PermissionsRule(private val permissions: Set<Permission>) : TestRule {

    constructor(vararg permissions: Permission) : this(permissions.toSet())

    override fun apply(base: Statement, description: Description): Statement {
        val context = ApplicationProvider.getApplicationContext<CineScoutApplication>()
        for ((minSdk, permission) in permissions) {
            if (Build.VERSION.SDK_INT >= minSdk) {
                InstrumentationRegistry.getInstrumentation()
                    .uiAutomation
                    .grantRuntimePermission(context.packageName, permission)
            }
        }
        return base
    }

    data class Permission(
        val minSdk: Int,
        val value: String
    )
}

fun PostNotificationsRule() = PermissionsRule(
    PermissionsRule.Permission(
        Build.VERSION_CODES.TIRAMISU,
        Manifest.permission.POST_NOTIFICATIONS
    )
)
