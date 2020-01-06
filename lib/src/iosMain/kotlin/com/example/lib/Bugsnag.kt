package com.example.lib

import framework.Bugsnag.Bugsnag
import framework.Bugsnag.BugsnagConfiguration

actual object Bugsnag {

    actual fun configure(apiKey: String) {
        Bugsnag.startBugsnagWithConfiguration(
            BugsnagConfiguration().apply {
                setApiKey(apiKey)
            }
        )
    }

}
