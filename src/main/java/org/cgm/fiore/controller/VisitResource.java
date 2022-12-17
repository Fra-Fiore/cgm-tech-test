package org.cgm.fiore.controller;

import io.quarkus.panache.common.Sort;
import org.cgm.fiore.model.Visit;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("/visit")
public class VisitResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(Visit.listAll(Sort.by("date", Sort.Direction.Descending))).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Visit.findByIdOptional(id)
                .map(visit -> Response.ok(visit).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Visit update) {
        Visit visit = Visit.findById(id);
        if (visit != null){
            visit.date = update.date;
            visit.type = update.type;
            visit.reason = update.reason;
            visit.familyHistory = update.familyHistory;
            visit.lastModified = new Date();
            return Response.ok(visit).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = Visit.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
