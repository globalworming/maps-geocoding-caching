#!/usr/bin/env bash

set -e


cd `dirname $0`
cd "`pwd`/.."
PROJECT_ROOT=`pwd`
FE_ROOT=${PROJECT_ROOT}/map-pins

# check login
az account show > /dev/null

yarn --cwd ${FE_ROOT} build
rm -rf ${PROJECT_ROOT}/src/main/resources/static
cp -R ${FE_ROOT}/build ${PROJECT_ROOT}/src/main/resources
mv ${PROJECT_ROOT}/src/main/resources/build ${PROJECT_ROOT}/src/main/resources/static

mvn clean package azure-webapp:deploy