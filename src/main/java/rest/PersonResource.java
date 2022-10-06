package rest;

import businessfacades.HobbyDTOFacade;
import businessfacades.PersonDTOFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Path("person")
public class PersonResource {

    private static final PersonDTOFacade FACADE =  PersonDTOFacade.getInstance(EMF_Creator.createEntityManagerFactory());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") Integer id) throws EntityNotFoundException {
        return Response.ok().entity(GSON.toJson(FACADE.getById(id))).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})     //It needs to get a json containing HobbyDTO not Hobby
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        PersonDTO pdto = GSON.fromJson(content, PersonDTO.class);
        PersonDTO newPdto = FACADE.create(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") Integer id, String content) throws EntityNotFoundException {
        PersonDTO pdto = GSON.fromJson(content, PersonDTO.class);
        pdto.setId(id);
        PersonDTO updated = FACADE.update(pdto);
        return Response.ok().entity(GSON.toJson(updated)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") Integer id) throws EntityNotFoundException {
        FACADE.delete(id);
        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build(); //.entity(GSON.toJson(deleted)).build();
    }

    @GET
    @Path("/phone/{phoneNumber}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByPhoneNumber(@PathParam("phoneNumber") String phoneNumber) throws EntityNotFoundException {
        return Response.ok().entity(GSON.toJson(FACADE.getByPhoneNumber(phoneNumber))).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @GET
    @Path("/city/{zipCode}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllByZipCode(@PathParam("zipCode") String zipCode) throws EntityNotFoundException {
        return Response.ok().entity(GSON.toJson(FACADE.getAllByZipCode(zipCode))).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

}
