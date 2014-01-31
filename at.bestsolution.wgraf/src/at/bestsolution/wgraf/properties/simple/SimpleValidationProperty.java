package at.bestsolution.wgraf.properties.simple;

import java.util.ArrayList;
import java.util.List;

import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.InvalidValueException;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ValidState;
import at.bestsolution.wgraf.properties.ValidationProperty;

public class SimpleValidationProperty<Type> extends SimpleProperty<Type> implements ValidationProperty<Type> {
	
	public SimpleValidationProperty() {
		super();
	}

	public SimpleValidationProperty(Type initialValue) {
		super(initialValue);
	}

	private Property<ValidState> valid = new SimpleProperty<ValidState>(ValidState.VALID);
	@Override
	public Property<ValidState> valid() {
		return valid;
	}
	
	protected void notify(Type oldValue, Type newValue) {
		if (oldValue != newValue) {
			List<InvalidValueException> problems = new ArrayList<InvalidValueException>();
			for (ChangeListener<Type> listener : listeners) {
				try {
					notifyListener(listener, oldValue, newValue);
				}
				catch (InvalidValueException e) {
					problems.add(e);
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (problems.isEmpty()) {
				// OK
				valid.set(ValidState.VALID);
			}
			else {
				// Handle invalid value
				valid.set(new ValidState(problems));
			}
		}
	}
	
}
