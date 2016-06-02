#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$4\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     registry.ServerRegisterRemoteObject $1 $2 $3
