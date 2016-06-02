printf "Setting RMI registry ...\n"
./set_rmiregistry_alt.sh $2 &
sleep 2

printf "\nStarting Service Register ...\n"
cd BuiltToRun/dir_registry/
./registry_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Repository ...\n"
cd BuiltToRun/dir_RepoSide/
./repositorySide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting RefereeSite ...\n"
cd BuiltToRun/dir_RefSiteSide/
./refSiteSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Playground ...\n"
cd BuiltToRun/dir_PlaygroundSide/
./playgroundSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Bench ...\n"
cd BuiltToRun/dir_BenchSide/
./benchSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Referee ...\n"
cd BuiltToRun/dir_RefereeSide/
./refereeSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Coaches ...\n"
cd BuiltToRun/dir_CoachSide/
./coachSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Contestants ...\n"
cd BuiltToRun/dir_ContestantsSide/
./contestantSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nRunning ...\n\n"