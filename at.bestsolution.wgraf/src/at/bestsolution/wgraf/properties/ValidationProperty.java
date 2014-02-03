package at.bestsolution.wgraf.properties;

public interface ValidationProperty<Type> extends Property<Type> {
	Property<ValidState> valid();
}
