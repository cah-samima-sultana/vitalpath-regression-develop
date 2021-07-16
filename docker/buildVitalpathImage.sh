#!/usr/bin/env bash

# must be in the vitalpath-releases directory
# $1 = directory root where the artifacts were deployed
# $2 = version number for the image
# $3 = snapshot text of the image

echo "Creating the Image from the downloaded artifacts"
# Assumes vitalpath-releases exists in the current directory
cd vitalpath-releases
./gradlew buildImageSite -PdestinationRootDir=../"${1}" -PversionNumber=$2 -Psnapshot=$3

cd ..
