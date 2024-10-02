package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "KOI API", version = "1.0", description = "Information"))
@SecurityScheme(name = "api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)//custom lai de test token tren swagger
public class DemoApplication {

	// Quản lý sinh viên
	/*
	* 	- Thêm đc 1 sinh viên mới
	* 	- Update thông tin hoặc điểm của sinh viên
	*   - Coi được danh sách tất cả sinh viên
	* 	- Delete 1 thằng sinh viên nào đó
	*
	* */

	// Thêm đc 1 sinh viên mới
	// POST => /api/student

	// Update
	// PUT => /api/student

	// Remove student
	//DELETE => /api/student

	/*
	* 	POST: Create
	* 	PUT: Update
	* 	GET: get
	* 	DELETE: Remove
	* */

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
