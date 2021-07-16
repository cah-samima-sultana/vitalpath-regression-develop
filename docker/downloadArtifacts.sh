#!/usr/bin/env bash

# be in the directory you want the clone to be placed in

# $1 VersionsToDeploy
# $2 directory to deploy to
# $3 directory to place models

releaseRepo="vitalpath-releases"

if [ -d "${releaseRepo}" ]; then
    echo "Updating Release Repo"
    cd "${releaseRepo}"
    git pull
  else
    echo "Cloning the Release repo"
    git clone "https://github.com/cahcommercial/${releaseRepo}.git"
    cd "${releaseRepo}"
fi

# Download the needed artifacts
docker/downloadArtifacts.sh "$1" "../${2}"

docker/extractModels.sh "../${2}" "$3"

cd ..

