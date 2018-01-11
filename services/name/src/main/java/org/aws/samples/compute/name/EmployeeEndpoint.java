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
@Path("employees")
@ApplicationScoped
public class EmployeeEndpoint {

    @PersistenceContext
    EntityManager em;
    
    @GET
    @Produces({"application/xml", "application/json"})
    public Employee[] get() {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList().toArray(new Employee[0]);
    }
    
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Employee get(@PathParam("id") int id) {
        return em
                .createNamedQuery("Employee.findById", Employee.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
