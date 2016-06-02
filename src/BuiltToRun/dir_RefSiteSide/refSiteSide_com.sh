#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$5\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     RefSiteSide.RefSiteServer $1 $2 $3 $4
