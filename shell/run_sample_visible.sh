#!/bin/bash

command=`cat resource/setting/java/run_command.txt`
options=`cat resource/setting/java/run_options.txt`

${command} ${options} -classpath java/src ac.a14ehsr.platform.GamePlatform -sample -olevel 2 -v
#${command} ${options} -classpath java/src ac.a14ehsr.platform.TronGame -p "$1" -sample true -v true