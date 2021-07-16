#!/usr/bin/env bash

for ARGUMENT in "$@"
do

   KEY=$(echo $ARGUMENT | cut -f1 -d=)
   VALUE=$(echo $ARGUMENT | cut -f2 -d=)

   case "$KEY" in
            module)               module=${VALUE} ;;
            browser)              browser=${VALUE} ;;
            seleniumRemoteServer) seleniumRemoteServer=${VALUE} ;;
            profile)              profile=${VALUE} ;;
            *)
    esac

done

betaUrl="$seleniumRemoteServer:4443/wd/hub" # Beta
currentUrl="$seleniumRemoteServer:4444/wd/hub" # Current
previousUrl="$seleniumRemoteServer:4445/wd/hub" # Previous

echo "module = $module"
echo "browser = $browser"
echo "seleniumRemoteServer = $seleniumRemoteServer"
echo "profile = $profile"
echo "betaUrl = $betaUrl"
echo "currentUrl = $currentUrl"
echo "previousUrl = $previousUrl"

if [ $profile = "dev" ]
then
    subdomain="admdevsite1"
elif [ $profile = "qa" ]
then
    subdomain="admqasite1"
elif [ $profile = "tst" ]
then
    subdomain="admtstsite1"
elif [ $profile = "stg" ]
then
    subdomain="admstgsite1"
else
    echo "Not a Valid Profile"
    exit 1
fi

appUrl="https://$subdomain.cardinalhealth.net/latest/$module/"
servicesUrl="https://$subdomain.cardinalhealth.net/services/"

if [ $module = "cabinet" ]
then
    browserWidth=640
    browserHeight=600
elif [ $module = "client" ]
then
    browserWidth=1024
    browserHeight=768
else
    echo "Not a Valid Module"
    exit 1
fi

rm -rf ~/reports/$profile-$module-beta
rm -rf ~/reports/$profile-$module-current
rm -rf ~/reports/$profile-$module-previous

echo "----- Setting up data -----"
./gradlew -DservicesUrl=$servicesUrl :common:setUpSuiteData

#echo "----- Starting Beta Tests -----"
#./gradlew -Dbrowser=$browser -DbrowserVersion="Chrome Beta" -DseleniumRemoteUrl=$betaUrl -DbrowserWidth=$browserWidth -DbrowserHeight=$browserHeight -DappUrl=$appUrl -DservicesUrl=$servicesUrl -DreportDir="$module-beta-reports" :$module:test &
#BETA_PID=$!

echo "----- Starting Current Tests -----"
./gradlew -Dbrowser=$browser -DbrowserVersion="Chrome Current" -DseleniumRemoteUrl=$currentUrl -DbrowserWidth=$browserWidth -DbrowserHeight=$browserHeight -DappUrl=$appUrl -DservicesUrl=$servicesUrl -DreportDir="$module-current-reports" :$module:test &
CURRENT_PID=$!

#echo "----- Starting Previous Tests -----"
#./gradlew -Dbrowser=$browser -DbrowserVersion="Chrome Previous" -DseleniumRemoteUrl=$previousUrl -DbrowserWidth=$browserWidth -DbrowserHeight=$browserHeight -DappUrl=$appUrl -DservicesUrl=$servicesUrl -DreportDir="$module-previous-reports" :$module:test &
#PREVIOUS_PID=$!

#wait $BETA_PID $CURRENT_PID $PREVIOUS_PID
wait $CURRENT_PID

#mkdir ~/reports/$profile-$module-beta
mkdir ~/reports/$profile-$module-current
#mkdir ~/reports/$profile-$module-previous

#mv ./$module/$module-beta-reports/*.* ~/reports/$profile-$module-beta
mv ./$module/$module-current-reports/*.* ~/reports/$profile-$module-current
#mv ./$module/$module-previous-reports/*.* ~/reports/$profile-$module-previous

echo "Done!"