/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.testsuite.arquillian;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.enricher.AbstractSearchContextEnricher;
import org.jboss.arquillian.graphene.enricher.ReflectionHelper;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.SearchContext;

/** Injects an instance of {@link Console} into test classes, pages or page fragments. */
public class ConsoleEnricher extends AbstractSearchContextEnricher {

    @Override
    public void enrich(SearchContext searchContext, Object target) {
        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(target.getClass(), Inject.class);
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(Console.class)) {
                Console console = new Console();
                enrichRecursively(searchContext, console);
                setValue(field, target, console);
            }
        }
    }

    @Override
    public Object[] resolve(SearchContext searchContext, Method method, Object[] resolvedParams) {
        return resolvedParams;
    }

    @Override
    public int getPrecedence() {
        return 1;
    }
}
