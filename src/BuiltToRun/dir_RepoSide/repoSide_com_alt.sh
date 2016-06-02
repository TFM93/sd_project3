#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///Users/ivosilva/Documents/university/sd/sd_project3/sd_project3/src/BuiltToRun/dir_RepoSide/"\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     RepoSide.RepoServer $1 $2 $3 $4 $5
