package rest;

import businessfacades.PhotoDTOFacade;
import datafacades.IExtraFunc;
import dtos.TagDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datafacades.IDataFacade;
import dtos.PhotoDTO;
import errorhandling.EntityNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

//Todo Remove or change relevant parts before ACTUAL use
@Path("photo")
public class PhotoResource {
       
    private static final IDataFacade<PhotoDTO> FACADE =  PhotoDTOFacade.getFacade();
    private static final IExtraFunc<TagDTO> TagFacade =  PhotoDTOFacade.getTagFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        PhotoDTO p = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(p)).build();
    }

    @GET
    @Path("/allTags")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllTags() {
        List<TagDTO> tagDtos = TagFacade.getAllElements();
        return Response.ok().entity(GSON.toJson(tagDtos)).build();
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        PhotoDTO pdto = GSON.fromJson(content, PhotoDTO.class);
        PhotoDTO newPdto = FACADE.create(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        PhotoDTO pdto = GSON.fromJson(content, PhotoDTO.class);
        pdto.setId(id);
        PhotoDTO updated = FACADE.update(pdto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        PhotoDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }
}
