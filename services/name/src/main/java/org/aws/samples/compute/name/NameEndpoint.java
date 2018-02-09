package org.aws.samples.compute.name;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * @author Arun Gupta
 */
@Path("names")
@ApplicationScoped
public class NameEndpoint {

    @PersistenceContext
    EntityManager em;
    
    @GET
    @Produces({"application/xml", "application/json"})
    public Name[] get() {
        return em.createNamedQuery("Name.findAll", Name.class).getResultList().toArray(new Name[0]);
    }
    
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json", "text/plain"})
    public Name get(@PathParam("id") int id) {
        return em
                .createNamedQuery("Name.findById", Name.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
