#!/bin/bash

browsers=(chrome-beta chrome-stable chrome-prev)
selenium_ports=(4445 4444 4443)
vnc_ports=(6001 6000 5999)
chrome_paths=('/opt/google/chrome-beta' '/opt/google/chrome' '/opt/google/chrome')

forced=${1}

function swapStableToPrev() {
    image=${1}
	tag=${2}

    echo "Retagging ${image}:${tag} as chrome-prev:${tag}"
    docker tag ${image}:${tag} chrome-prev:${tag}

}

function getChromeVersion() {
    container=${1}
    echo $(docker run -t ${container} "google-chrome --version" | awk '{print $3}' | egrep -v '[a-zA-Z]')
}

function buildImage() {
    image=${1}
    browser=${image}
    selenium_port=${2}
    vnc_port=${3}
    chrome_path=${4}

	# Don't want to build prev from scratch if it doesn't exist. Instead, use the previously
	# build stable image with a different tag. Stable and prev will be the same image until
	# stable gets an update.
    if [[ ${image} == 'chrome-prev' ]]; then
        browser='chrome-stable'
	tag='latest'
		swapStableToPrev ${browser} ${tag}
	else
    	echo "Building ${image}..."
    	docker build --no-cache -t ${image} . --build-arg CHROME_PACKAGE=google-${browser} --build-arg SELENIUM_PORT=${selenium_port} --build-arg VNC_PORT=${vnc_port} --build-arg CHROME_PATH=${chrome_path}
	fi
}

echo "########################################"
echo "#      Chrome Management Script        #"
echo "########################################"
echo "Runtime: " $(date +"%m-%d-%Y %r")
echo

# Check for existing images. Will only build missing images, doesn't care about tags at this point.
count=0
echo "Checking for existing images and building missing ones..."
for browser in ${browsers[@]}; do
    selenium_port=${selenium_ports[count]}
    vnc_port=${vnc_ports[count]}
    chrome_path=${chrome_paths[count]}

    if [[ -z $(docker images | grep ${browser}) ]] || [[ ! -z ${forced} ]]; then
        buildImage ${browser} ${selenium_port} ${vnc_port} ${chrome_path}
    else
        echo "${browser} already has an image."
    fi

    (( count++ ))
done
echo '-----'

# Check for updates to browsers and rebuild image if there are
count=0
echo "Checking if any images have updates to their chrome version..."
for browser in ${browsers[@]}; do
    selenium_port=${selenium_ports[count]}
    vnc_port=${vnc_ports[count]}
    chrome_path=${chrome_paths[count]}

    # chrome-prev will become the old stable image. We don't care about updates to this browser.
    if [[ ${browser} == "chrome-prev" ]]; then
        continue
    fi

	# Check for any tags that are not "latest". If there are none then that means we have never tagged
	# this image with a chrome version before, meaning these are fresh images with the latest chrome.
    image_tag=$(docker images ${browser} --format "{{.Tag}}" | grep -v latest)
    if [[ -z ${image_tag} ]]; then
        echo "There are no version-tagged images for ${browser}. Skipping update check..."
		continue
    fi

	# Runs the apt package manager and checks for updates to chrome. If there are updates then build it.
	# This step also retags stable to prev if stable has an update.
	echo -n "Checking for updates to ${browser}:${image_tag}..."
    chrome_update=$(docker run -t -u root ${browser}:${image_tag} "apt-get update && apt-get -u upgrade --assume-no | grep ${browser}")
    if [[ -z ${chrome_update} ]]; then
        echo "already up-to-date"
    elif [[ ${browser} == 'chrome-stable' ]]; then
        swapStableToPrev ${browser} ${image_tag}
        buildImage ${browser} ${selenium_port} ${vnc_port} ${chrome_path}
    else
        buildImage ${browser} ${selenium_port} ${vnc_port} ${chrome_path}
    fi

    (( count++ ))
done
echo '-----'

# Check if there is a latest tag out there for the browser. If not, get the current chrome version for the
# latest image and tag it with that version. Remove the latest tag.
echo "Tagging any latest images with their chrome version and removing latest..."
for browser in ${browsers[@]}; do
    latest_tag=$(docker images ${browser} --format "{{.Tag}}" | grep latest)
    if [[ ! -z ${latest_tag} ]]; then
        chrome_version=$(getChromeVersion ${browser})
		echo "Tagging ${browser}:${latest_tag} as ${browser}:${chrome_version}"
        docker tag ${browser} ${browser}:${chrome_version}
        docker rmi ${browser}:${latest_tag}
    fi
done

echo
echo "End time: " $(date +"%m-%d-%Y %r")
echo "########################################"
