#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///Users/tiagomagalhaes/Documents/IdeaProjects/sd_project3/src/BuiltToRun/dir_ContestantSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.active_entities.ContestantSide.ContestantClient $1 $2 $3 $4 $5 $6 $7
