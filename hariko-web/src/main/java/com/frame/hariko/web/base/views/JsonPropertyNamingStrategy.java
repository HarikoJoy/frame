package com.frame.hariko.web.base.views;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

public class JsonPropertyNamingStrategy extends PropertyNamingStrategy {

	private static final long serialVersionUID = 1507763000511320283L;

	@Override
	public String nameForGetterMethod(MapperConfig<?> config,
			AnnotatedMethod method, String defaultName) {
		if (method.getName().startsWith("is")) {
			return method.getName();
		}

		return defaultName;
	}

}
