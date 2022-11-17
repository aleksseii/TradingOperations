plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":models"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}