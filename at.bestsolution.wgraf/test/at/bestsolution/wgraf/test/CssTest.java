package at.bestsolution.wgraf.test;

import java.io.InputStreamReader;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;

import com.steadystate.css.parser.CSSOMParser;

public class CssTest {

	public static void main(String[] args) throws Exception {
		
		
		InputSource source = new InputSource(new InputStreamReader(CssTest.class.getResourceAsStream("test.css")));
        
		
		CSSOMParser parser = new CSSOMParser();
        // parse and create a stylesheet composition
        CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);

        CSSRuleList cssRules = stylesheet.getCssRules();
        
        for (int i = 0; i < cssRules.getLength(); i++) {
        	CSSRule item = cssRules.item(i);
        	System.err.println(item);
        	
        	if (item instanceof CSSStyleRule) 
            {
                CSSStyleRule styleRule=(CSSStyleRule)item;
                System.err.println("selector:" + i + ": " + styleRule.getSelectorText());
                CSSStyleDeclaration styleDeclaration = styleRule.getStyle();


                for (int j = 0; j < styleDeclaration.getLength(); j++) 
                {
                     String property = styleDeclaration.item(j);
                     System.err.println("property: " + property);
                     System.err.println("value: " + styleDeclaration.getPropertyCSSValue(property).getCssText());
                     System.err.println("priority: " + styleDeclaration.getPropertyPriority(property));   
                     
                     CSSValue value = styleDeclaration.getPropertyCSSValue(property);
                     System.err.print("ValueType: " + value.getCssValueType() + " = ");
                     switch (value.getCssValueType()) {
                     case CSSValue.CSS_CUSTOM: System.err.println("custom"); break;
                     case CSSValue.CSS_INHERIT: System.err.println("inherit"); break;
                     case CSSValue.CSS_PRIMITIVE_VALUE: System.err.println("primitve_value"); break;
                     case CSSValue.CSS_VALUE_LIST: System.err.println("value_list"); break;
                     default: System.err.println("?");
                     }
                }



             }// end of StyleRule instance test
        	
        }
        
	}

}
