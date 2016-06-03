#!/bin/bash
username=sd0203
password=diogocardoso
confFile=localhost.config

# Read configuration file
while read line
do
    splitIndex=(${line//=/ })
    value=${line##*=}

    if [[ "$splitIndex" =~ "REPO_HOST_NAME" ]]; then
        repositoryHostName=${value}
    elif [[ "$line" =~ "REPO_PORT_NUM" ]]; then
        repositoryPortNum=${value}
    elif [[ "$line" =~ "REFSITE_HOST_NAME" ]]; then
        refSiteHostName=${value}
    elif [[ "$line" =~ "REFSITE_PORT_NUM" ]]; then
        refSitePortNum=${value}
    elif [[ "$line" =~ "BENCH_HOST_NAME" ]]; then
        benchHostName=${value}
    elif [[ "$line" =~ "BENCH_PORT_NUM" ]]; then
        benchPortNum=${value}
    elif [[ "$line" =~ "PLAYG_HOST_NAME" ]]; then
        playgHostName=${value}
    elif [[ "$line" =~ "PLAYG_PORT_NUM" ]]; then
        playgPortNum=${value}
    elif [[ "$line" =~ "REFEREE_HOST_NAME" ]]; then
        refereeHostName=${value}
    elif [[ "$line" =~ "COACH_HOST_NAME" ]]; then
        coachHostName=${value}
    elif [[ "$line" =~ "CONTESTANT_HOST_NAME" ]]; then
        contestantHostName=${value}
    elif [[ "$line" =~ "REGISTRY_HOST_NAME" ]]; then
        registryHostName=${value}
    elif [[ "$line" =~ "REGISTRY_PORT_NUM" ]]; then
        registryPortNum=${value}
    elif [[ "$line" =~ "REGISTRY_LISTENING_PORT_NUM" ]]; then
        registryListeningPortNum=${value}
    elif [[ "$line" =~ "N_PLAYERS_TEAM" ]]; then
        nPlayers=${value}
    elif [[ "$line" =~ "N_PLAYERS_PUSHING" ]]; then
        nPlayersPushing=${value}
    elif [[ "$line" =~ "N_TRIALS" ]]; then
        nTrials=${value}
    elif [[ "$line" =~ "N_GAMES" ]]; then
        nGames=${value}
    elif [[ "$line" =~ "KNOCKOUT_DIF" ]]; then
        knockDiff=${value}

    fi
done < $confFile

nEntities=$((6+$nPlayers))

printf "\nClosing active servers ...\n"
{
sudo kill `sudo lsof -t -i:22210`
sudo kill `sudo lsof -t -i:22211`
sudo kill `sudo lsof -t -i:9136`
sudo kill `sudo lsof -t -i:9137`
sudo kill `sudo lsof -t -i:9138`
sudo kill `sudo lsof -t -i:9139`
} &> /dev/null

printf "Setting RMI registry ...\n"
./set_rmiregistry_alt.sh $registryPortNum &
sleep 2

cd BuiltToRun/

printf "\nStarting Service Register ...\n"
cd dir_registry/
./registry_com_alt.sh $registryHostName $registryPortNum $registryListeningPortNum &
cd ..
sleep 2

printf "\nStarting Repository ...\n"
cd dir_RepoSide/
./repoSide_com_alt.sh $registryHostName $registryPortNum $repositoryPortNum $nPlayers $nPlayersPushing &
cd ..
sleep 2

printf "\nStarting Bench ...\n"
cd dir_BenchSide/
./benchSide_com_alt.sh $registryHostName $registryPortNum $benchPortNum $nPlayers &
cd ..
sleep 2

printf "\nStarting Playground ...\n"
cd dir_PlaygroundSide/
./playgroundSide_com_alt.sh $registryHostName $registryPortNum $playgPortNum $nPlayers &
cd ..
sleep 2

printf "\nStarting RefSite ...\n"
cd dir_RefSiteSide/
./refSiteSide_com_alt.sh $registryHostName $registryPortNum $refSitePortNum $nPlayers &
cd ..
sleep 2

printf "\nStarting Referee ...\n"
cd dir_RefereeSide/
./refereeSide_com_alt.sh $registryHostName $registryPortNum $nPlayers $nPlayersPushing $nTrials $nGames $knockDiff &
cd ..
sleep 2

printf "\nStarting Coaches ...\n"
cd dir_CoachSide/
./coachSide_com_alt.sh $registryHostName $registryPortNum $nPlayers $nPlayersPushing $nTrials $nGames $knockDiff &
cd ..
sleep 2

printf "\nStarting Contestants ...\n"
cd dir_ContestantSide/
./contestantSide_com_alt.sh $registryHostName $registryPortNum $nPlayers $nPlayersPushing $nTrials $nGames $knockDiff &
cd ..
sleep 2

printf "\nAll Running ...\n\n"