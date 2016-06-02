#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///Users/ivosilva/Documents/university/sd/sd_project3/sd_project3/src/BuiltToRun/dir_registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.registry.ServerRegisterRemoteObject $1 $2 $3
