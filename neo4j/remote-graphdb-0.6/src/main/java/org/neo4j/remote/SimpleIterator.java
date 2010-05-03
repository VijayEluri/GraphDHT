/*
 * Copyright (c) 2008-2009 "Neo Technology,"
 *     Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 * 
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.remote;

abstract class SimpleIterator<T>
{
    private int count = 0;
    private final int size;

    SimpleIterator( int size )
    {
        this.size = size;
    }

    SimpleIterator()
    {
        this.size = -1;
    }

    final int size()
    {
        return this.size;
    }

    final int count()
    {
        return count;
    }

    abstract boolean hasNext();

    abstract T getNext();

    final T next()
    {
        count++;
        return getNext();
    }
}
