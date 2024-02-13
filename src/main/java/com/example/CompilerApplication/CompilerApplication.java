package com.example.CompilerApplication;

import com.example.CompilerApplication.model.dto.CodeExecutionRequest;
import com.example.CompilerApplication.service.Implementation.PistonServiceImplementation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
//@RestController
public class CompilerApplication {

	private final PistonServiceImplementation pistonServiceImplementation;

    public CompilerApplication(PistonServiceImplementation pistonServiceImplementation) {
        this.pistonServiceImplementation = pistonServiceImplementation;
    }

    public static void main(String[] args) {
		SpringApplication.run(CompilerApplication.class, args);
	}

//	@GetMapping
//	public String get() {
//		return "Boopathi Domer";
//	}
//
//	@PostMapping("/execute")
//	public String ExecuteCode(@RequestBody CodeExecutionRequest request) {
//		pistonServiceImplementation.CodeExecution(request);
//		return "SAVED";
//	}

}
