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
package org.graphdht.hashgraph;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.remote.RemoteGraphDatabase;
import org.neo4j.remote.transports.LocalGraphDatabase;

abstract class Base {

    private static final String STORE_DIR = "target/neo";
    private StringBuffer buffer;

    private GraphDatabaseService neo;
    private PrintStream stream;
    private GraphDatabaseService embeddedNeo;

    protected GraphDatabaseService neo() {
        return neo;
    }

    protected void print(Object object) {
        stream.print(object.toString());
    }

    protected void println(Object object) {
        stream.println(object.toString());
    }

    public void setUp() {
        deleteIfPresent(STORE_DIR);
        buffer = new StringBuffer();
        stream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                buffer.append((char) b);
            }
        });
        embeddedNeo = new EmbeddedGraphDatabase(STORE_DIR);
        neo = new RemoteGraphDatabase(new LocalGraphDatabase(embeddedNeo));
    }

    public void tearDown() {
        neo.shutdown();
        embeddedNeo.shutdown();
        markForRemoval(STORE_DIR);
        System.out.print(buffer);
    }

    private void deleteIfPresent(String storeDir) {
        File dir = new File(storeDir);
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
            dir.delete();
        }
    }

    private void markForRemoval(String storeDir) {
        File dir = new File(storeDir);
        if (dir.exists()) {
            dir.deleteOnExit();
            for (File file : dir.listFiles()) {
                file.deleteOnExit();
            }
        }
    }

}
