package org.springframework.samples.petclinic.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class TestAPIController {

	private final OwnerRepository owners;

	public TestAPIController(OwnerRepository clinicService) {
		this.owners = clinicService;
	}

	@GetMapping("/owners/{ownerId}/Detail")
	public String getOwnerDetail(@PathVariable("ownerId") int ownerId) {
		Owner owner = this.owners.findById(ownerId);
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
			Collection<Visit> visits = pet.getVisits();
			JSONArray visitsArray = new JSONArray();
			for (Visit visit : visits) {
				JSONObject visitJson = new JSONObject();
				visitJson.put("data", visit.getDate());
				visitJson.put("description", visit.getDescription());
				visitsArray.add(visitJson);
			}
			petJson.put("visits", visitsArray);
			petArray.add(petJson);
		}
		ownerJson.put("pets", petArray);
		return ownerJson.toJSONString();
	}

}
