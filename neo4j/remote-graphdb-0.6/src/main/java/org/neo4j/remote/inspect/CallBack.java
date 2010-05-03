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
package org.neo4j.remote.inspect;

/**
 * A callback for an event in the debugger.
 * @author Tobias Ivarsson
 * @param <T>
 *            The result type of the event call.
 */
public interface CallBack<T>
{
    /**
     * Called if the event was successful.
     * @param result
     *            the result of the call.
     */
    void success( T result );

    /**
     * Called if the event failed.
     * @param ex
     *            The exception that the event failed with.
     */
    void failure( Throwable ex );
}
