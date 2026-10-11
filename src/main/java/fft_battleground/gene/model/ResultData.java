package fft_battleground.gene.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fft_battleground.util.GenericPairing;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ResultData {
	private boolean abilityEnabled;
	private boolean itemEnabled;
	private boolean userSkillsEnabled;
	private boolean classEnabled;
	private boolean mapsEnabled;
	private boolean braveFaithEnabled;
	private boolean sideEnabled;
	private int numberOfAnalyzedTournaments;
	private int currentResultSuccessPercentage;
	private double threshold;
	private int population;
	private int maxGene;
	private int minGene;
	private int maxBraveFaithGene;
	private int minBraveFaithGene;
	private String creationDateString;
	private long score;
	private List<GenericPairing<String, Integer>> geneticAttributes;
	private List<GenericPairing<Integer, Double>> percentiles;
	
	public ResultData() {}
	
	public ResultData(List<Integer> geneAttributes, List<String> attributeNames, int currentResultSuccessPercentage, boolean ABILITY_ENABLED, boolean ITEM_ENABLED,
	boolean USER_SKILLS_ENABLED, boolean CLASS_ENABLED, boolean MAPS_ENABLED, boolean BRAVE_FAITH_ENABLED,
	boolean SIDE_ENABLED, int numberOfAnalyzedTournaments, double threshold, int population, int maxGene, int minGene, 
	int maxBraveFaithGene, int minBraveFaithGene, Map<Integer, Double> percentiles, String creationDateString, long score) {
		this.geneticAttributes = new ArrayList<>();
		Map<String, GenericPairing<String, Integer>> uniqueAttributes = new LinkedHashMap<>();
		for(int i = 0; i < geneAttributes.size() && i < attributeNames.size(); i++) {
			String attributeName = attributeNames.get(i);
			Integer geneValue = geneAttributes.get(i);
			GenericPairing<String, Integer> existingAttribute = uniqueAttributes.get(attributeName);
			if(existingAttribute == null) {
				uniqueAttributes.put(attributeName, new GenericPairing<>(attributeName, geneValue));
			} else if(Math.abs(geneValue) > Math.abs(existingAttribute.getValue())) {
				log.warn("Duplicate geneticAttribute key '{}': keeping new value {} over existing value {}", attributeName, geneValue, existingAttribute.getValue());
				uniqueAttributes.put(attributeName, new GenericPairing<>(attributeName, geneValue));
			} else {
				log.warn("Duplicate geneticAttribute key '{}': keeping existing value {} over new value {}", attributeName, existingAttribute.getValue(), geneValue);
			}
		}
		this.geneticAttributes.addAll(uniqueAttributes.values());
		
		this.currentResultSuccessPercentage = currentResultSuccessPercentage;
		this.abilityEnabled = ABILITY_ENABLED;
		this.itemEnabled = ITEM_ENABLED;
		this.userSkillsEnabled = USER_SKILLS_ENABLED;
		this.classEnabled = CLASS_ENABLED;
		this.mapsEnabled = MAPS_ENABLED;
		this.braveFaithEnabled = BRAVE_FAITH_ENABLED;
		this.sideEnabled = SIDE_ENABLED;
		this.numberOfAnalyzedTournaments = numberOfAnalyzedTournaments;
		this.threshold = threshold;
		this.population = population;
		this.maxGene = maxGene;
		this.minGene = minGene;
		this.maxBraveFaithGene = maxBraveFaithGene;
		this.minBraveFaithGene = minBraveFaithGene;
		this.creationDateString = creationDateString;
		this.score = score;
		
		this.percentiles = GenericPairing.convertMapToGenericPairList(percentiles);
	}
	
	public void sortGeneticAttributesByAbsoluteValue() {
		Comparator<GenericPairing<String, Integer>> comparator = new Comparator<GenericPairing<String, Integer>>() {
			@Override
			public int compare(GenericPairing<String, Integer> o1, GenericPairing<String, Integer> o2) {
				Integer o1AbsoluteValue = Math.abs(o1.getValue());
				Integer o2AbsoluteValue = Math.abs(o2.getValue());
				return o1AbsoluteValue.compareTo(o2AbsoluteValue);
			}
			
		};
		Collections.sort(this.geneticAttributes, comparator.reversed());
	}
}
