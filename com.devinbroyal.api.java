// Copyright (c) 2024, Devin B. Royal. All Rights Reserved.
package com.devinbroyal.api;

import com.devinbroyal.api.Imports;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableWebSocketMessageBroker
public class InfiniteEquationApiApplication implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private DataService dataService;

    @Autowired
    private DataCompressionService dataCompressionService;

    @Autowired
    private TuringMachineSimulator turingMachineSimulator;

    @Autowired
    private MachineLearningService machineLearningService;

    @Autowired
    private PersonalizedResponseService personalizedResponseService;

    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private KafkaDataStreamer kafkaDataStreamer;

    @Autowired 
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private RealtimeDataSynchronizer realtimeDataSynchronizer;
    
    @Autowired
    private JwtDecoder jwtDecoder;
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    private Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public static void main(String[] args) {
        SpringApplication.run(InfiniteEquationApiApplication.class, args);
    }

    @PostConstruct
    public void init() {
        try {
            blockchainService.init();
            MqttMessageSubscriber mqttMessageSubscriber = new MqttMessageSubscriber(kafkaTemplate, new ObjectMapper());
            mqttMessageSubscriber.init();
        } catch (Exception e) {
            System.err.println("An error occurred during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    // Example of a data controller
    @RestController
    @RequestMapping("/data")
    public class DataController {

        @GetMapping("/all")
        public ResponseEntity<List<Data>> getAllData() {
            List<Data> dataList = dataService.findAll();
            return ResponseEntity.ok(dataList);
        }

        @PostMapping("/compress")
        public ResponseEntity<Data> compressData(@RequestBody Data data) {
            Data compressedData = dataCompressionService.compressData(data);
            return ResponseEntity.ok(compressedData);
        }

        @Cacheable("data")
        @GetMapping("/{id}")
        public ResponseEntity<Data> getDataById(@PathVariable String id) {
            Data data = dataService.findById(id);
            return ResponseEntity.ok(data);
        }
    }

    @RestController
    @RequestMapping("/compute")
    public class ComputationController {

        @PostMapping("/execute")
        public ResponseEntity<ComputationResult> executeComputation(@RequestBody ComputationRequest request) {
            if (request == null || request.getData() == null) {
                throw new InvalidComputationRequestException("Invalid computation request");
            }
            ComputationResult result = turingMachineSimulator.simulate(request);
            return ResponseEntity.ok(result);
        }
    }

    // Health check controller
    @RestController
    @RequestMapping("/health")
    public class HealthCheckController {

        @GetMapping("/status")
        public ResponseEntity<String> checkHealth() {
            return ResponseEntity.ok("Service is running");
        }
    }

    // Kafka listener example
    @KafkaListener(topics = "data-topic", groupId = "group_id")
    public void listen(ConsumerRecord<String, String> record) {
        kafkaDataStreamer.stream(record.value());
    }

    // Exception handling
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleException(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        @ExceptionHandler(InvalidComputationRequestException.class)
        public ResponseEntity<String> handleInvalidComputationRequestException(InvalidComputationRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Spring Security configuration
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2ResourceServer().jwt();
        }
    }

    // Machine learning service example
    @Service
    public class MachineLearningService {

        public String predict(String input) {
            // Implement machine learning prediction logic
            return "Prediction result";
        }
    }
}
