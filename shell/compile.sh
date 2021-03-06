#!/bin/bash

command=`cat resource/config/java/compile_command.txt`
options=`cat resource/config/java/compile_options.txt`
cd java/src

${command} ${options} ac/a14ehsr/player/PlayerProcess.java
${command} ${options} ac/a14ehsr/platform/GamePlatform.java
${command} ${options} ac/a14ehsr/sample_ai/Ai_Clockwise.java
${command} ${options} ac/a14ehsr/sample_ai/Ai_Random.java
${command} ${options} ac/a14ehsr/sample_ai/Ai_Straight.java
#${command} ${options} ac/a14ehsr/sample_ai/P_Max.java
#${command} ${options} ac/a14ehsr/sample_ai/P_4Neighbours.java
#${command} ${options} ac/a14ehsr/sample_ai/P_8Neighbours.java
#${command} ${options} ac/a14ehsr/sample_ai/P_Chaise.java
#${command} ${options} ac/a14ehsr/sample_ai/P_Copy.java

cd ../../