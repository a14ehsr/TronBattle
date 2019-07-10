#!/bin/bash

cd java/src/
javac ac/a14ehsr/platform/visualizer/GameModeWindow.java

cd ../../

java -classpath java/src ac.a14ehsr.platform.visualizer.GameModeWindow
