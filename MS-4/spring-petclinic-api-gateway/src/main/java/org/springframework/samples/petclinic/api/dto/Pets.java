package org.springframework.samples.petclinic.api.dto;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class Pets {

    private List<PetDetails> items = new ArrayList<>();

}
