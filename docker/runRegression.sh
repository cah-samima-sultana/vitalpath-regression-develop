#!/usr/bin/env bash

mkdir -p build
cd build

# Clone the vitalpath releases and download all the artifacts
../docker/downloadArtifacts.sh "${versionsToDeploy}" "${deployDir}" "${modelsDir}"

# Start the MySql Container
echo "Starting the regression mySqlServer"
vitalpath-releases/docker/startMySqlContainer.sh "${regressionMySqlContainerName}" "${regressionMySqlPort}" "5.6"

# Build the docker image for the application
../docker/buildVitalpathImage.sh "${deployDir}" "${vitalpathImageVersion}" "${vitalpathImageSnapshot}"

echo "Creating SQL config file"
eval "cat <<EOF
$(<"${mySqlConfigTemplate}")
EOF
" > "${regressionMySQLConfig}"

cat "${regressionMySQLConfig}"

# Reset the Database in the mySql container (note this is the script sin vitalpath-releases)
vitalpath-releases/docker/resetDataBase.sh "${deployDir}/lib" "./${regressionMySQLConfig}" "${modelsDir}/vitalpath-models*.jar" "${ADMUPDATE_OPTS}" "force"

rm "${regressionMySQLConfig}"

# Start the vitalpath application container
vitalpath-releases/docker/startVitalpathContainer.sh "${vitalpathContainerName}" "${vitalpathImageVersion}${vitalpathImageSnapshot}" "4200" "${regressionMySqlContainerName}"

# Reset back to the correct directory
cd ..

echo "Waiting for App Server to spin up"
sleep 10

# Run the tests
./gradlew "${testToRun}"

# Remove the application Container
build/vitalpath-releases/docker/stopRemoveContainer.sh "MySql" "${vitalpathContainerName}" "true"

# Remove the mySql Container
build/vitalpath-releases/docker/stopRemoveContainer.sh "Vitalpath App" "${regressionMySqlContainerName}" "true"


