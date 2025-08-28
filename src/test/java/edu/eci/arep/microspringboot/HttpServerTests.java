package edu.eci.arep.microspringboot;
import edu.eci.arep.microspringboot.annotations.RequestMapping;
import edu.eci.arep.microspringboot.examples.TaskController;
import org.junit.*;
import java.net.*;

import edu.eci.arep.microspringboot.connection.URLConnection;
import static edu.eci.arep.microspringboot.httpserver.HttpServer.start;
import static edu.eci.arep.microspringboot.httpserver.HttpServer.staticfiles;
import static org.junit.Assert.*;

public class HttpServerTests {
    private static Thread serverThread;
    private static final int port = 35001;
    static URLConnection urlConnection;
    @BeforeClass
    public static void setUp() throws Exception {
        staticfiles("/resources");
        urlConnection = new URLConnection(port);
        serverThread = new Thread(() -> {
            try { start(port,new String[]{"edu.eci.arep.microspringboot.examples.TaskController"}); } catch (Exception ignored) {}
        }, "http-server-test");
        serverThread.start();
        Thread.sleep(2000);
    }
    @AfterClass
    public static void tearDown() {
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }
    /*
     *Returns index.html file
     */
    @Test
    public void testGetHtmlFile() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/");
        int responseCode = getConnection.getResponseCode();
        assertEquals("Should return 200 OK",200, responseCode);
        getConnection.disconnect();
        HttpURLConnection getConnectionWithFile = urlConnection.createGetConnection("/index.html");
        int responseCodeWithFile = getConnectionWithFile.getResponseCode();
        assertEquals("Should return 200 OK",200, responseCodeWithFile);
        getConnectionWithFile.disconnect();
    }
    /*
     *Returns style.css file
     */
    @Test
    public void testGetCssFile() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/styles/style.css");
        int responseCode = getConnection.getResponseCode();
        assertEquals("Should return 200 OK",200, responseCode);
        getConnection.disconnect();
    }
    /*
     *Returns script.js file
     */
    @Test
    public void testGetJsFile() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/scripts/script.js");
        int responseCode = getConnection.getResponseCode();
        assertEquals("Should return 200 OK",200, responseCode);
        getConnection.disconnect();
    }
    /*
     *Returns image file
     */
    @Test
    public void testGetImageFile() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/images/logo.png");
        int responseCode = getConnection.getResponseCode();
        assertEquals("Should return 200 OK",200, responseCode);
        getConnection.disconnect();
    }
    /*
     *Trying to get an unkown file
     */
    @Test
    public void testGetUnkownFile() throws Exception {
        HttpURLConnection getConnection = urlConnection.createGetConnection("/prueba.html");
        int responseCode = getConnection.getResponseCode();
        assertEquals("Should return 404 Not Found",404, responseCode);
        getConnection.disconnect();
    }

    /*
     *Testing if TaskController has the RequestMapping annotation and the correct path /task
     */
    @Test
    public void testControllerHasRequestMappingAnnotation() {
        Class<?> controllerClass = TaskController.class;
        boolean hasRequestMapping = controllerClass.isAnnotationPresent(RequestMapping.class);
        assertTrue("Controller should have @RequestMapping annotation", hasRequestMapping);

        RequestMapping mapping = controllerClass.getAnnotation(RequestMapping.class);
        assertEquals("Should map to /task", "/task", mapping.value());
    }

    /*
     *Get all tasks successfully
     * In the lambda, we did not specify the content-type and the status 200 OK,
     * the server should return automatically 200 OK and the header content-type : application/json automatically with the
     * list of tasks
     */
    @Test
    public void testGetTasks() throws Exception {
        String jsonPayload = "{\"name\":\"Get Test Task\",\"description\":\"For GET test\"}";
        HttpURLConnection postConnection = urlConnection.createPostConnection("/task/saveTask", jsonPayload);
        postConnection.getResponseCode(); // Ejecutar POST
        postConnection.disconnect();

        HttpURLConnection getConnection = urlConnection.createGetConnection("/task?name=All");

        int responseCode = getConnection.getResponseCode();
        assertEquals("Should return 200 OK",200, responseCode);

        String responseBody = urlConnection.readResponse(getConnection);
        assertFalse("Response should contain tasks", responseBody.isEmpty());

        String responseHeader = getConnection.getHeaderField("Content-Type");
        assertEquals("Should return application/json",responseHeader,"application/json");
        getConnection.disconnect();
    }
    /*
     *Get tasks by filter name successfully
     *
     */
    @Test
    public void testGetTasksByName() throws Exception {
        String jsonPayload = "{\"name\":\"Task GET\",\"description\":\"For GET test\"}";
        HttpURLConnection postConnection = urlConnection.createPostConnection("/task/save", jsonPayload);
        postConnection.getResponseCode();
        postConnection.disconnect();

        HttpURLConnection getConnection = urlConnection.createGetConnection("/task?name=Task%20GET");

        int responseCode = getConnection.getResponseCode();
        assertEquals("Should return 200 OK",200, responseCode);

        String responseBody = urlConnection.readResponse(getConnection);
        String[] inner = responseBody.trim().substring(1, responseBody.length()-1).trim().split("},");
        assertEquals("Response should contain one task", 1, inner.length);
        getConnection.disconnect();
    }


    /*
     *Send Other base path
     */
    @Test
    public void testMethodNotAllowed() throws Exception {
        HttpURLConnection connection = urlConnection.createConnection("/app/task", "PUT");

        int responseCode = connection.getResponseCode();
        assertEquals("Should return 405 Method Not Allowed",405, responseCode);

        connection.disconnect();
    }



}
