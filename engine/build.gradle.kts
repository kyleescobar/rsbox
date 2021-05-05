dependencies {
    api(project(":common"))
    api(project(":config"))
    api(project(":cache"))
    api("io.netty:netty-all:_")
    implementation("org.slf4j:slf4j-api:_")
    implementation("org.slf4j:slf4j-simple:_")
}