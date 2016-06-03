#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///Users/tiagomagalhaes/Documents/IdeaProjects/sd_project3/src/BuiltToRun/dir_PlaygroundSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.shared_mem.PlaygroundSide.PlaygroundServer $1 $2 $3 $4
