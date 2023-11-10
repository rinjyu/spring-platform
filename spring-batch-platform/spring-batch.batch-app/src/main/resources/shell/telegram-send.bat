@echo OFF

set BOT_TOKEN=%1%
set CHAT_ID=%2%
set JOB_NAME=%3%
set BUILD_NUMBER=%4%
set NOTIFICATION_MESSAGE=%5%

if "%BOT_TOKEN%" == "" (
    echo BOT_TOKEN is unknown.
    exit 1
)

if "%CHAT_ID%" == "" (
  echo CHAT_ID is unknown.
  exit 1
)

if "%JOB_NAME%" == "" (
    echo JOB_NAME is unknown.
    exit 1
)

if "%BUILD_NUMBER%" == "" (
    echo BUILD_NUMBER is unknown.
    exit 1
)

if "%NOTIFICATION_MESSAGE%" == "" (
    echo NOTIFICATION_MESSAGE is unknown.
    exit 1
)

set TELEGRAM_MESSAGE=[\\(LOCAL\\) %JOB_NAME% \\#\\%BUILD_NUMBER%]\n%NOTIFICATION_MESSAGE%

curl -X POST https://api.telegram.org/bot%BOT_TOKEN%/sendMessage ^
        -H "Content-Type: application/json" ^
        -d "{\"chat_id\": \"%CHAT_ID%\", \"parse_mode\": \"MarkdownV2\", \"disable_web_page_preview\": false, \"text\": \"%TELEGRAM_MESSAGE%\"}"

set STATUS=%ERRORLEVEL%

echo status : %STATUS%
if %STATUS% neq 0 (
    echo Failed to send notification.
    exit 1
) else (
    echo Successful sending of notifications.
    exit 0
)