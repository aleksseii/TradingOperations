plugins {
    id("java")
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
}

allprojects {

    group = "ru.aleksseii"
    version = "1.0-SNAPSHOT"

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")

        testImplementation("org.hamcrest:hamcrest-all:1.3")

        implementation("com.google.inject:guice:5.1.0")

        implementation("org.postgresql:postgresql:42.5.0")

        implementation("org.flywaydb:flyway-core:9.6.0")

        implementation("org.jetbrains:annotations:23.0.0")

        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
