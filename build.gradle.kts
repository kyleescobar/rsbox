plugins {
    kotlin("jvm") version "1.5.0"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "io.rsbox"
    version = "1.0.0"

    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        implementation(kotlin("stdlib"))

        implementation("org.koin:koin-core:_")
        implementation("org.koin:koin-core-ext:_")
        implementation("org.tinylog:tinylog-api-kotlin:_")
        implementation("org.tinylog:tinylog-impl:_")
    }
}
