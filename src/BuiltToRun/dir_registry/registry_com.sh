#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$4\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.registry.ServerRegisterRemoteObject $1 $2 $3
