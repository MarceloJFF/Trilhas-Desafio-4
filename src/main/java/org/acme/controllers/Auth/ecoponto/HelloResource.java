package org.acme.controllers.Auth.ecoponto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message hello() {
        return new Message("Olá, mundo!");
    }

    public static class Message {
        public String message;
        public Message(String message) {
            this.message = message;
        }
    }
}
