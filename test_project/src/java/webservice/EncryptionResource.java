/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import java.util.concurrent.ExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import encryptionapp.EncryptionApp;
import javax.json.Json;

/**
 * REST Web Service
 *
 * @author kunal
 */
@Path("resource")
@RequestScoped
public class EncryptionResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public EncryptionResource() {
    }
    private ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    /**
     * Retrieves representation of an instance of webservice.EncryptionResource
     *
     * @param asyncResponse
     * @return an instance of java.lang.String
     */
    @Path(value = "/encrypt")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getStringToEncrypt(@Suspended final AsyncResponse asyncResponse) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetStringToEncrypt());
        });
    }

    private Response doGetStringToEncrypt() {
        JsonObject value = null;
        try {
            MultivaluedMap<String, String> multivaluedMap = context.getQueryParameters();
            String toEncrypt = multivaluedMap.getFirst("key");
            if (toEncrypt != null && toEncrypt.trim().length() > 2) {
              String encryptedString = EncryptionApp.encrypt(toEncrypt);
                          value = (JsonObject) Json.createObjectBuilder().add("success", "true")
                        .add("data", Json.createObjectBuilder()
                                .add("message", encryptedString))
                        .build();
            }
        } catch (Exception e) {
             value = (JsonObject) Json.createObjectBuilder().add("success", "false").add("data", "null").build();
        }
       return Response.status(201).entity(value).header("accept", "application/json").header("Access-Control-Allow-Origin", "*").build();
    }

}
