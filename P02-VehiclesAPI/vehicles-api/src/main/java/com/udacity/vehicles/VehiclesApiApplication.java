package com.udacity.vehicles;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Launches a Spring Boot application for the Vehicles API,
 * initializes the car manufacturers in the database,
 * and launches web clients to communicate with maps and pricing.
 *
 * VehiclesApiApplication
 * This launches the Vehicles API as a Spring Boot application.
 * Additionally, it initializes a few car manufacturers to place in the ManufacturerRepository,
 * as well as creating the web clients to connect to the Maps and Pricing services.
 *
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
@EnableSwagger2
public class VehiclesApiApplication {

    @Autowired(required = false)
    EurekaClient eurekaClient;
    public static void main(String[] args) {
        SpringApplication.run(VehiclesApiApplication.class, args);
    }

    /**
     * Initializes the car manufacturers available to the Vehicle API.
     * @param repository where the manufacturer information persists.
     * @return the car manufacturers to add to the related repository
     */
    @Bean
    CommandLineRunner initDatabase(ManufacturerRepository repository, CarRepository carRepository) {
        return args -> {
//            Manufacturer manufacturer = new Manufacturer(100, "Audi");
//            repository.save(manufacturer);
//            repository.save(new Manufacturer(101, "Chevrolet"));
//            repository.save(new Manufacturer(102, "Ford"));
//            repository.save(new Manufacturer(103, "BMW"));
//            repository.save(new Manufacturer(104, "Dodge"));

//            carRepository.save(new Car(1L, LocalDateTime.now(), LocalDateTime.now(), Condition.USED, new Details("wood","crow",manufacturer)));
//            carRepository.save(new Car(22L, LocalDateTime.now(), LocalDateTime.now(), Condition.USED, new Details("steel","house",manufacturer)));
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Web Client for the maps (location) API
     * @param endpoint where to communicate for the maps API
     * @return created maps endpoint
     */
    @Bean(name="maps")
    public WebClient webClientMaps(@Value("${maps.endpoint}") String endpoint) {
        return WebClient.create(endpoint);
    }

    /**
     * Web Client for the pricing API
     * @param endpoint where to communicate for the pricing API
     * @return created pricing endpoint
     */
    @Bean(name="pricing")
    public WebClient webClientPricing(@Value("${pricing.endpoint}") String endpoint) {
        if(eurekaClient != null) {
            try{
                Application app = eurekaClient.getApplications().getRegisteredApplications("PRICING-SERVICE");

                List<InstanceInfo> list = app.getInstances();
                if(list.size() > 0){
                    System.out.println("pricing service found in eureka");
                    return WebClient.create("http://"+list.get(0).getHostName() + ":" + list.get(0).getPort());
                }
            } catch (Exception e){
                System.out.println("pricing service not found in eureka");
            }
        }
        return WebClient.create(endpoint);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
