#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///Users/tiagomagalhaes/Documents/IdeaProjects/sd_project3/src/BuiltToRun/dir_RepoSide/"\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.shared_mem.RepoSide.RepoServer $1 $2 $3 $4 $5
