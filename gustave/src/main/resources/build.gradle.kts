plugins {
    kotlin("jvm").version("{{kotlinVersion}}")
    id("maven-publish")
}

group = "{{group}}"
version = "0.0.1-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components.findByName("kotlin"))

            pom {
                groupId = rootProject.group as String
                artifactId = "{{projectName}}"
                version = rootProject.version as String

                name.set("{{projectName}}")
                packaging = "jar"
                url.set("https://github.com/{{githubRepo}}")

                scm {
                    url.set("https://github.com/{{githubRepo}}")
                    connection.set("https://github.com/{{githubRepo}}")
                    developerConnection.set("https://github.com/{{githubRepo}}")
                }

                licenses {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }

                developers {
                    developer {
                        id.set("{{developer}}")
                        name.set("{{developer}}")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "bintray"
            url = uri("https://api.bintray.com/maven/mbonnin/default/gustave/;publish=1;override=1")
            credentials {
                username = System.getenv("BINTRAY_USER")
                password = System.getenv("BINTRAY_API_KEY")
            }
        }
        maven {
            name = "ojo"
            url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
            credentials {
                username = System.getenv("BINTRAY_USER")
                password = System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
