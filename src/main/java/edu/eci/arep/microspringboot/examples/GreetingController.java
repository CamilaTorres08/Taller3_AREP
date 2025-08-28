/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arep.microspringboot.examples;

import edu.eci.arep.microspringboot.annotations.GetMapping;
import edu.eci.arep.microspringboot.annotations.RequestMapping;
import edu.eci.arep.microspringboot.annotations.RequestParam;
import edu.eci.arep.microspringboot.annotations.RestController;
import edu.eci.arep.microspringboot.classes.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static edu.eci.arep.microspringboot.classes.TaskManager.getTaskManager;

/**
 *
 * @author andrea.torres-g
 */
@RestController
@RequestMapping("/app")
public class GreetingController {


	@GetMapping("/greeting")
	public static String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hola " + name;
	}
	@GetMapping("/void")
	public static String defaultValue() {
		return "Hello World!";
	}

	@GetMapping("/params")
	public static String params(@RequestParam(value = "name", defaultValue = "Camila") String name,@RequestParam(value = "gender", defaultValue = "female") String gender) {
		return "Hello " + name + " Gender: " + gender;
	}

	@GetMapping("/body")
	public static void sendNoBody(@RequestParam(value = "name", defaultValue = "World") String name,@RequestParam(value = "gender", defaultValue = "female") String gender) {
		System.out.println("Hello " + name + " Gender: " + gender);
	}

}
