# Docker file based on https://github.com/jboss-dockerfiles/wildfly/blob/master/Dockerfile with liberica java 16
# Use bellsoft/liberica-openjdk-alpine:16.0.2-7
FROM bellsoft/liberica-openjdk-alpine:16.0.2-7

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 24.0.0.Final
ENV WILDFLY_SHA1 391346c9ed2772647ff07aeae39deb838ee11dcf
ENV HOME /root

USER root

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN cd $HOME \
    && apk --no-cache add curl \
    && mkdir -p /opt/jboss/wildfly \
    && addgroup -S jboss && adduser -S -G jboss jboss \
    && curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
    && sha1sum wildfly-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-$WILDFLY_VERSION/* /opt/jboss/wildfly \
    && rm wildfly-$WILDFLY_VERSION.tar.gz \
    && chown -R jboss:jboss /opt/jboss/wildfly \
    && chmod -R g+rw /opt/jboss/wildfly

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

USER jboss

# Expose the ports in which we're interested
EXPOSE 8080

# Set the default command to run on boot
# This will boot WildFly in standalone mode and bind to all interfaces
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
