#!/bin/sh
#
# VNC_PORT and SELENIUM_PORT come from the Dockerfile and are provided 
# as --build-arg in the 'docker run' command

pkill Xfv
rm -f /tmp/.X99-lock > /dev/null 2>&1 

XVFB_WHD=${XVFB_WHD:-1280x720x16}

Xvfb :99 -ac -screen 0 $XVFB_WHD -nolisten tcp > /home/chrome/xvfb 2>&1 &
export DISPLAY=:99

x11vnc -rfbport VNC_PORT -display :99 -forever > /home/chrome/x11vnc 2>&1 &

exec_path="/usr/local/bin"
selenium_jar="${exec_path}/selenium-server-standalone-3.3.1.jar"

nohup java -jar $selenium_jar -port SELENIUM_PORT > /dev/null 2>&1 &

exec $@
