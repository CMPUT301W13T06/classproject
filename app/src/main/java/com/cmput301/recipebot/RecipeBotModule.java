/*
 * Copyright 2013 Adam Saturna
 * Copyright 2013 Brian Trinh
 * Copyright 2013 Ethan Mykytiuk
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Copyright 2012 Donn Felker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cmput301.recipebot;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.squareup.otto.Bus;

/**
 * Module for setting up custom bindings in RoboGuice.
 */
public class RecipeBotModule extends AbstractModule {

    /**
     * Contributes bindings and other configurations for this module to binder.
     */
    @Override
    protected void configure() {

        // We want Otto to be bound as a singleton as one instance only needs
        // to be present in this app
        bind(Bus.class).in(Singleton.class);

    }

}
