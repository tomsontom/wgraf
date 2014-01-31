package at.bestsolution.wgraf.properties;

import java.util.List;

public class ValidState {

	private final List<InvalidValueException> problems;
	
	public ValidState(List<InvalidValueException> problems) {
		this.problems = problems;
	}
	
	public boolean isValid() {
		return problems == null || problems.isEmpty();
	}
	
	
	public static final ValidState VALID = new ValidState(null);
}
