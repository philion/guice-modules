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
package com.acmerocket.guice.modules.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;


public class AbstractTestModule extends AbstractModule {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private boolean configured = false;

	@Override
	protected void configure() {
		log.debug("Configured {}", this.getClass().getSimpleName());
		this.configured = true;
	}
	
	protected boolean isConfigured() {
		return this.configured;
	}
	
	public String toString() {
		return this.getClass().getSimpleName() + (this.configured ? "*" : "");
	}
}
