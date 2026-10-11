package fft_battleground.gene.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import fft_battleground.util.GenericPairing;

class ResultDataDuplicateKeyTest {

	private ResultData newResultDataWithKikuichimonjiDuplicate() {
		List<Integer> geneValues = Arrays.asList(-60, 27, 5);
		List<String> geneNames = Arrays.asList("Kikuichimonji", "Kikuichimonji", "Potion");
		return new ResultData(geneValues, geneNames, 61, true, true, true, true, true, true, false,
				5000, 0.65, 2500, 100, -100, 100, 1, new HashMap<Integer, Double>(), "test-date", 12345L);
	}

	@Test
	void duplicateKeysAreCollapsedToTheEntryWithTheLargerAbsoluteValue() {
		ResultData data = this.newResultDataWithKikuichimonjiDuplicate();

		List<String> keys = data.getGeneticAttributes().stream()
				.map(GenericPairing::getKey)
				.collect(Collectors.toList());
		Set<String> uniqueKeys = new HashSet<>(keys);
		Assertions.assertEquals(keys.size(), uniqueKeys.size(), "geneticAttributes keys must be unique but were: " + keys);

		Integer keptValue = data.getGeneticAttributes().stream()
				.filter(attribute -> attribute.getKey().equals("Kikuichimonji"))
				.map(GenericPairing::getValue)
				.findFirst().orElse(null);
		Assertions.assertEquals(Integer.valueOf(-60), keptValue, "the duplicate with the larger absolute value must be kept");
	}

	@Test
	void duplicateKeysRemainUniqueThroughJsonSerialization() throws Exception {
		ResultData data = this.newResultDataWithKikuichimonjiDuplicate();
		data.sortGeneticAttributesByAbsoluteValue();

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(data);
		ResultData reloaded = mapper.readValue(json, ResultData.class);

		List<String> keys = reloaded.getGeneticAttributes().stream()
				.map(GenericPairing::getKey)
				.collect(Collectors.toList());
		Assertions.assertEquals(keys.size(), new HashSet<>(keys).size(),
				"geneticAttributes keys must stay unique after a write/reload cycle but were: " + keys);
	}
}
