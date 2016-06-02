#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$9\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     pt.ua.sd.RopeGame.active_entities.RefereeSide.RefereeClient $1 $2 $3 $4 $5 $6 $7 $8
