package rest;

import businessfacades.CityInfoDTOFacade;
import businessfacades.AddressDTOFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.CityInfoDTO;
import utils.EMF_Creator;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Map;

@Path("city")
public class CityResource {

    private static final CityInfoDTOFacade FACADE = CityInfoDTOFacade.getInstance(EMF_Creator.createEntityManagerFactory());
    private static final AddressDTOFacade addressFACADE = AddressDTOFacade.getInstance(EMF_Creator.createEntityManagerFactory());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{zipCode}/addresses")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("zipCode") String zipCode) throws EntityNotFoundException {
        return Response.ok().entity(GSON.toJson(addressFACADE.getAllByZipCode(zipCode))).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})     //It needs to get a json containing HobbyDTO not Hobby
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        CityInfoDTO cityInfoDTO = GSON.fromJson(content, CityInfoDTO.class);
        CityInfoDTO newCityInfoDTO = FACADE.create(cityInfoDTO);
        return Response.ok().entity(GSON.toJson(newCityInfoDTO)).build();
    }

    @PUT
    @Path("/{zipCode}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("zipCode") String zipCode, String cityInfo) throws EntityNotFoundException {
        CityInfoDTO cityInfoDTO = GSON.fromJson(cityInfo, CityInfoDTO.class);
        CityInfoDTO updated = FACADE.update(cityInfoDTO);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("/{zipCode}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("zipCode") String zipCode) throws EntityNotFoundException {
        FACADE.delete(zipCode);
        //CityInfoDTO deleted = FACADE.delete(zipCode);
        return Response.ok().build(); //.entity(GSON.toJson(deleted)).build();
    }

//    @GET
//    @Path("/count")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getCountOfAllMembers() {
//        Map<String, Integer> result = FACADE.getCountOfAllMembers();
//        return Response.ok().entity(GSON.toJson(result)).build();
//    }
//    @GET
//    @Path("/{name}/count")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getCountOfMembersForHobby(@PathParam("name") String name) {
//        Integer result = FACADE.getCountOfMembersForHobby(name);        //TODO: This method has no error handling
//        return Response.ok().entity(GSON.toJson(Collections.singletonMap(name, result))).build();
//    }
}
