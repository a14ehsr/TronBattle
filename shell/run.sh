#!/bin/bash

command=`cat resource/config/java/run_command.txt`
options=`cat resource/config/java/run_options.txt`
if [ $1 -eq 2 ]; then
  ${command} ${options} -classpath java/src ac.a14ehsr.platform.GamePlatform -p "$2" -p "$3"
fi

if [ $1 -eq 3 ]; then
  ${command} ${options} -classpath java/src ac.a14ehsr.platform.GamePlatform -p "$2" -p "$3" -p "$4" -nop 3
fi

if [ $1 -eq 4 ]; then
  ${command} ${options} -classpath java/src ac.a14ehsr.platform.GamePlatform -p "$2" -p "$3" -p "$4" -p "$5" -nop 4
fi
