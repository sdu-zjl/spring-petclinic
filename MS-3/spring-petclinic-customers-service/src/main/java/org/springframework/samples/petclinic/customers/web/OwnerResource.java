/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.customers.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@RequestMapping("/owners")
@RestController
@Timed("petclinic.owner")
@RequiredArgsConstructor
@Slf4j
class OwnerResource {

    @Autowired
    private RestTemplate restTemplate;

    private final OwnerRepository ownerRepository;

    /**
     * Create Owner
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Owner createOwner(@Valid @RequestBody Owner owner) {
        return ownerRepository.save(owner);
    }

    /**
     * Read single Owner
     */
    @GetMapping(value = "/{ownerId}")
    public Optional<Owner> findOwner(@PathVariable("ownerId") int ownerId) {
        return ownerRepository.findById(ownerId);
    }

    /**
     * Read List of Owners
     */
    @GetMapping
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    /**
     * Update Owner
     */
    @PutMapping(value = "/{ownerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOwner(@PathVariable("ownerId") int ownerId, @Valid @RequestBody Owner ownerRequest) {
        final Optional<Owner> owner = ownerRepository.findById(ownerId);

        final Owner ownerModel = owner.orElseThrow(() -> new ResourceNotFoundException("Owner "+ownerId+" not found"));
        // This is done by hand for simplicity purpose. In a real life use-case we should consider using MapStruct.
        ownerModel.setFirstName(ownerRequest.getFirstName());
        ownerModel.setLastName(ownerRequest.getLastName());
        ownerModel.setCity(ownerRequest.getCity());
        ownerModel.setAddress(ownerRequest.getAddress());
        ownerModel.setTelephone(ownerRequest.getTelephone());
        log.info("Saving owner {}", ownerModel);
        ownerRepository.save(ownerModel);
    }

    @GetMapping(value = "getOwnerDetail/{ownerId}")
    public String getOwnerDetail(@PathVariable("ownerId") int ownerId) {
        Optional<Owner> ownerOptional = ownerRepository.findById(ownerId);
        Owner owner = ownerOptional.get();
        JSONObject ownerJson = new JSONObject();
        ownerJson.put("id", owner.getId());
        ownerJson.put("firstName", owner.getFirstName());
        ownerJson.put("lastName", owner.getLastName());
        ownerJson.put("city", owner.getCity());
        ownerJson.put("address", owner.getAddress());
        ownerJson.put("telephone", owner.getTelephone());
        List<Pet> pets = owner.getPets();
        JSONArray petArray = new JSONArray();
        for (Pet pet : pets) {
            JSONObject petJson = new JSONObject();
            petJson.put("pet_id", pet.getId());
            petJson.put("birth_date", pet.getBirthDate());
            petJson.put("pet_type", pet.getType());
            String uri="http://visits-service/getVisitsByPetId/"+ownerId;
            String visitsArrayString = restTemplate.getForEntity(uri, String.class).getBody();
            JSONArray visitsArray = JSONArray.parseArray(visitsArrayString);
            petJson.put("visits", visitsArray);
            petArray.add(petJson);
        }
        ownerJson.put("pets", petArray);
        return ownerJson.toJSONString();
    }

}
