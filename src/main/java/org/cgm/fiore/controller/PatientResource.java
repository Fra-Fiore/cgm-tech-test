package org.cgm.fiore.controller;

import org.cgm.fiore.model.Patient;
import org.cgm.fiore.model.Visit;
import org.hibernate.Hibernate;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Date;

@Path("/patient")
public class PatientResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(Patient.listAll()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Patient.findByIdOptional(id)
                .map(patient -> Response.ok(patient).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Patient patient) {
        Patient.persist(patient);
        if (patient.isPersistent())
            return Response.created(URI.create("/patient" + patient.id)).build();

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePatient(@PathParam("id") Long id, Patient update) {
        Patient patientToUpdate = Patient.findById(id);
        if (patientToUpdate != null){
            patientToUpdate.name = update.name;
            patientToUpdate.surname = update.surname;
            patientToUpdate.ssn = update.ssn;
            patientToUpdate.birthDate = update.birthDate;
            patientToUpdate.lastModified = new Date();
            return Response.ok(patientToUpdate).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = Patient.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/visits")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVisits(@PathParam("id") Long id) {
        Patient patient = Patient.findById(id);
        if (patient != null){
            Hibernate.initialize(patient.visits);
            return Response.ok(patient.visits).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Transactional
    @Path("{id}/visits")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addVisit(@PathParam("id") Long id, Visit visit) {
        Patient patientToUpdate = Patient.findById(id);
        if (patientToUpdate != null){
            patientToUpdate.addVisit(visit);
            patientToUpdate.persist();
            Hibernate.initialize(patientToUpdate.visits);
            return Response.ok(patientToUpdate.visits).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Transactional
    @Path("{id}/visits/{visitId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteVisit(@PathParam("id") Long id, @PathParam("visitId") Long visitId) {
        Patient patientToUpdate = Patient.findById(id);
        Visit visitToRemove = Visit.findById(visitId);
        if (patientToUpdate != null && visitToRemove != null){
            patientToUpdate.removeVisit(visitToRemove);
            patientToUpdate.persist();
            Hibernate.initialize(patientToUpdate.visits);
            return Response.ok(patientToUpdate.visits).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
