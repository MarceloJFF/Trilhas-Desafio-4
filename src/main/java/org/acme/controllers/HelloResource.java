package org.acme.controllers;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
