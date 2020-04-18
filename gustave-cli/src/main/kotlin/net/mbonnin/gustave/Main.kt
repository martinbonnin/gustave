package net.mbonnin.gustave

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import java.io.File


val initCommand = object : CliktCommand(name = "init", help = "creates a new gradle/kotlin projoct in the current directory") {
    override fun run() {
        Gustave().init(File("."))
    }
}
val makeReleaseCommange = object : CliktCommand(name = "makeRelease", help = "creates a tag and bump versions") {
    override fun run() {
        Gustave().makeRelease()
    }
}

fun main(args: Array<String>) {
    object : CliktCommand() {
        override fun run() {
        }
    }.subcommands(
            initCommand,
            makeReleaseCommange
    ).main(args)
}