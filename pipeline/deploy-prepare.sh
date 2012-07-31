#!/bin/bash


# Following environment variables are required
# VERSION			Version number (1.0.1)
# BUILD				Build number for this version (12)
# NEXUS_URL			Nexus URL to download artifacts from

check_env()
{
	if [ "${!1}" == "" ]
	then
		echo Variable $1 must be defined.
		exit 1
	fi
}

check_env "VERSION"
check_env "BUILD"
check_env "NEXUS_URL"

# Preparation
RELEASE=${VERSION}-${BUILD}

# Download
wget --quiet ${NEXUS_URL}/net/myconfig/myconfig/${RELEASE}/myconfig-${RELEASE}.tar

# Extraction
tar xvf myconfig-${RELEASE}.tar
