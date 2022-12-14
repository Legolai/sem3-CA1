package rest;

import businessfacades.HobbyDTOFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HobbyDTO;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

@Path("hobby")
public class HobbyResource {

    private static final HobbyDTOFacade FACADE =  HobbyDTOFacade.getInstance(EMF_Creator.createEntityManagerFactory());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @GET
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("name") String name) throws EntityNotFoundException {
        return Response.ok().entity(GSON.toJson(FACADE.getById(name))).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})     //It needs to get a json containing HobbyDTO not Hobby
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        HobbyDTO hdto = GSON.fromJson(content, HobbyDTO.class);
        HobbyDTO newHdto = FACADE.create(hdto);
        return Response.ok().entity(GSON.toJson(newHdto)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @PUT
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("name") String name, String content) throws EntityNotFoundException {
        HobbyDTO hdto = GSON.fromJson(content, HobbyDTO.class);
        hdto.setName(name);
        HobbyDTO updated = FACADE.update(hdto);
        return Response.ok().entity(GSON.toJson(updated)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @DELETE
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("name") String name) throws EntityNotFoundException {
        FACADE.delete(name);
        //HobbyDTO deleted = FACADE.delete(name);
        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build(); //.entity(GSON.toJson(deleted)).build();
    }

    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCountOfAllMembers() {
        Map<String, Integer> result = FACADE.getCountOfAllMembers();
        return Response.ok().entity(GSON.toJson(result)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }
    @GET
    @Path("/{name}/count")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCountOfMembersForHobby(@PathParam("name") String name) {
        Integer result = FACADE.getCountOfMembersForHobby(name);        //TODO: This method has no error handling
        return Response.ok().entity(GSON.toJson(Collections.singletonMap(name, result))).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }
}
