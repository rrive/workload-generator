#!/bin/bash

mvn clean package

# default path = ../JepREST/JepRest Todo: make it variable
cp target/WorkloadGenerator-1.0.jar ../JepREST/JepRest

# default input = ../JepREST sd Todo: make it variable
java -cp target/WorkloadGenerator-1.0.jar Main ../JepREST sd

# default path = ../JepREST/JepRest/clojure-code Todo: make it variable
cp workload-graph.dat ../JepREST/JepRest/clojure-code
