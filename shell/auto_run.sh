#!/bin/bash
if [ $# -ne 1 ]; then
  echo "The specified number of arguments is $#." 1>&2
  echo "Please enter only the number of players as an argument." 1>&2
  exit 1
fi

command=`cat resource/config/java/run_command.txt`
options=`cat resource/config/java/run_options.txt`

${command} ${options} -classpath java/src ac.a14ehsr.platform.GamePlatform -auto true -nop $1

