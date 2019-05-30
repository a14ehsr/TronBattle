#!/bin/bash

command=`cat resource/config/java/run_command.txt`
options=`cat resource/config/java/run_options.txt`
${command} ${options} -classpath java/src ac.a14ehsr.platform.GamePlatform -p "$1" -sample