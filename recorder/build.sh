#!/usr/bin/env bash

mvn clean compile assembly:single
cp target/recorder-*-jar-with-dependencies.jar recorder.jar
chmod +x recorder.jar