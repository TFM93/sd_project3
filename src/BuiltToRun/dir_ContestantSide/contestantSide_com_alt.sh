#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///Users/ivosilva/Documents/university/sd/sd_project3/sd_project3/src/BuiltToRun/dir_ContestantSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.active_entities.ContestantSide.ContestantClient $1 $2 $3 $4 $5 $6 $7 $8
