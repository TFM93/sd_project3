# Compile
echo "Compiling..."
javac interfaces/*.java
javac info/*.java
javac registry/*.java
javac storageSide/*.java
javac repositorySide/*.java
javac shopSide/*.java
javac workshopSide/*.java
javac customersSide/*.java
javac entrepreneurSide/*.java
javac craftsmenSide/*.java

# Deploy classes in each side
echo "Deploying..."
cp interfaces/Register.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/

cp interfaces/*.class dir_storageSide/interfaces/
cp storageSide/*.class dir_storageSide/storageSide/

cp interfaces/*.class dir_repositorySide/interfaces/
cp repositorySide/*.class dir_repositorySide/repositorySide/

cp interfaces/*.class dir_shopSide/interfaces/
cp info/*.class dir_shopSide/info/
cp shopSide/*.class dir_shopSide/shopSide/

cp interfaces/*.class dir_workshopSide/interfaces/
cp info/*.class dir_workshopSide/info/
cp workshopSide/*.class dir_workshopSide/workshopSide/

cp interfaces/ShopInterface.class dir_customersSide/interfaces/
cp info/*.class dir_customersSide/info/
cp customersSide/*.class dir_customersSide/customersSide/

cp interfaces/ShopInterface.class interfaces/WorkShopInterface.class interfaces/StorageInterface.class dir_entrepreneurSide/interfaces/
cp info/*.class dir_entrepreneurSide/info/
cp entrepreneurSide/*.class dir_entrepreneurSide/entrepreneurSide/

cp interfaces/ShopInterface.class interfaces/WorkShopInterface.class dir_craftsmenSide/interfaces/
cp info/*.class dir_craftsmenSide/info/
cp craftsmenSide/*.class dir_craftsmenSide/craftsmenSide/

echo "Done!"