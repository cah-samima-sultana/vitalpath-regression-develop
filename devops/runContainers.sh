#!/bin/bash

browsers=(chrome-beta chrome-stable chrome-prev)
selenium_ports=(4445 4444 4443)
vnc_ports=(6001 6000 5999)
chrome_paths=('/opt/google/chrome-beta' '/opt/google/chrome' '/opt/google/chrome')

# Starts up a container based on the provided image:tag, selenium port, and vnc port.
function startContainer() {
    container=${1}
    selenium_port=${2}
    vnc_port=${3}

    echo -n "Starting ${container} on ports ${selenium_port} and ${vnc_port}..."
    docker run -p ${selenium_port}:${selenium_port} -p ${vnc_port}:${vnc_port} -dti ${container} /bin/bash > /dev/null
	echo "done"
}

# Kills and removes any container passed to it
function killContainer() {
	container_id=${1}

	echo -n "Killing and removing $container_id..."
	docker kill ${container_id} > /dev/null
	docker rm ${container_id} > /dev/null
	echo "done"
	
}

# Get list of current running containers
echo "Checking for running chrome containers and killing them..."
containers=$(docker ps | grep chrome | awk '{print $1 "\t" $2}')
if [[ -z ${containers} ]]; then
    echo "No running chrome containers."
else
    while read container; do
		killContainer ${container}
    done < <(docker ps | grep chrome | awk '{print $1}')
fi
echo

# Start containers
count=0
for browser in ${browsers[@]}; do
    selenium_port=${selenium_ports[count]}
    vnc_port=${vnc_ports[count]}
    chrome_path=${chrome_paths[count]}

    if [[ ${browser} == 'chrome-prev' ]]; then
        # Gets the older tag of chrome-stable
        image_tag=$(docker images 'chrome-stable' --format "{{.Tag}}" | head -n2 | tail -n1)
    else
        image_tag=$(docker images ${browser} --format "{{.Tag}}" | head -n1)
    fi

    startContainer "${browser}:${image_tag}" ${selenium_port} ${vnc_port} ${chrome_path}
    if [[ ${browser} == 'chrome-prev' ]]; then
        container_id=$(docker ps | grep ${browser} | awk '{print $1}')
        docker exec --user root -t ${container_id} sed -i -e "s/$(( selenium_port + 1 ))/${selenium_port}/g" -e "s/$(( vnc_port + 1 ))/${vnc_port}/g" /entrypoint.sh
		docker restart ${container_id} > /dev/null
    fi
    (( count++ ))
done
echo

echo "Current running chrome containers:"
echo "--------------------------------------------------"
docker ps | grep chrome | awk '{print $1 "\t" $2}'
echo "--------------------------------------------------"
