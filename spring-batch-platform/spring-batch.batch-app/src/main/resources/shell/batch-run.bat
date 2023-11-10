@echo OFF

echo Hello, spring-batch.batch-app!

cd ../../../../target
echo The current path is %cd%.

set SPRING_PROFILES_ACTIVE=default
echo Spring Profiles Active is %SPRING_PROFILES_ACTIVE%.

set JVM_HEAP_SIZE=-Xms256m -Xmx1024m

for /f %%f in ('dir /b/a-d/od/t:c') do (
    set BATCH_JAR=%%f
)

java -jar %JVM_HEAP_SIZE% %BATCH_JAR% %* --Dspring.profiles.active=%SPRING_PROFILES_ACTIVE%

set STATUS=%ERRORLEVEL%

echo status : %STATUS%
if %STATUS% neq 0 (
    echo Batch execution failed.
    exit 1
) else (
    echo Batch execution completed.
    exit 0
)