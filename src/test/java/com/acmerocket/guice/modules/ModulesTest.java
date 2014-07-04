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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmerocket.guice.modules.test.ModuleA;
import com.acmerocket.guice.modules.test.ModuleB;
import com.acmerocket.guice.modules.test.ModuleC;
import com.acmerocket.guice.modules.test.ModuleD;
import com.acmerocket.guice.modules.test.sub.ModuleE;
import com.acmerocket.guice.modules.test.sub.ModuleF;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Module;

public class ModulesTest {
    final Logger log = LoggerFactory.getLogger(this.getClass());

	@Test
	public void testUnknownModule() {
		Iterable<? extends Module> modules = Modules.Builder.packages("com.acmerocket.guice.modules").build("yoyo");
		Set<?> classes = classSet(modules);
		assertEquals(1, classes.size());
		assertTrue(classes.contains(ModuleA.class));
	}
	
	@Test
	public void testModuleOne() {
		Iterable<? extends Module> modules = Modules.Builder.packages("com.acmerocket.guice.modules").build("module-one");		
		Set<?> classes = classSet(modules);
		//log.info("Classes: {}", classes);
		assertEquals(3, classes.size());
		assertTrue(classes.contains(ModuleA.class));
		assertTrue(classes.contains(ModuleB.class));
		assertTrue(classes.contains(ModuleC.class));
	}
	
	@Test
	public void testModuleTwo() {
		Iterable<? extends Module> modules = Modules.Builder.packages("com.acmerocket.guice.modules").build("module-two");
		Set<?> classes = classSet(modules);
		//log.info("Classes: {}", classes);
		assertEquals(4, classes.size());
		assertTrue(classes.contains(ModuleA.class));
		assertTrue(classes.contains(ModuleC.class));
		assertTrue(classes.contains(ModuleD.class));
		assertTrue(classes.contains(ModuleE.class));
	}
	
	@Test
	public void testModuleThree() {
		Iterable<? extends Module> modules = Modules.Builder.packages("com.acmerocket.guice.modules").build("module-three");
		Set<?> classes = classSet(modules);
		//log.info("Classes: {}", classes);
		assertEquals(3, classes.size());
		assertTrue(classes.contains(ModuleA.class));
		assertTrue(classes.contains(ModuleD.class));
		assertTrue(classes.contains(ModuleF.class));
	}
	
	private static Set<Class<? extends Module>> classSet(Iterable<? extends Module> modules) {
		return Sets.newHashSet(Iterables.transform(modules, classesFor));
	}
	private static final Function<Module, Class<? extends Module>> classesFor = new Function<Module, Class<? extends Module>>() {
		@Override
		public Class<? extends Module> apply(Module input) {
			return input.getClass();
		}
	};
}
