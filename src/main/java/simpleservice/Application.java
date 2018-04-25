package simpleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
        return "Hello from SimpleService";
    }

    @RequestMapping("/env")
    public Map<String, Object> env() {
        HashMap<String, Object> root = new HashMap<>();

        HashMap<String, Object> envMap = new HashMap<>();
        envMap.put("SIMPLE_SERVICE_VERSION", System.getenv("SIMPLE_SERVICE_VERSION"));
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            envMap.put(envName, env.get(envName));
        }
        root.put("env", envMap);
        
        return root;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}