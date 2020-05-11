package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.json.JsonPropertyAccessor;

/**
 * @author pilak
 *
 */
public class TestJsonAccessing {

	private static final String COMMON_SPEL_EXPRESSION = "property.^[name == 'value1'].name";

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		StandardEvaluationContext context = new StandardEvaluationContext();
		SpelExpressionParser parser = new SpelExpressionParser();

		Example example = new Example();
		JavaBean javaBean1 = new JavaBean();
		javaBean1.setName("value1");
		JavaBean javaBean2 = new JavaBean();
		javaBean2.setName("value2");
		example.setProperty(new JavaBean[] { javaBean1, javaBean2 });

		context.setPropertyAccessors(List.of(new ReflectivePropertyAccessor()));
		context.setRootObject(example);

		System.out.println("Example with JavaBean");
		System.out.println(parser.parseExpression(COMMON_SPEL_EXPRESSION).getValue(context));

		List<Map<String, Object>> property = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		HashMap<String, Object> value1 = new HashMap<>();
		HashMap<String, Object> value2 = new HashMap<>();

		map.put("property", property);

		property.add(value1);
		property.add(value2);

		value1.put("name", "value1");
		value2.put("name", "value2");

		context.setPropertyAccessors(List.of(new MapAccessor()));
		context.setRootObject(map);

		System.out.println("Example with Map");
		System.out.println(parser.parseExpression(COMMON_SPEL_EXPRESSION).getValue(context));
		
		context.setPropertyAccessors(List.of(new JsonPropertyAccessor()));
		context.setRootObject(
				new ObjectMapper().readTree("{\"property\":[{\"name\":\"value1\"},{\"name\":\"value2\"}]}"));


		System.out.println("Example with JsonNode");
		System.out.println(parser.parseExpression(COMMON_SPEL_EXPRESSION).getValue(context));

	}

	static class Example {

		private JavaBean[] property;

		public JavaBean[] getProperty() {
			return property;
		}

		public void setProperty(JavaBean[] property) {
			this.property = property;
		}

	}

	static class JavaBean {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}