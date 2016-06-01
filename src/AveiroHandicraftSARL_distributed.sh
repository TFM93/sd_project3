#!/bin/bash
username=sd0201
password=jeromarques
confFile=conf_distributed.txt

# Read configuration file
while read line
do
    splitIndex=`expr index "$line" =`

    if [[ "$line" =~ "repositoryHostName" ]]; then	
    	repositoryHostName=${line:$splitIndex}
    elif [[ "$line" =~ "repositoryPortNum" ]]; then
    	repositoryPortNum=${line:$splitIndex}    
    elif [[ "$line" =~ "workshopHostName" ]]; then
    	workshopHostName=${line:$splitIndex}
    elif [[ "$line" =~ "workshopPortNum" ]]; then
    	workshopPortNum=${line:$splitIndex}
    elif [[ "$line" =~ "shopHostName" ]]; then
    	shopHostName=${line:$splitIndex}
    elif [[ "$line" =~ "shopPortNum" ]]; then
    	shopPortNum=${line:$splitIndex}
    elif [[ "$line" =~ "storageHostName" ]]; then
    	storageHostName=${line:$splitIndex}
    elif [[ "$line" =~ "storagePortNum" ]]; then
    	storagePortNum=${line:$splitIndex}
    elif [[ "$line" =~ "registyHostName" ]]; then
    	registyHostName=${line:$splitIndex}
    elif [[ "$line" =~ "registyPortNum" ]]; then
    	registyPortNum=${line:$splitIndex}
    elif [[ "$line" =~ "registyListeningPortNum" ]]; then
    	registyListeningPortNum=${line:$splitIndex}
    elif [[ "$line" =~ "entrepreneurHostName" ]]; then
    	entrepreneurHostName=${line:$splitIndex}
    elif [[ "$line" =~ "craftsmenHostName" ]]; then
    	craftsmenHostName=${line:$splitIndex}
    elif [[ "$line" =~ "customersHostName" ]]; then
    	customersHostName=${line:$splitIndex}
    elif [[ "$line" =~ "initialNumberOfPrimeMaterialsInStorage" ]]; then
    	initialNumberOfPrimeMaterialsInStorage=${line:$splitIndex}
    elif [[ "$line" =~ "numberOfPrimeMaterialsForRestock" ]]; then
    	numberOfPrimeMaterialsForRestock=${line:$splitIndex}
    elif [[ "$line" =~ "nCustomers" ]]; then
    	nCustomer=${line:$splitIndex}
    elif [[ "$line" =~ "nCraftsman" ]]; then
    	nCraftsman=${line:$splitIndex}
    elif [[ "$line" =~ "initialGoodsInDisplay" ]]; then
    	initialGoodsInDisplay=${line:$splitIndex}
    elif [[ "$line" =~ "numberOfPrimeMaterialsInWorkshop" ]]; then
    	numberOfPrimeMaterialsInWorkshop=${line:$splitIndex}
    elif [[ "$line" =~ "logFileName" ]]; then
    	logFileName=${line:$splitIndex}
    elif [[ "$line" =~ "numberOfPrimeMaterialsByProduct" ]]; then
    	numberOfPrimeMaterialsByProduct=${line:$splitIndex}
    elif [[ "$line" =~ "limitOfProducts" ]]; then
    	limitOfProducts=${line:$splitIndex}
    fi
done < $confFile

nEntities=$(($nCustomer+$nCraftsman+1))
url="http://"$registyHostName"/sd0201/classes/"

printf "\nClosing active servers and cleaning machines ...\n"
{
sshpass -p $password ssh $username@$registyHostName killall -u sd0201
sshpass -p $password ssh $username@$registyHostName "rm -rfv P3_TP2_G1/*"
sshpass -p $password ssh $username@$registyHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$repositoryHostName killall -u sd0201
sshpass -p $password ssh $username@$repositoryHostName "rm -rfv P3_TP2_G1/*"
sshpass -p $password ssh $username@$registyHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$storageHostName killall -u sd0201
sshpass -p $password ssh $username@$storageHostName "rm -rfv P3_TP2_G1/*"
sshpass -p $password ssh $username@$registyHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$shopHostName killall -u sd0201
sshpass -p $password ssh $username@$shopHostName "rm -rfv P3_TP2_G1/*"
sshpass -p $password ssh $username@$registyHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$workshopHostName killall -u sd0201
sshpass -p $password ssh $username@$workshopHostName "rm -rfv P3_TP2_G1/*"
sshpass -p $password ssh $username@$registyHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$entrepreneurHostName killall -u sd0201
sshpass -p $password ssh $username@$entrepreneurHostName "rm -rfv P3_TP2_G1/*"
sshpass -p $password ssh $username@$registyHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$craftsmenHostName killall -u sd0201
sshpass -p $password ssh $username@$craftsmenHostName "rm -rfv P3_TP2_G1/*"
sshpass -p $password ssh $username@$registyHostName "rm -rfv Public/classes/*"

sshpass -p $password ssh $username@$customersHostName killall -u sd0201
sshpass -p $password ssh $username@$customersHostName "rm -rfv P3_TP2_G1/*"
sshpass -p $password ssh $username@$registyHostName "rm -rfv Public/classes/*"
} &> /dev/null

