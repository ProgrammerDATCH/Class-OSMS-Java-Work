# Use Tomcat 11.0 with JDK 17
FROM tomcat:11.0-jdk17

# Set working directory
WORKDIR /usr/local/tomcat

# Remove default Tomcat webapps
RUN rm -rf webapps/*

# Copy the WAR file to Tomcat webapps directory as ROOT application
COPY target/osms.war webapps/ROOT.war

# Create server.xml with custom port
RUN echo '<?xml version="1.0" encoding="UTF-8"?>\n\
<Server port="8005" shutdown="SHUTDOWN">\n\
  <Service name="Catalina">\n\
    <Connector port="7050" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />\n\
    <Engine name="Catalina" defaultHost="localhost">\n\
      <Host name="localhost" appBase="webapps" unpackWARs="true" autoDeploy="true">\n\
      </Host>\n\
    </Engine>\n\
  </Service>\n\
</Server>' > conf/server.xml

# Set environment variables
ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Xmx512m"
ENV CATALINA_BASE=/usr/local/tomcat
ENV CATALINA_HOME=/usr/local/tomcat

# Create necessary directories
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost

# Start Tomcat
CMD ["catalina.sh", "run"] 