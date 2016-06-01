#!/bin/bash
username=sd0201
password=jeromarques
confFile=conf_localhost.txt

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

printf "\nClosing active servers ...\n"
{
sudo kill `sudo lsof -t -i:22210`
sudo kill `sudo lsof -t -i:22211`
sudo kill `sudo lsof -t -i:22212`
sudo kill `sudo lsof -t -i:22213`
sudo kill `sudo lsof -t -i:22214`
sudo kill `sudo lsof -t -i:22215`
sudo kill `sudo lsof -t -i:22216`
sudo kill `sudo lsof -t -i:22217`
sudo kill `sudo lsof -t -i:22218`
sudo kill `sudo lsof -t -i:22219`
} &> /dev/null

printf "Setting RMI registry ...\n"
./set_rmiregistry_alt.sh $registyPortNum &
sleep 2

printf "\nStarting Service Register ...\n"
cd dir_registry/
./registry_com_alt.sh $registyHostName $registyPortNum $registyListeningPortNum &
cd ..
sleep 2

printf "\nStarting Repository ...\n"
cd dir_repositorySide/
./repositorySide_com_alt.sh $registyHostName $registyPortNum $repositoryPortNum $nCustomer $nCraftsman $initialGoodsInDisplay $numberOfPrimeMaterialsInWorkshop $logFileName &
cd ..
sleep 2

printf "\nStarting Storage ...\n"
cd dir_storageSide/
./storageSide_com_alt.sh $registyHostName $registyPortNum $storagePortNum $initialNumberOfPrimeMaterialsInStorage $numberOfPrimeMaterialsForRestock &
cd ..
sleep 2

printf "\nStarting WorkShop ...\n"
cd dir_workshopSide/
./workshopSide_com_alt.sh $registyHostName $registyPortNum $workshopPortNum $numberOfPrimeMaterialsByProduct $limitOfProducts $nCraftsman $nEntities &
cd ..
sleep 2

printf "\nStarting Shop ...\n"
cd dir_shopSide/
./shopSide_com_alt.sh $registyHostName $registyPortNum $shopPortNum $nCraftsman $nCustomer &
cd ..
sleep 2

printf "\nStarting Entrepreneur ...\n"
cd dir_entrepreneurSide/
./entrepreneurSide_com_alt.sh $registyHostName $registyPortNum $nEntities &
cd ..
sleep 2

printf "\nStarting Craftsmen ...\n"
cd dir_craftsmenSide/
./craftsmenSide_com_alt.sh $registyHostName $registyPortNum $nCraftsman $nCustomer &
cd ..
sleep 2

printf "\nStarting Customers ...\n"
cd dir_customersSide/
./customersSide_com_alt.sh $registyHostName $registyPortNum $nEntities &
cd ..
sleep 2

printf "\nRunning ...\n\n"