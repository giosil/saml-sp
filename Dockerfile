FROM tomcat
ENV DEPLOY_DIR /usr/local/tomcat/webapps
COPY target/saml-sp.war $DEPLOY_DIR
