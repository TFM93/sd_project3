#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$5\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.shared_mem.BenchSide.BenchServer $1 $2 $3 $4
