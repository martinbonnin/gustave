group = "net.mbonnin"
version = "0.0.3-SNAPSHOT"

subprojects {
    repositories {
        mavenCentral()
    }
}

apply(from = "publishing.gradle.kts")
