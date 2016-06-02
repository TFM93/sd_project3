# Compile
echo "Compiling..."
javac interfaces/*.java
javac info/*.java
javac registry/*.java
javac ©*.java
javac active_entities/ContestantSide/*.java
javac active_entities/RefereeSide/*.java
javac shared_mem/BenchSide/*.java
javac shared_mem/PlaygroundSide/*.java
javac shared_mem/RefSiteSide/*.java
javac shared_mem/RepoSide/*.java
javac enums/*.java
javac structures/*java


# Deploy classes in each side
echo "Deploying..."
cp interfaces/Register.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/

cp interfaces/*.class dir_CoachSide/interfaces/
cp active_entities/ContestantSide/*.class dir_Contestantide/storageSide/

cp interfaces/*.class dir_repositorySide/interfaces/
cp active_entities/RepoSide/*.class dir_repositorySide/repositorySide/

cp interfaces/*.class dir_shopSide/interfaces/
cp info/*.class dir_shopSide/info/
cp shopSide/*.class dir_shopSide/shopSide/

cp interfaces/*.class dir_workshopSide/interfaces/
cp info/*.class dir_workshopSide/info/
cp workshopSide/*.class dir_workshopSide/workshopSide/

cp interfaces/ShopInterface.class dir_customersSide/interfaces/
cp info/*.class dir_customersSide/info/
cp customersSide/*.class dir_customersSide/customersSide/

cp interfaces/RepoInterface.class interfaces/Register.class interfaces/PlaygroundInterface.class interfaces/RefereeSiteInterface.class interfaces/BenchInterface.class dir_RepoSide/interfaces/
cp info/*.class dir_RepoSide/info/
cp shared_mem/RepoSide/*.class dir_RepoSide/RepoSide/

cp interfaces/RepoInterface.class interfaces/Register.class interfaces/PlaygroundInterface.class interfaces/RefereeSiteInterface.class interfaces/BenchInterface.class dir_BenchSide/interfaces/
cp info/*.class dir_BenchSide/info/
cp shared_mem/BenchSide/*.class dir_BenchSide/BenchSide/

cp interfaces/RepoInterface.class interfaces/Register.class interfaces/PlaygroundInterface.class interfaces/RefereeSiteInterface.class interfaces/BenchInterface.class dir_PlaygroundSide/interfaces/
cp info/*.class dir_PlaygroundSide/info/
cp shared_mem/PlaygroundSide/*.class dir_PlaygroundSide/PlaygroundSide/

cp interfaces/RepoInterface.class interfaces/Register.class interfaces/PlaygroundInterface.class interfaces/RefereeSiteInterface.class interfaces/BenchInterface.class dir_RefSiteSide/interfaces/
cp info/*.class dir_RefSiteSide/info/
cp shared_mem/RefSiteSide/*.class dir_RefSiteSide/RefSiteSide/

echo "Done!"