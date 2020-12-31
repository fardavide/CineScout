import studio.forface.easygradle.dsl.`assert4k version`
import studio.forface.easygradle.dsl.`coroutines version`
import studio.forface.easygradle.dsl.`kermit version`
import studio.forface.easygradle.dsl.`klock version`
import studio.forface.easygradle.dsl.`koin version`
import studio.forface.easygradle.dsl.`kotlin version`
import studio.forface.easygradle.dsl.`ktor version`
import studio.forface.easygradle.dsl.`mockK version`
import studio.forface.easygradle.dsl.`picnic version`
import studio.forface.easygradle.dsl.`serialization version`
import studio.forface.easygradle.dsl.`sqlDelight version`
import studio.forface.easygradle.dsl.android.`accompanist version`
import studio.forface.easygradle.dsl.android.`activity version`
import studio.forface.easygradle.dsl.android.`android-compose version`
import studio.forface.easygradle.dsl.android.`appcompat version`
import studio.forface.easygradle.dsl.android.`ktx version`

internal fun initVersions() {

    // kotlin
    `kotlin version` =  "1.4.20" //
    `coroutines version` = "1.4.1" // Nov 03, 2020
    `serialization version` = "1.0.1" // Oct 28, 2020

    // others
    `assert4k version` = "0.5.7" // Aug 16, 2020
    `kermit version` = "0.1.8" // Aug 24, 2020
    `klock version` = "1.12.0" // Aug 15, 2020
    `koin version` = "2.2.1" // Nov 16, 2020
    `ktor version` = "1.4.3" // Dec 01, 2020
    `mockK version` = "1.10.3" // Nov 30, 2020
    `picnic version` = "0.4.0" // Aug 12, 2020
    `sqlDelight version` = "1.4.4" // Oct 08, 2020

    // Android
    `accompanist version` = "0.4.0" // Dec 02, 2020
    `activity version` = "1.2.0-beta01" // Aug 21, 2020
    `appcompat version` = "1.3.0-alpha02" // Aug 21, 2020
    `android-compose version` = "1.0.0-alpha08" // Dec 02, 2020
    `ktx version` = "1.5.0-alpha05" // Nov 11, 2020
}

internal const val `koin-mp version` = "3.0.0-alpha-4" // Oct 01, 2020
