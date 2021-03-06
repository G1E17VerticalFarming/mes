/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java;

import mes.gui.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// mvn spring-boot:run
// mvn install:install-file -Dfile={dir}.jar -DgroupId=FLIB -DartifactId=flib -Dversion=1.5 -Dpackaging=jar

/**
 * Class used by MAVEN to run the MES application
 * @author DanielToft
 */
@SpringBootApplication
public class Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Main.mainMethod(args);
        
    }
    
}