printf "\nSetting RMI registry ...\n"
sshpass -p $password scp set_rmiregistry.sh $username@$registyHostName:/home/$username/P3_TP2_G1/
sshpass -p $password scp -r interfaces $username@$registyHostName:/home/$username/Public/classes/
sshpass -p $password scp -r info $username@$registyHostName:/home/$username/Public/classes/
sshpass -p $password ssh -f $username@$registyHostName "./P3_TP2_G1/set_rmiregistry.sh $registyPortNum $url"
sleep 2

printf "\nStarting Service Register ...\n"
sshpass -p $password scp -r dir_registry $username@$registyHostName:/home/$username/P3_TP2_G1/
sshpass -p $password ssh -f $username@$registyHostName "cd P3_TP2_G1/dir_registry/ ; ./registry_com.sh $registyHostName $registyPortNum $registyListeningPortNum $url &"
sleep 2

printf "\nStarting Repository ...\n"
sshpass -p $password scp -r dir_repositorySide $username@$repositoryHostName:/home/$username/P3_TP2_G1/
sshpass -p $password ssh -f $username@$repositoryHostName "cd P3_TP2_G1/dir_repositorySide/ ; ./repositorySide_com.sh $registyHostName $registyPortNum $repositoryPortNum $nCustomer $nCraftsman $initialGoodsInDisplay $numberOfPrimeMaterialsInWorkshop $logFileName $url &"
sleep 2

printf "\nStarting Storage ...\n"
sshpass -p $password scp -r dir_storageSide $username@$storageHostName:/home/$username/P3_TP2_G1/
sshpass -p $password ssh -f $username@$storageHostName "cd P3_TP2_G1/dir_storageSide/ ; ./storageSide_com.sh $registyHostName $registyPortNum $storagePortNum $initialNumberOfPrimeMaterialsInStorage $numberOfPrimeMaterialsForRestock $url &"
sleep 2

printf "\nStarting WorkShop ...\n"
sshpass -p $password scp -r dir_workshopSide $username@$workshopHostName:/home/$username/P3_TP2_G1/
sshpass -p $password ssh -f $username@$workshopHostName "cd P3_TP2_G1/dir_workshopSide/ ; ./workshopSide_com.sh $registyHostName $registyPortNum $workshopPortNum $numberOfPrimeMaterialsByProduct $limitOfProducts $nCraftsman $nEntities $url &"
sleep 2

printf "\nStarting Shop ...\n"
sshpass -p $password scp -r dir_shopSide $username@$shopHostName:/home/$username/P3_TP2_G1/
sshpass -p $password ssh -f $username@$shopHostName "cd P3_TP2_G1/dir_shopSide/ ; ./shopSide_com.sh $registyHostName $registyPortNum $shopPortNum $nCraftsman $nCustomer $url &"
sleep 2

printf "\nStarting Entrepreneur ...\n"
sshpass -p $password scp -r dir_entrepreneurSide $username@$entrepreneurHostName:/home/$username/P3_TP2_G1/
sshpass -p $password ssh -f $username@$entrepreneurHostName "cd P3_TP2_G1/dir_entrepreneurSide/ ; ./entrepreneurSide_com.sh $registyHostName $registyPortNum $nEntities &"
sleep 2

printf "\nStarting Craftsmen ...\n"
sshpass -p $password scp -r dir_craftsmenSide $username@$craftsmenHostName:/home/$username/P3_TP2_G1/
sshpass -p $password ssh -f $username@$craftsmenHostName "cd P3_TP2_G1/dir_craftsmenSide/ ; ./craftsmenSide_com.sh  $registyHostName $registyPortNum $nCraftsman $nCustomer &"
sleep 2

printf "\nStarting Customers ...\n"
sshpass -p $password scp -r dir_customersSide $username@$customersHostName:/home/$username/P3_TP2_G1/
sshpass -p $password ssh -f $username@$customersHostName "cd P3_TP2_G1/dir_customersSide/ ; ./customersSide_com.sh   $registyHostName $registyPortNum $nEntities &"
sleep 2

printf "\nRunning ...\n\n"