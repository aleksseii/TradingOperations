plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    jooqGenerator("org.postgresql:postgresql:42.5.0")
}

jooq {
    version.set("3.17.5") // default
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS) // default

    configurations {
        create("main") { // name of the jOOQ configuration

            generateSchemaSourceOnCompilation.set(false) // custom

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN

                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://127.0.0.1:5432/trading_operations_db" // custom
                    user = "postgres" // custom
                    password = "postgres" // custom
                }

                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"

                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        includes = ".*"
                        excludes = "flyway_schema_history"
                    }

                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }

                    target.apply {
                        packageName = "generated" // custom
                        directory = "src/main/java" // custom
                    }

                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
