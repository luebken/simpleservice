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
    public Map env() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("version", System.getenv("SIMPLE_SERVICE_VERSION"));
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            map.put(envName, env.get(envName));
        }
        return map;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}