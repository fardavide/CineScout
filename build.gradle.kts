import me.proton.core.util.gradle.*

buildscript {
    initVersions()
    repositories(repos)
    dependencies(classpathDependencies)
}

allprojects {
    repositories(repos)
}

setupKotlin(
    "-XXLanguage:+NewInference",
    "-Xuse-experimental=kotlin.Experimental",
    "-XXLanguage:+InlineClasses",
    "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
)
setupDetekt { "tokenAutoComplete" !in it.name }

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
