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
package org.jboss.hal.testsuite.fragment.finder;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The finder path holds the selection in the finder. It's a collection of segments with each segment holding
 * two values:
 * <ol>
 * <li>{@code columnId} The column id</li>
 * <li>{@code itemId} The selected item id</li>
 * </ol>
 */
public class FinderPath implements Iterable<FinderSegment> {

    /** Separator is used in URL tokens. Please choose a string which is safe to use in URLs */
    private static final String SEPARATOR = "!";


    private final LinkedList<FinderSegment> segments;

    public FinderPath() {
        this(Collections.emptyList());
    }

    public FinderPath(List<FinderSegment> segments) {
        this.segments = new LinkedList<>();
        this.segments.addAll(segments);
    }

    public FinderPath append(String columnId, String itemId) {
        segments.add(new FinderSegment(columnId, itemId));
        return this;
    }

    @Override
    public Iterator<FinderSegment> iterator() {
        return segments.iterator();
    }

    public boolean isEmpty() {
        return segments.isEmpty();
    }

    public int size() {
        return segments.size();
    }

    public String getLastColumnId() {
        if (!isEmpty()) {
            return segments.getLast().columnId;
        }
        return null;
    }

    @Override
    public String toString() {
        return segments.stream()
                .filter(segment -> segment.itemId != null)
                .map(FinderSegment::toString)
                .collect(Collectors.joining(SEPARATOR));
    }
}