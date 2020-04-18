#!/bin/bash

//usr/bin/env echo '
/**** BOOTSTRAP kscript ****\'>/dev/null
command -v kscript >/dev/null 2>&1 || curl -L "https://git.io/fpF1K" | bash 1>&2
exec kscript $0 "$@"
\*** IMPORTANT: Any code including imports and annotations must come after this line ***/


@file:MavenRepository("mbonnin", "https://dl.bintray.com/mbonnin/default/")
@file:DependsOn("net.mbonnin.gustave:gustave-cli:0.0.3")

import net.mbonnin.gustave.main

main(args)