package org.money.back.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("resources")
class ApplicationConfig extends ResourceConfig {

    ApplicationConfig() {
        packages("org.money.back");
        register(JacksonFeature.class);
    }
}
