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
echo "Deploying..."
cp pt/ua/sd/RopeGame/interfaces/Register.class BuiltToRun/dir_registry/interfaces/
cp pt/ua/sd/RopeGame/registry/*.class BuiltToRun/dir_registry/registry/

cp pt/ua/sd/RopeGame/interfaces/*.class BuiltToRun/dir_ContestantSide/interfaces/
cp pt/ua/sd/RopeGame/active_entities/ContestantSide/*.class BuiltToRun/dir_ContestantSide/ContestantSide/

cp pt/ua/sd/RopeGame/interfaces/*.class BuiltToRun/dir_CoachSide/interfaces/
cp pt/ua/sd/RopeGame/active_entities/CoachSide/*.class BuiltToRun/dir_CoachSide/CoachSide/

cp pt/ua/sd/RopeGame/interfaces/*.class BuiltToRun/dir_RefereeSide/interfaces/
cp pt/ua/sd/RopeGame/active_entities/RefereeSide/*.class BuiltToRun/dir_RefereeSide/RefereeSide/

cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_RefereeSide/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_CoachSide/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_ContestantSide/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_BenchSide/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_PlaygroundSide/info/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_RefSiteSide/info/


cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefereeSide/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_CoachSide/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_ContestantSide/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_BenchSide/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_PlaygroundSide/enums/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/enums/


cp pt/ua/sd/RopeGame/interfaces/RepoInterface.class pt/ua/sd/RopeGame/interfaces/Register.class pt/ua/sd/RopeGame/interfaces/PlaygroundInterface.class pt/ua/sd/RopeGame/interfaces/RefereeSiteInterface.class pt/ua/sd/RopeGame/interfaces/BenchInterface.class BuiltToRun/dir_RepoSide/interfaces/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_RepoSide/info/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/enums/
cp pt/ua/sd/RopeGame/shared_mem/RepoSide/*.class BuiltToRun/dir_RepoSide/RepoSide/

cp pt/ua/sd/RopeGame/interfaces/RepoInterface.class pt/ua/sd/RopeGame/interfaces/Register.class pt/ua/sd/RopeGame/interfaces/PlaygroundInterface.class pt/ua/sd/RopeGame/interfaces/RefereeSiteInterface.class pt/ua/sd/RopeGame/interfaces/BenchInterface.class BuiltToRun/dir_BenchSide/interfaces/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_BenchSide/info/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/enums/
cp pt/ua/sd/RopeGame/shared_mem/BenchSide/*.class BuiltToRun/dir_BenchSide/BenchSide/

cp pt/ua/sd/RopeGame/interfaces/RepoInterface.class pt/ua/sd/RopeGame/interfaces/Register.class pt/ua/sd/RopeGame/interfaces/PlaygroundInterface.class pt/ua/sd/RopeGame/interfaces/RefereeSiteInterface.class pt/ua/sd/RopeGame/interfaces/BenchInterface.class BuiltToRun/dir_PlaygroundSide/interfaces/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_PlaygroundSide/info/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/enums/
cp pt/ua/sd/RopeGame/shared_mem/PlaygroundSide/*.class BuiltToRun/dir_PlaygroundSide/PlaygroundSide/

cp pt/ua/sd/RopeGame/interfaces/RepoInterface.class pt/ua/sd/RopeGame/interfaces/Register.class pt/ua/sd/RopeGame/interfaces/PlaygroundInterface.class pt/ua/sd/RopeGame/interfaces/RefereeSiteInterface.class pt/ua/sd/RopeGame/interfaces/BenchInterface.class BuiltToRun/dir_RefSiteSide/interfaces/
cp pt/ua/sd/RopeGame/info/*.class BuiltToRun/dir_RefSiteSide/info/
cp pt/ua/sd/RopeGame/enums/*.class BuiltToRun/dir_RefSiteSide/enums/
cp pt/ua/sd/RopeGame/shared_mem/RefSiteSide/*.class BuiltToRun/dir_RefSiteSide/RefSiteSide/

echo "Done!"