package at.bestsolution.wgraf.properties.binding;

public interface BindingConverter<A, B> {
	
	B convert(A value) throws ConversionException;

}
