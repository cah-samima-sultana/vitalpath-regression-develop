# How to Run:
#
#    docker run -p 6000:6000 -p 4567:4567 -dt chromium-selenium /bin/bash

FROM debian:jessie
ARG CHROME_PACKAGE
ARG CHROME_PATH
ARG VNC_PORT
ARG SELENIUM_PORT

##libgconf-2-4 is a dependency for chromedriver
RUN apt-get update && apt-get install -y curl xvfb x11vnc libgconf-2-4 unzip

# Download and install Java 8
RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" >> /etc/apt/sources.list.d/java-8-debian.list \
    && echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" >> /etc/apt/sources.list.d/java-8-debian.list \
    && apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886 \
    && apt-get update \
    && echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections \
    && apt-get install -y oracle-java8-installer
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Download and install selenium-server-standalone
RUN wget -q http://selenium-release.storage.googleapis.com/3.3/selenium-server-standalone-3.3.1.jar \
    && mv selenium-server-standalone-3.3.1.jar /usr/local/bin/

# Download and install chrome, move chrome to chrome-base for the --no-sandbox script later
RUN echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list \
    && wget https://dl-ssl.google.com/linux/linux_signing_key.pub \
    && apt-key add linux_signing_key.pub \
    && rm linux_signing_key.pub \
    && apt-get update \
    && apt-get install -y ${CHROME_PACKAGE} \
    && mv ${CHROME_PATH}/chrome ${CHROME_PATH}/chrome-base
# Copy in wrapper script for chrome that forces --sandbox
COPY chrome ${CHROME_PATH}/
RUN chmod 755 ${CHROME_PATH}/chrome \
    && sed -i -e s#CHROME_PATH#${CHROME_PATH}#g ${CHROME_PATH}/chrome

# Download and install chromedriver
RUN wget -q "https://chromedriver.storage.googleapis.com/2.28/chromedriver_linux64.zip" \
    && unzip chromedriver_linux64.zip \
    && rm chromedriver_linux64.zip \ 
    && mv chromedriver /usr/local/bin/ \
    && chmod 755 /usr/local/bin/chromedriver

# Set user to chrome
RUN useradd -ms /bin/bash chrome 

# Entrypoint script creates an Xvfb session, start a vnc server, starts selenium server
COPY entrypoint.sh /entrypoint.sh
RUN chmod 755 /entrypoint.sh \
    && sed -i -e s#VNC_PORT#${VNC_PORT}#g \
              -e s#SELENIUM_PORT#${SELENIUM_PORT}#g \
              /entrypoint.sh 

USER chrome
ENTRYPOINT ["/entrypoint.sh"]
