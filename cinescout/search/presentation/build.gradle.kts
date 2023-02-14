apply plugin: 'cinescout.android.compose.library'

moduleDependencies {
    add 'common'
    add 'design'
    add 'movies.domain'
    add 'screenplay:domain'
    add 'search.domain'
    add 'store'
    add 'test:compose'
    add 'test:kotlin'
    add 'tvShows.domain'
    add 'utils:android'
    add 'utils:kotlin'
}

dependencies {
    ksp libs.koin.ksp
    
    implementation libs.androidx.lifecycle.runtime 
    implementation libs.bundles.base 
    implementation libs.bundles.compose 

    debugImplementation libs.bundles.compose.debug 

    testImplementation libs.bundles.test.kotlin 
    androidTestImplementation libs.bundles.test.android 
}
