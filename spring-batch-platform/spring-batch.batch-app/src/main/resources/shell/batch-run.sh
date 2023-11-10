#!/bin/bash

echo "Hello, spring-batch.batch-app!"

cd ../../../../target || exit
echo "The current path is $PWD."

ls -rtl

SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-default}

if [ -z "$SPRING_PROFILES_ACTIVE" ]; then
    echo "Spring Profiles Active value is unknown."
    exit 1;
fi;

echo "Spring Profiles Active is $SPRING_PROFILES_ACTIVE."

JVM_HEAP_SIZE="-Xms256m -Xmx1024m"
BATCH_PRSNT_JAR=($(/usr/bin/find . -maxdepth 1 -type f -name "*.jar" -printf '%T@ %p\n' | sort -k 1nr | sed 's/^[^ ]* //' | head -n 2 | awk -F '/' '{print $2}'))
BATCH_JAR=${BATCH_PRSNT_JAR[0]}

java -jar "$JVM_HEAP_SIZE" "$BATCH_JAR" \
      $@ \
      --Dspring.profiles.active="$SPRING_PROFILES_ACTIVE"

STATUS=$?

echo "status : $STATUS"
if [ "$STATUS" -ne "0" ]; then
  echo "Batch execution failed."
  exit 1;
else
  echo "Batch execution completed."
  exit 0;
fi;