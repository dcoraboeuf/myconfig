#!/bin/bash

# Pre-requisites:
# BUILD_NUMBER environment variable must be accessible
# mvn must be accessible and correctly configured

##########################
# Preparation of the build
##########################

# Gets the version number from the POM
VERSION=`mvn help:evaluate -Dexpression=project.version | grep "^[0-9]" | sed -e 's/\-SNAPSHOT//'`

# Release number is made of the version and the build number
RELEASE=${VERSION}-${BUILD_NUMBER}

# Logging the whole
echo Building release ${RELEASE}...

#####################################################
# Runs the build itself and as much tests as possible
#####################################################

# Build options
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128m"

# Maven build
mvn clean install -P acceptance-test

# Creates the TAR that contains:
# 1. the WAR
# 2. all the deployment files
tar --create --gzip --file=target/myconfig-${RELEASE}.tar --directory=myconfig-web/target myconfig-web-${RELEASE}.war

##################################################################
# After the build is complete, create the tag in Git and pushes it
##################################################################

# We don't care about Jenkins tags
for TAG in $(git tag -l jenkins-myconfig*) ; do
	git tag -d $TAG
done

# Tag
TAG=myconfig-${RELEASE}
# Tagging the build
git tag ${TAG}
# Pushing the result
git push origin ${TAG}
