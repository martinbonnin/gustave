plugins {
    kotlin("jvm").version("1.3.72")
    id("maven-publish")
}

group = "net.mbonnin"
version = "0.0.2-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components.findByName("kotlin"))

            pom {
                groupId = rootProject.group as String
                artifactId = "gustave"
                version = rootProject.version as String

                name.set("gustave")
                packaging = "jar"
                url.set("https://github.com/martinbonnin/gustave")

                scm {
                    url.set("https://github.com/martinbonnin/gustave")
                    connection.set("https://github.com/martinbonnin/gustave")
                    developerConnection.set("https://github.com/martinbonnin/gustave")
                }

                licenses {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }

                developers {
                    developer {
                        id.set("Martin Bonnin")
                        name.set("Martin Bonnin")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "bintray"
            url = uri("https://api.bintray.com/maven/mbonnin/default/gustave;publish=1;override=1")
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
