plugins {
    id("application")
}

tasks.withType<JavaExec> {
    mainClass.set("io.rsbox.launcher.Launcher")
    workingDir = rootProject.projectDir
    standardInput = System.`in`
}

dependencies {
    implementation(project(":common"))
    implementation(project(":config"))
    implementation(project(":engine"))
    implementation(project(":cache"))
}