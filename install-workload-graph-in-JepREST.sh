#!/bin/bash

# Compile the project
mvn clean package

jepRestPath=$1 # path/to/JepREST directory, e.g., /home/user/JepREST
projectName=$2 # name of the app being tested, e.g., store, petstore, usersManagement, etc.

# Copy the jar file to the JepREST directory, to be able to use the generated graph
cp target/WorkloadGenerator-1.0.jar $jepRestPath/JepRest

# Generate the workload graph
java -cp target/WorkloadGenerator-1.0.jar Main $jepRestPath $projectName

# Copy the workload graph to the JepREST directory
cp workload-graph.dat $jepRestPath/JepRest/clojure-code
