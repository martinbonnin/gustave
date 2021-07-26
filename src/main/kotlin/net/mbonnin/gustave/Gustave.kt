package net.mbonnin.gustave

import java.io.File

class Gustave {
    var versionGetter: () -> String = {
        val file = File("build.gradle.kts")
        val version = file.readLines().mapNotNull {
            Regex("^version = \"(.*)").matchEntire(it)?.groupValues?.get(1)
        }.firstOrNull()

        require(version != null) {
            "cannot find the version in ${file.name}"
        }

        require(version.endsWith("-SNAPSHOT")) {
            "version is currently $version and doesn't end with -SNAPSHOT. Gustave works by dropping the '-SNAPSHOT' to create the version, please check your workflow."
        }

        version.substringBeforeLast("-SNAPSHOT")
    }

    var versionSetter: (String) -> Unit =  {version ->
        val file = File("build.gradle.kts")
        val newContent = file.readLines().map {
            it.replace(Regex("^version = \".*\""), "version = \"$version\"")
        }.joinToString(separator = "\n", postfix = "\n")
        file.writeText(newContent)
    }

    private fun runCommand(vararg args: String): String {
        val builder = ProcessBuilder(*args)
                .redirectError(ProcessBuilder.Redirect.INHERIT)

        val process = builder.start()
        val ret = process.waitFor()

        val output = process.inputStream.bufferedReader().readText()
        if (ret != 0) {
            throw java.lang.Exception("command ${args.joinToString(" ")} failed:\n$output")
        }

        return output
    }

    private fun getNext(version: String, position: Int) = version.split(".").mapIndexed { index, s ->
        when {
            index == position -> (s.toInt() + 1).toString()
            index > position -> "0"
            else -> s
        }
    }.joinToString(".")

    private fun getNextPatch(version: String) = getNext(version, 2)
    private fun getNextMinor(version: String) = getNext(version, 1)
    private fun getNextMajor(version: String) = getNext(version, 0)

    fun makeRelease() {
        if (runCommand("git", "status", "--porcelain").isNotEmpty()) {
            println("Your git repo is not clean. Make sur to stash or commit your changes before making a release")
            System.exit(1)
        }

        val version = versionGetter()
        val nextPatch = getNextPatch(version)
        val nextMinor = getNextMinor(version)
        val nextPatchAfterMinor = getNextPatch(nextMinor)
        val nextMajor = getNextMajor(version)
        val nextPatchAfterMajor = getNextPatch(nextMajor)

        var tagVersion: String = ""

        while (tagVersion.isEmpty()) {
            println("Current version is '$version-SNAPSHOT'.")
            println("1. patch: tag $version and bump to $nextPatch")
            println("2. minor: tag $nextMinor and bump to $nextPatchAfterMinor")
            println("3. major: tag $nextMajor and bump to $nextPatchAfterMajor")
            println("What do you want to do [1/2/3]?")

            val answer = readLine()!!.trim()
            when (answer) {
                "1" -> tagVersion = version
                "2" -> tagVersion = nextMinor
                "3" -> tagVersion = nextMajor
            }
        }

        versionSetter(tagVersion)

        runCommand("git", "commit", "-a", "-m", "release $tagVersion")
        runCommand("git", "tag", tagVersion)

        val snapshot = "${getNextPatch(tagVersion)}-SNAPSHOT"
        versionSetter(snapshot)
        runCommand("git", "commit", "-a", "-m", "version is now $snapshot")

        println("Everything is done. Verify everything is ok and type `git push origin master --tags` to trigger the new version.")
    }

    fun init(dir: File) {
        check(!File(dir, "settings.gradle.kts").exists()) {
            "a settings.gradle.kts file already exists in this directory"
        }

        val replacements = mapOf(
                "projectName" to "dactylo",
                "kotlinVersion" to "1.5.30-M1",
                "group" to "net.mbonnin",
                "githubRepo" to "martinbonnin/dactylo",
                "developer" to "Martin Bonnin"
        )

        copyResource("build.gradle.kts", dir, replacements)
        copyResource("settings.gradle.kts", dir, replacements)

        copyResource(".gitignore", dir, emptyMap())
        copyResource("gradle.properties", dir, emptyMap())
    }

    private fun copyResource(resourceName: String, dir: File, replacements: Map<String, String>) {
        val inputStream = this::class.java.classLoader.getResourceAsStream("/$resourceName")
        check(inputStream != null) {
            "cannot find resource: $resourceName"
        }
        var template = inputStream.reader().readText()

        replacements.forEach {
           template = template.replace("{{${it.key}}}", it.value)
        }

        File(dir, resourceName).writeText(template)
    }
}