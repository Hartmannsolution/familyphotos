package rest;

import businessfacades.PhotoDTOFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datafacades.IDataFacade;
import datafacades.IExtraFunc;
import dtos.PhotoDTO;
import dtos.TagDTO;
import entities.Photo;
import errorhandling.EntityNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

//Todo Remove or change relevant parts before ACTUAL use
@Path("test")
public class TestResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {

        return Response.ok().entity("{\"msg\":\"demo\"}").build();
    }
}
