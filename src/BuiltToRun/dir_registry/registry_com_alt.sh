#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///Users/tiagomagalhaes/Documents/IdeaProjects/sd_project3/src/BuiltToRun/dir_registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.registry.ServerRegisterRemoteObject $1 $2 $3
