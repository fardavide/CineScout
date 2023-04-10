package cinescout.plugins.android

import io.github.flank.gradle.Device
import io.github.flank.gradle.SimpleFlankExtension
import org.gradle.api.Project

fun Project.configureSimpleFlankExtension(ext: SimpleFlankExtension) {
    ext.credentialsFile.set(rootProject.file("ftl-credentials.json"))
    ext.devices.set(
        listOf(
            Device(id = "oriole", osVersion = 32, make = "Google", model = "Pixel 6")
        )
    )
    ext.numFlakyTestAttempts.set(2)
    ext.testTimeout.set("30m")
}
