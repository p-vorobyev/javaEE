FROM voroby/wild_fly_liberica:1.0

COPY target/ee-v1.war /opt/jboss/wildfly/standalone/deployments/
