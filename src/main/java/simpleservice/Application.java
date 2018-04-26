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
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

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
        String downstream = System.getenv("SIMPLE_SERVICE_DOWNSTREAM_SERVICES");
        envMap.put("SIMPLE_SERVICE_DOWNSTREAM_SERVICES", downstream);
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            envMap.put(envName, env.get(envName));
        }
        root.put("env", envMap);

        // Check & call downstream services
        if (downstream != null && downstream.split(",").length > 0) {
            for (String svc : downstream.split(",")) {
                logger.info("Found downstream service to call: " + svc);
                HttpHeaders headers = new HttpHeaders();
                //headers.set(key, parameters.get(key));
                HttpEntity<String> entity = new HttpEntity<>(headers);
                try {
                    ResponseEntity<String> response = restTemplate.exchange(downstream, HttpMethod.GET, entity,
                            String.class);
                    root.put(svc, response.getBody());
                } catch (org.springframework.web.client.HttpClientErrorException e) {
                    logger.error("Error from downstream service: " + e);
                    logger.info("Error handled. Not propagated.");
                    root.put(svc, "Error" + e.toString());
                }
            }
        } else {
            logger.info("No downstream services to call");
        }
        return root;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}