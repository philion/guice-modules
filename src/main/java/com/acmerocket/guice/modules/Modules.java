/**
 * Copyright 2014 Acme Rocket Company [acmerocket.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acmerocket.guice.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Modules {
	static final String DEFAULT_MODULE = "*default*";

	String[] value() default DEFAULT_MODULE;
	
	public static class Builder {
	    private static final Logger LOG = LoggerFactory.getLogger(Modules.Builder.class);

		public static Builder packages(String... basePackages) {
			LOG.debug("Adding basePackages: {}", basePackages);
			return new Builder(Arrays.asList(basePackages));
		}
		
		public static Builder packages(Iterable<String> basePackages) {
			LOG.debug("Adding basePackages: {}", basePackages);
			return new Builder(basePackages);
		}
		
		private final Reflections reflections;
		
		Builder(Iterable<String> basePackages) {			
			ConfigurationBuilder cfgBldr = new ConfigurationBuilder();
			FilterBuilder filterBuilder = new FilterBuilder();
			for (String basePkg : basePackages) {
				cfgBldr.addUrls(ClasspathHelper.forPackage(basePkg));
				filterBuilder.include(FilterBuilder.prefix(basePkg));
			}
			cfgBldr.filterInputsBy(filterBuilder).setScanners(new SubTypesScanner(), new TypeAnnotationsScanner());

			this.reflections = new Reflections(cfgBldr);			
		}
		
		public Iterable<? extends Module> build(String... moduleNames) {
			return this.build(Arrays.asList(moduleNames));
		}
		
		public Iterable<? extends Module> build(Iterable<String> moduleNames) {
			ArrayList<Module> modules = new ArrayList<>();
			
			for (Class<?> clazz : this.reflections.getTypesAnnotatedWith(Modules.class)) {
				if (this.matches(clazz, moduleNames)) {
					modules.add(this.create(clazz));
				}
			}
			
			return modules;
		}
		
		private Module create(Class<?> clazz) {
			try {
				return (Module) clazz.newInstance();
			} 
			catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		
		private boolean matches(Class<?> clazz, Iterable<String> moduleNames) {
			Modules annotation = clazz.getAnnotation(Modules.class);
			if (annotation == null) {
				return false;
			}

			String[] annoNames = annotation.value();
			if (annoNames.length == 1 && DEFAULT_MODULE.equals(annoNames[0])) {
				LOG.debug("Always loading {}", clazz.getSimpleName());
				return true;
			}
			
			for (String moduleName : moduleNames) {
				for (String match : annoNames) {
					if (match.equals(moduleName)) {
						LOG.debug("Found match: {} -> {}", clazz.getSimpleName(), match);
						return true;
					}
				}
			}
			
			return false;
		}
	}
}
