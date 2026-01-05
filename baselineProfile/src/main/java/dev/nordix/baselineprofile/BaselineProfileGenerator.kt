package dev.nordix.baselineprofile

import android.content.Context
import android.content.Intent
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        val pkg = InstrumentationRegistry.getArguments()
            .getString("targetAppId")
            ?: error("targetAppId not passed")

        // Regular launcher
        rule.collect(
            packageName = pkg,
            includeInStartupProfile = true
        ) {
            pressHome()
            startActivityAndWait(launchIntent(pkg))
        }

        // WidgetControlActivity with extras
        rule.collect(
            packageName = pkg,
            includeInStartupProfile = false // not a launcher-start
        ) {
            pressHome()
            startActivityAndWait(widgetIntent(pkg))
        }
    }

    private fun widgetIntent(
        packageName: String
    ): Intent {
        return Intent().apply {
            setClassName(
                packageName,
                "dev.nordix.irbridge.feature.widget.WidgetControlActivity"
            )
            action = "dev.nordix.irbridge.action.OPEN_REMOTE_DIALOG"
            putExtra("extra_remote_id", REMOTE_ID)
            putExtra("extra_from_widget", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun launchIntent(packageName: String): Intent {
        val context = InstrumentationRegistry.getInstrumentation().context
        val pm = context.packageManager

        return pm.getLaunchIntentForPackage(packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ?: error("No launch intent for package=$packageName (no MAIN/LAUNCHER activity?)")
    }

    companion object {
        private const val REMOTE_ID = "b7e670bc-5fb9-4979-874d-4f1897d2af84"
    }
}