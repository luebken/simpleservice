package simpleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import org.springframework.http.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@RestController
public class Application {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/")
    public String home() {
        return "Hello from SimpleService";
    }

    @RequestMapping("/env")
    public Map<String, Object> env() {
        HashMap<String, Object> root = new HashMap<>();

        HashMap<String, Object> envMap = new HashMap<>();
        envMap.put("SIMPLE_SERVICE_VERSION", System.getenv("SIMPLE_SERVICE_VERSION"));
        envMap.put("SIMPLE_SERVICE_DOWNSTREAM_SERVICE", System.getenv("SIMPLE_SERVICE_DOWNSTREAM_SERVICE"));
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            envMap.put(envName, env.get(envName));
        }
        root.put("env", envMap);

        // Check & call downstream service
        String downstream = System.getenv("SIMPLE_SERVICE_DOWNSTREAM_SERVICE");
        if (downstream != null && !downstream.trim().isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            //headers.set(key, parameters.get(key));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(downstream, HttpMethod.GET, entity, String.class);
            root.put(downstream, response.getBody());
        }
        return root;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}