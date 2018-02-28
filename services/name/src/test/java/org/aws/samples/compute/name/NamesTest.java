package org.aws.samples.compute.name;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.json.JSONArray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Kevin Yung
 */
@RunWith(Arquillian.class)
public class NamesTest {

    private WebTarget target;
    //private Client client;

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        MyApplication.class,
                        NameEndpoint.class,
                        Name.class,
                        Names.class);
    }

    @Before
    public void setUp() {
    }

    @Test
    @RunAsClient
    public void testfindById() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "resources/names/1").toExternalForm()));
        String response = target.request().get(String.class);
        assertEquals("Sheldon", response);
    }

    @Test
    @RunAsClient
    public void testFindAll() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "resources/names").toExternalForm()));
        String response = target.request().get(String.class);
        JSONArray obj = new JSONArray(response);
        assertTrue(obj.length() > 0);
    }
}
