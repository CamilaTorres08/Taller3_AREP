package edu.eci.arep.microspringboot;

import edu.eci.arep.microspringboot.annotations.GetMapping;
import edu.eci.arep.microspringboot.annotations.RequestMapping;
import edu.eci.arep.microspringboot.annotations.RestController;
import edu.eci.arep.microspringboot.connection.URLConnection;
import edu.eci.arep.microspringboot.examples.GreetingController;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;

import static edu.eci.arep.microspringboot.httpserver.HttpServer.start;
import static edu.eci.arep.microspringboot.httpserver.HttpServer.staticfiles;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ControllerTests {
    private static Thread serverThread;
    private static final int port = 35002;
    static URLConnection urlConnection;
    @BeforeClass
    public static void setUp() throws Exception {
        staticfiles("/resources");
        urlConnection = new URLConnection(port);
        serverThread = new Thread(() -> {
            try { start(port,new String[]{"edu.eci.arep.microspringboot.examples.GreetingController"}); } catch (Exception ignored) {}
        }, "http-server-test");
        serverThread.start();
        Thread.sleep(2000);
    }
    @AfterClass
    public static void tearDown() throws Exception {
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }

    /*
    *Not send parameter to endpoint /greeting, should response "hello world"
     */
    @Test
    public void testGreetingWithDefaultParameter() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/app/greeting");
        String response = urlConnection.readResponse(getConnection);
        assertEquals("Hello World", response.trim());
        getConnection.disconnect();
    }
    /*
     *Sending parameter to endpoint /greeting, Should response the message with the parameter
     */
    @Test
    public void testGreetingWithCustomName() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/app/greeting?name=Juan");
        String response = urlConnection.readResponse(getConnection);
        assertEquals("Hola Juan", response.trim());
        getConnection.disconnect();
    }

    /*
     *Testing endpoint /void, should return always "hello world"
     */
    @Test
    public void testVoidEndpoint() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/app/void");
        String response = urlConnection.readResponse(getConnection);
        assertEquals("Hello World!", response.trim());
        getConnection.disconnect();
    }
    /*
    *Testing if default values are setting when we call /params
     */
    @Test
    public void testParamsWithDefaultValues() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/app/params");
        String response = urlConnection.readResponse(getConnection);
        assertEquals("Name: Camila Gender: female Age: 23", response.trim());
        getConnection.disconnect();
    }
    /*
     *Testing if default 'Gender' and 'Age' values are setting when we call /params
     */
    @Test
    public void testParamsWithCustomName() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/app/params?name=Carlos");
        String response = urlConnection.readResponse(getConnection);
        assertEquals("Name: Carlos Gender: female Age: 23", response.trim());
        getConnection.disconnect();
    }
    /*
     *Testing if default 'Name' and 'Gender' values are setting when we call /params
     */
    @Test
    public void testParamsWithCustomAge() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/app/params?age=30");
        String response = urlConnection.readResponse(getConnection);
        assertEquals("Name: Camila Gender: female Age: 30", response.trim());
        getConnection.disconnect();
    }
    /*
     *Testing if default 'Age' value are setting when we call /params
     */
    @Test
    public void testParamsWithBothParameters() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/app/params?name=Andres&gender=male");
        String response = urlConnection.readResponse(getConnection);
        assertEquals("Name: Andres Gender: male Age: 23", response.trim());
        getConnection.disconnect();
    }
    /*
     *Testing when we not send body message, should response with 204
     */
    @Test
    public void testBodyEndpointReturnsNoContent() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/app/body");
        String response = urlConnection.readResponse(getConnection);
        int responseCode = getConnection.getResponseCode();
        assertEquals("Should return 204 No Content",204, responseCode);
        assertTrue("Response should be empty",
                response == null || response.trim().isEmpty());
        getConnection.disconnect();
    }

    /*
     *Testing if GreetingController has the RestController annotation
     */
    @Test
    public void testControllerHasRestControllerAnnotation() {
        Class<?> controllerClass = GreetingController.class;
        boolean hasRestController = controllerClass.isAnnotationPresent(RestController.class);
        assertTrue("Controller should have @RestController annotation", hasRestController);
    }
    /*
     *Testing if GreetingController has the RequestMapping annotation
     */
    @Test
    public void testControllerHasRequestMappingAnnotation() {
        Class<?> controllerClass = GreetingController.class;
        boolean hasRequestMapping = controllerClass.isAnnotationPresent(RequestMapping.class);
        assertTrue("Controller should have @RequestMapping annotation", hasRequestMapping);

        RequestMapping mapping = controllerClass.getAnnotation(RequestMapping.class);
        assertEquals("Should map to /app", "/app", mapping.value());
    }
    /*
     *Testing if GreetingController has GetMapping annotation
     */
    @Test
    public void testGreetingMethodHasGetMappingAnnotation() throws NoSuchMethodException {
        Method greetingMethod = GreetingController.class.getMethod("greeting", String.class);
        boolean hasGetMapping = greetingMethod.isAnnotationPresent(GetMapping.class);
        assertTrue("greeting method should have @GetMapping annotation", hasGetMapping);

        GetMapping mapping = greetingMethod.getAnnotation(GetMapping.class);
        assertEquals("Should map to /greeting", "/greeting", mapping.value());
    }

}
