package rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.URL;

@Path("/")
public class ApplicationResource {

    private String html = getSite();

    private String getSite(){
        URL a = getClass().getClassLoader().getResource("api.html");
        try( BufferedReader reader = new BufferedReader(new FileReader(a.getPath().replaceAll("%20", " ")))){
            return reader.lines().reduce("",(acc, curr) -> acc + curr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GET
    @Produces({MediaType.TEXT_HTML})
    public String hello() {
        return html;
    }
}