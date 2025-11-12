#!/bin/bash
cd /home/kavia/workspace/code-generation/sportsnow-live-tv-41542-41552/sportsnow_tv_android_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

