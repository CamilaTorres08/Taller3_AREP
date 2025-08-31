/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.eci.arep.microspringboot;

import edu.eci.arep.microspringboot.httpserver.HttpServer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static edu.eci.arep.microspringboot.httpserver.HttpServer.setClassPath;
import static edu.eci.arep.microspringboot.httpserver.HttpServer.staticfiles;

/**
 *
 * @author andrea.torres-g
 */
public class MicroSpringBoot {

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Starting microspringboot");
        //set the directory of static files
        staticfiles("/resources");
        //set the classpath to search the classes
        setClassPath("edu.eci.arep.microspringboot");
//        //value of pi
//        get("/pi", (req, resp) -> {
//            return resp.body(String.valueOf(Math.PI)).contentType("text/html");
//        });
//        //greet
//        get("/hello", (req, resp) -> {
//            String value = req.getValues("name");
//            if(value == null){
//                return resp.body("hello world!").contentType("text/html");
//            }
//            return resp.body("Hello " + value).contentType("text/html");
//        });
//        //return list of tasks
//        get("/tasks", (req, res) -> {
//            String param = req.getValues("name");
//            if (param == null) {
//                return res.status(400).body("Missing filter parameter");
//            }
//            List<Task> tasks;
//            if(!param.equals("All")){
//                tasks = getTaskManager().getTasksByName(param);
//            }else{
//                tasks = getTaskManager().getTasks();
//            }
//            return res.body(tasks);
//        });
//        //return first n numbers in a list
//        get("/numbers", (request, response) -> {
//            String param = request.getValues("n");
//            if(param == null){
//                return response.status(400).body("Missing n parameter");
//            }
//            List<Integer> list = new ArrayList<>();
//            for(int i=1;i<=Integer.parseInt(param);i++){
//                list.add(i);
//            }
//            return response.body(list);
//        });
        HttpServer.runServer(args);
    }
}
