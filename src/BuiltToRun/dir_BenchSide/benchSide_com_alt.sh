#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///Users/ivosilva/Documents/university/sd/sd_project3/sd_project3/src/BuiltToRun/dir_BenchSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     BenchSide.BenchServer $1 $2 $3 $4
