#!/bin/bash
username=sd0203
password=diogocardoso
confFile=rg.config

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


url="http://"$registryHostName"/sd0203/classes/"

printf "\nClosing active servers and cleaning machines ...\n"
{
sshpass -p $password ssh $username@$registryHostName killall -u sd0203
sshpass -p $password ssh $username@$registryHostName "rm -rfv sd_project3/*"
sshpass -p $password ssh $username@$registryHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$repositoryHostName killall -u sd0203
sshpass -p $password ssh $username@$repositoryHostName "rm -rfv sd_project3/*"
sshpass -p $password ssh $username@$registryHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$refSiteHostName killall -u sd0203
sshpass -p $password ssh $username@$refSiteHostName "rm -rfv sd_project3/*"
sshpass -p $password ssh $username@$registryHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$benchHostName killall -u sd0203
sshpass -p $password ssh $username@$benchHostName "rm -rfv sd_project3/*"
sshpass -p $password ssh $username@$registryHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$playgHostName killall -u sd0203
sshpass -p $password ssh $username@$playgHostName "rm -rfv sd_project3/*"
sshpass -p $password ssh $username@$registryHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$refereeHostName killall -u sd0203
sshpass -p $password ssh $username@$refereeHostName "rm -rfv sd_project3/*"
sshpass -p $password ssh $username@$registryHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$coachHostName killall -u sd0203
sshpass -p $password ssh $username@$coachHostName "rm -rfv sd_project3/*"
sshpass -p $password ssh $username@$registryHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$contestantHostName killall -u sd0203
sshpass -p $password ssh $username@$contestantHostName "rm -rfv sd_project3/*"
sshpass -p $password ssh $username@$registryHostName "rm -rfv Public/classes/*"
} &> /dev/null

printf "\nSetting RMI registry ...\n"
sshpass -p $password scp set_rmiregistry.sh $username@$registryHostName:/home/$username/sd_project3/
sshpass -p $password scp -r BuiltToRun/interfaces $username@$registryHostName:/home/$username/Public/classes/
sshpass -p $password scp -r BuiltToRun/info $username@$registryHostName:/home/$username/Public/classes/
sshpass -p $password ssh -f $username@$registryHostName "sudo ./sd_project3/set_rmiregistry.sh $registryPortNum $url"
sleep 2

printf "\nStarting Service Register ...\n"
sshpass -p $password scp -r BuiltToRun/dir_registry $username@$registryHostName:/home/$username/sd_project3/
sshpass -p $password ssh -f $username@$registryHostName "cd sd_project3/dir_registry/ ; ./registry_com.sh $registryHostName $registryPortNum $registryListeningPortNum $url &"
sleep 2

printf "\nStarting Referee Site ...\n"
sshpass -p $password scp -r BuiltToRun/dir_RefSiteSide $username@$refSiteHostName:/home/$username/sd_project3/
sshpass -p $password ssh -f $username@$refSiteHostName "cd sd_project3/dir_RefSiteSide/ ; ./refSiteSide_com.sh $registryHostName $registryPortNum $refSitePortNum $nPlayers $url &"
sleep 2

printf "\nStarting Bench ...\n"
sshpass -p $password scp -r BuiltToRun/dir_BenchSide $username@$benchHostName:/home/$username/sd_project3/
sshpass -p $password ssh -f $username@$benchHostName "cd sd_project3/dir_BenchSide/ ; ./benchSide_com.sh $registryHostName $registryPortNum $benchPortNum $nPlayers $url &"
sleep 2

printf "\nStarting Playground ...\n"
sshpass -p $password scp -r BuiltToRun/dir_PlaygroundSide $username@$playgHostName:/home/$username/sd_project3/
sshpass -p $password ssh -f $username@$playgHostName "cd sd_project3/dir_PlaygroundSide/ ; ./playgroundSide_com.sh $registryHostName $registryPortNum $playgPortNum $nPlayers $url &"
sleep 2

printf "\nStarting Repository ...\n"
sshpass -p $password scp -r dir_RepoSide $username@$repositoryHostName:/home/$username/sd_project3/
sshpass -p $password ssh -f $username@$repositoryHostName "cd sd_project3/dir_RepoSide/ ; ./repoSide_com.sh $registryHostName $registryPortNum $repositoryPortNum $nPlayers $nPlayersPushing $url &"
sleep 2

printf "\nStarting Referee ...\n"
sshpass -p $password scp -r dir_RefereeSide $username@$refereeHostName:/home/$username/sd_project3/
sshpass -p $password ssh -f $username@$refereeHostName "cd sd_project3/dir_RefereeSide/ ; ./refereeSide_com.sh $registryHostName $registryPortNum $nPlayers $nPlayersPushing $nTrials $nGames $knockDiff $url &"
sleep 2

printf "\nStarting Coaches ...\n"
sshpass -p $password scp -r dir_CoachSide $username@$coachHostName:/home/$username/sd_project3/
sshpass -p $password ssh -f $username@$coachHostName "cd sd_project3/dir_CoachSide/ ; ./coachSide_com.sh  $registryHostName $registryPortNum $nPlayers $nPlayersPushing $nTrials $nGames $knockDiff $url &"
sleep 2

printf "\nStarting Contestants ...\n"
sshpass -p $password scp -r dir_ContestantSide $username@$contestantHostName:/home/$username/sd_project3/
sshpass -p $password ssh -f $username@$contestantHostName "cd sd_project3/dir_ContestantSide/ ; ./contestantSide_com.sh   $registryHostName $registryPortNum $nPlayers $nPlayersPushing $nTrials $nGames $knockDiff $url &"
sleep 2

printf "\nRunning ...\n\n"