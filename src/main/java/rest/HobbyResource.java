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

@Path("hobby")
public class HobbyResource {

    private static final HobbyDTOFacade FACADE =  HobbyDTOFacade.getInstance(EMF_Creator.createEntityManagerFactory());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("name") String name) throws EntityNotFoundException {
        HobbyDTO h = FACADE.getById(name);
        return Response.ok().entity(GSON.toJson(h)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        HobbyDTO hdto = GSON.fromJson(content, HobbyDTO.class);
        HobbyDTO newHdto = FACADE.create(hdto);
        return Response.ok().entity(GSON.toJson(newHdto)).build();
    }

    @PUT
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("name") String name, String content) throws EntityNotFoundException {
        HobbyDTO hdto = GSON.fromJson(content, HobbyDTO.class);
        hdto.setName(name);
        HobbyDTO updated = FACADE.update(hdto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("name") String name) throws EntityNotFoundException {
        FACADE.delete(name);
        //HobbyDTO deleted = FACADE.delete(name);
        return Response.ok().build(); //.entity(GSON.toJson(deleted)).build();
    }


}
