#!/bin/bash

# This script must be executed after the build is complete and successful

# We don't care about Jenkins tags
for TAG in $(git tag -l jenkins-myconfig*) ; do git tag -d $TAG; done
# Tag
TAG=myconfig-${RELEASE}
# Tagging the build
git tag ${TAG}
# Pushing the result
git push origin ${TAG}

