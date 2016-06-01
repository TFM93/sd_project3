printf "Setting RMI registry ...\n"
./set_rmiregistry_alt.sh $2 &
sleep 2

printf "\nStarting Service Register ...\n"
cd dir_registry/
./registry_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Repository ...\n"
cd dir_repositorySide/
./repositorySide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Storage ...\n"
cd dir_storageSide/
./storageSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting WorkShop ...\n"
cd dir_workshopSide/
./workshopSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Shop ...\n"
cd dir_shopSide/
./shopSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Entrepreneur ...\n"
cd dir_entrepreneurSide/
./entrepreneurSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Craftsmen ...\n"
cd dir_craftsmenSide/
./craftsmenSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nStarting Customers ...\n"
cd dir_customersSide/
./customersSide_com_alt.sh $1 $2 &
cd ..
sleep 2

printf "\nRunning ...\n\n"