# Compile
echo "Compiling..."
javac pt/ua/sd/RopeGame/interfaces/*.java
javac pt/ua/sd/RopeGame/info/*.java
javac pt/ua/sd/RopeGame/registry/*.java
javac pt/ua/sd/RopeGame/active_entities/ContestantSide/*.java
javac pt/ua/sd/RopeGame/active_entities/RefereeSide/*.java
javac pt/ua/sd/RopeGame/active_entities/CoachSide/*.java
javac pt/ua/sd/RopeGame/shared_mem/BenchSide/*.java
javac pt/ua/sd/RopeGame/shared_mem/PlaygroundSide/*.java
javac pt/ua/sd/RopeGame/shared_mem/RefSiteSide/*.java
javac pt/ua/sd/RopeGame/shared_mem/RepoSide/*.java
javac pt/ua/sd/RopeGame/enums/*.java
javac pt/ua/sd/RopeGame/structures/*java


# Deploy classes in each side
echo "Deploying1..."
cp pt/ua/sd/RopeGame/interfaces/Register.class BuiltToRun/dir_registry/pt/ua/sd/RopeGame/interfaces/
cp pt/ua/sd/RopeGame/registry/*.class BuiltToRun/dir_registry/pt/ua/sd/RopeGame/registry/
echo "Deploying2..."
cp pt/ua/sd/RopeGame/interfaces/*.class BuiltToRun/dir_ContestantSide/pt/ua/sd/RopeGame/interfaces/
cp pt/ua/sd/RopeGame/active_entities/ContestantSide/*.class BuiltToRun/dir_ContestantSide/pt/ua/sd/RopeGame/active_entities/ContestantSide/
echo "Deploying3..."
cp pt/ua/sd/RopeGame/interfaces/*.class BuiltToRun/dir_CoachSide/pt/ua/sd/RopeGame/interfaces/
cp pt/ua/sd/RopeGame/active_entities/CoachSide/*.class BuiltToRun/dir_CoachSide/pt/ua/sd/RopeGame/active_entities/CoachSide/
echo "Deploying4..."
cp pt/ua/sd/RopeGame/interfaces/*.class BuiltToRun/dir_RefereeSide/pt/ua/sd/RopeGame/interfaces/
cp pt/ua/sd/RopeGame/active_entities/RefereeSide/*.class BuiltToRun/dir_RefereeSide/pt/ua/sd/RopeGame/active_entities/RefereeSide/
echo "Deploying5..."
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_RefereeSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_CoachSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_ContestantSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_BenchSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_PlaygroundSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/info/
echo "Deploying6..."

cp pt/ua/sd/RopeGame/structures/*.class BuiltToRun/dir_RefereeSide/pt/ua/sd/RopeGame/structures/
cp pt/ua/sd/RopeGame/structures/*.class BuiltToRun/dir_CoachSide/pt/ua/sd/RopeGame/structures/
cp pt/ua/sd/RopeGame/structures/*.class BuiltToRun/dir_ContestantSide/pt/ua/sd/RopeGame/structures/
cp pt/ua/sd/RopeGame/structures/*.class BuiltToRun/dir_BenchSide/pt/ua/sd/RopeGame/structures/
cp pt/ua/sd/RopeGame/structures/*.class BuiltToRun/dir_PlaygroundSide/pt/ua/sd/RopeGame/structures/
cp pt/ua/sd/RopeGame/structures/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/structures/

cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefereeSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_CoachSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_ContestantSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_BenchSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_PlaygroundSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RepoSide/pt/ua/sd/RopeGame/enums/


echo "Deploying7..."
cp pt/ua/sd/RopeGame/interfaces/RepoInterface.class pt/ua/sd/RopeGame/interfaces/Register.class pt/ua/sd/RopeGame/interfaces/PlaygroundInterface.class pt/ua/sd/RopeGame/interfaces/RefereeSiteInterface.class pt/ua/sd/RopeGame/interfaces/BenchInterface.class BuiltToRun/dir_RepoSide/pt/ua/sd/RopeGame/interfaces/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_RepoSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/shared_mem/RepoSide/*.class BuiltToRun/dir_RepoSide/pt/ua/sd/RopeGame/shared_mem/RepoSide/
echo "Deploying8..."
cp pt/ua/sd/RopeGame/interfaces/RepoInterface.class pt/ua/sd/RopeGame/interfaces/Register.class pt/ua/sd/RopeGame/interfaces/PlaygroundInterface.class pt/ua/sd/RopeGame/interfaces/RefereeSiteInterface.class pt/ua/sd/RopeGame/interfaces/BenchInterface.class BuiltToRun/dir_BenchSide/pt/ua/sd/RopeGame/interfaces/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_BenchSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/shared_mem/BenchSide/*.class BuiltToRun/dir_BenchSide/pt/ua/sd/RopeGame/shared_mem/BenchSide/
echo "Deploying9..."
cp pt/ua/sd/RopeGame/interfaces/RepoInterface.class pt/ua/sd/RopeGame/interfaces/Register.class pt/ua/sd/RopeGame/interfaces/PlaygroundInterface.class pt/ua/sd/RopeGame/interfaces/RefereeSiteInterface.class pt/ua/sd/RopeGame/interfaces/BenchInterface.class BuiltToRun/dir_PlaygroundSide/pt/ua/sd/RopeGame/interfaces/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_PlaygroundSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/shared_mem/PlaygroundSide/*.class BuiltToRun/dir_PlaygroundSide/pt/ua/sd/RopeGame/shared_mem/PlaygroundSide/
echo "Deploying10..."
cp pt/ua/sd/RopeGame/interfaces/RepoInterface.class pt/ua/sd/RopeGame/interfaces/Register.class pt/ua/sd/RopeGame/interfaces/PlaygroundInterface.class pt/ua/sd/RopeGame/interfaces/RefereeSiteInterface.class pt/ua/sd/RopeGame/interfaces/BenchInterface.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/interfaces/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/info/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/enums/
cp pt/ua/sd/RopeGame/shared_mem/RefSiteSide/*.class BuiltToRun/dir_RefSiteSide/pt/ua/sd/RopeGame/shared_mem/RefSiteSide/

echo "Done!"