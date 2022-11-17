plugins {
    java
    `kotlin-dsl`
    application
    id("nu.studer.jooq") version "8.0" apply false
}

repositories {
    mavenCentral()
}

allprojects {

    group = "ru.aleksseii"
    version = "1.0-SNAPSHOT"

    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("nu.studer.jooq")
    }

    dependencies {

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")

        testImplementation("org.hamcrest:hamcrest-all:1.3")

        implementation("com.google.inject:guice:5.1.0")

        implementation("com.zaxxer:HikariCP:5.0.1")

        implementation("org.jooq:jooq:3.17.5")
        implementation("org.jooq:jooq-codegen:3.17.5")
        implementation("org.jooq:jooq-meta:3.17.5")

        implementation("org.postgresql:postgresql:42.5.0")

        implementation("org.flywaydb:flyway-core:9.8.1")

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
