plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":jooq-generated"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}