package simpleservice;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class Application {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger("simpleservice");

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @RequestMapping("/last-endpoint")
    public String home() {
        logger.info("Request to endpoint: " + request.getRequestURL().toString());
        return "Hello from SimpleService";
    }

    @RequestMapping("/endpoint*")
    public Map<String, Object> endpoint(@RequestHeader Map<String, String> requestHeader) {
        logger.info("Request to endpoint: " + request.getRequestURL().toString());

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
            logger.info("Found downstream service to call: " + downstream);
            HttpHeaders headers = new HttpHeaders();
            //headers.set(key, parameters.get(key));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(downstream, HttpMethod.GET, entity, String.class);
            root.put(downstream, response.getBody());
        } else {
            logger.info("No downstream service to call");
        }
        return root;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}