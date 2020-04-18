#!/usr/bin/env kscript

@file:MavenRepository("mbonnin", "https://dl.bintray.com/mbonnin/default/")
@file:DependsOn("net.mbonnin.gustave:gustave-cli:0.0.3")

import net.mbonnin.gustave.main

main(args)