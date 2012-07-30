#!/bin/bash

VERSION=`mvn help:evaluate -Dexpression=project.version | grep "^[0-9]" | sed -e 's/\-SNAPSHOT//'`
RELEASE=${VERSION}-${BUILD_NUMBER}
echo Release ${RELEASE}
echo VERSION=${VERSION} > build.properties
echo RELEASE=${RELEASE} >> build.properties

