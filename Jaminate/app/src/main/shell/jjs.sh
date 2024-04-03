#!/bin/bash
. ../shell/classpath
/c/graaljs-community-jvm-24.0.0-windows-amd64/graaljs-community-24.0.0-windows-amd64/bin/js.exe --experimental-options --polyglot --vm.Djs.allowAllAccess=true --vm.Xss1g --vm.Xmx4g --jvm --vm.classpath="${GRAAL_CLASSPATH}" "$@"
