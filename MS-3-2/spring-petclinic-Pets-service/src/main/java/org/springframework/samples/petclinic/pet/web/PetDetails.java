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
package org.springframework.samples.petclinic.pet.web;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.pet.model.Pet;
import org.springframework.samples.petclinic.pet.model.PetType;

import java.util.Date;

/**
 * @author mszarlinski@bravurasolutions.com on 2016-12-05.
 */
@Data
class PetDetails {

    private long id;

    private String name;

    private int ownerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private PetType type;

    PetDetails(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.ownerId = pet.getOwnerId();
        this.birthDate = pet.getBirthDate();
        this.type = pet.getType();
    }
}
