package at.bestsolution.wgraf.properties;

public interface Converter<TypeA, TypeB> {
	TypeB convert(TypeA value);
}
