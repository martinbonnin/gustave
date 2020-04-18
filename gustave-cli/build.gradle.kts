plugins {
    kotlin("jvm").version("1.3.72")
    id("maven-publish")
    id("application")
}

application {
    mainClassName = "net.mbonnin.gustave.MainKt"
}

tasks.named("run").configure {
    this as JavaExec
    workingDir = rootProject.projectDir
    standardInput = System.`in`
}

dependencies {
    implementation(project(":gustave"))
    implementation(kotlin("stdlib"))
    implementation("com.github.ajalt:clikt:2.6.0")
}

