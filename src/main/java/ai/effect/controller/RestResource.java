package ai.effect.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import ai.effect.models.Individual;
import ai.effect.models.Website;
import ai.effect.datasource.SqlHandler;
import ai.effect.servlet.Servlet;

@Path("/api")
public class RestResource {
		@Context SqlHandler sql;

    @POST @Path("/website/add")
		@Consumes("application/x-www-form-urlencoded")
		@Produces("application/json")
		public Response addSite(@FormParam("url") String url, @FormParam("json") String json) {
				// Create website object and tracker file
        Website website = new Website(url, json, sql);
        int id = website.getId();
        File default_tracker = new File("js/default-tracker.js");
        File tracker = new File("js/tracker-"+id+".js");

        try {
            Files.copy(default_tracker.toPath(), tracker.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.write(tracker.toPath(), Integer.toString(id).getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {}

        // TODO: foreach profile add starting DNA
        //new Individual(1, id, website.getDna(), this.sql);

				String resp = "{\"script\": \"localhost:7070/js/tracker-"+id+".js\"}";
				return Response.ok().entity(resp).build();
		}
}
