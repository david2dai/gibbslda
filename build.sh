#!/bin/bash

clear

mvn clean
mvn package

base_dir=$(dirname $0)

cp  $base_dir/target/*.jar  $base_dir/lib/

exit 0
