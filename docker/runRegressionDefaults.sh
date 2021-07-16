#!/usr/bin/env bash

regressionMySqlContainerName="regressionMySQL"
mySqlConfigTemplate="../conf/mysql.site.conf"
regressionMySqlPort="3330"
regressionMySQLConfig="regressionMySQL.cnf"

versionsToDeploy="env/sprint"

vitalpathContainerName="regressionVitalPath"
vitalpathImageVersion="1.0.0"
vitalpathImageSnapshot="-TEST"

ADMUPDATE_OPTS='-f HH_DRUGDATA,HH_FULL,HH1_DEV'
deployDir="deploy"
modelsDir="models"

testToRun="chromeTest"
