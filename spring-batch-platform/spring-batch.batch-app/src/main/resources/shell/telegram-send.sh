#!/bin/bash

BOT_TOKEN=${1}
CHAT_ID=${2}
JOB_NAME=${3}
BUILD_NUMBER=${4}
NOTIFICATION_MESSAGE=${5}

if [ -z "${BOT_TOKEN}" ]; then
    echo "BOT_TOKEN is unknown."
    exit 1;
fi;

if [ -z "${CHAT_ID}" ]; then
  echo "CHAT_ID is unknown."
  exit 1;
fi;

if [ -z "${JOB_NAME}" ]; then
    echo "JOB_NAME is unknown."
    exit 1;
fi;

if [ -z "${BUILD_NUMBER}" ]; then
    echo "BUILD_NUMBER is unknown."
    exit 1;
fi;

if [ -z "${NOTIFICATION_MESSAGE}" ]; then
    echo "NOTIFICATION_MESSAGE is unknown."
    exit 1;
fi;

if [ "${#}" -ne 5 ]; then
    echo "Please enclose blank characters in quotation marks."
    exit 1;
fi;

TELEGRAM_MESSAGE="\\\[\\\(변경필요\\\) ${JOB_NAME} \\\#${BUILD_NUMBER}\\\]"$'\n'"${NOTIFICATION_MESSAGE}"
echo "${TELEGRAM_MESSAGE}"

curl -X POST https://api.telegram.org/bot"${BOT_TOKEN}"/sendMessage \
                -H "Content-Type: application/json" \
                -d '{"chat_id": "'"${CHAT_ID}"'", "parse_mode": "MarkdownV2", "disable_web_page_preview": false, "text": "'"${TELEGRAM_MESSAGE}"'"'

STATUS=$?

echo "status : ${STATUS}"
if [ "${STATUS}" -ne "0" ]; then
  echo "Failed to send notification."
  exit 1;
else
  echo "Successful sending of notifications."
  exit 0;
fi;