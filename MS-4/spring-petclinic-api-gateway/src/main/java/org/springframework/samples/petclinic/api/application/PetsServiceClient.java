package org.springframework.samples.petclinic.api.application;


import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.api.dto.Pets;
import org.springframework.samples.petclinic.api.dto.Visits;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Component
@RequiredArgsConstructor
public class PetsServiceClient {


    // Could be changed for testing purpose
    private String hostname = "http://pets-service/";

    private final WebClient.Builder webClientBuilder;

    public Mono<Pets> getPetsForOwner(final Integer ownerId) {
        return webClientBuilder.build()
            .get()
            .uri(hostname + "owner/pets?ownerId={ownerId}", ownerId)
            .retrieve()
            .bodyToMono(Pets.class);
    }

}
