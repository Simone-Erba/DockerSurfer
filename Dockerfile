FROM tomcat:7-jre8

ENV CATALINA_OPTS="-DdeployOnStartup=true"
COPY target/DockerSurferWebApp.war $CATALINA_HOME/webapps/
VOLUME ["/data"]