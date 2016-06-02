#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$6\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     RepoSide.RepoServer $1 $2 $3 $4 $5
