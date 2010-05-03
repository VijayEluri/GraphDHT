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
package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.remote.RemoteGraphDatabase;
import org.neo4j.remote.transports.LocalGraphDatabase;
import org.neo4j.remote.transports.RmiTransport;

public class BasicTest
{
	private static final String RMI_RESOURCE = "rmi://localhost/"
		+ BasicTest.class.getSimpleName();
	private static GraphDatabaseService neo;
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		boolean rmi = true;
		try
		{
			LocateRegistry.createRegistry( Registry.REGISTRY_PORT );
		}
		catch ( RemoteException e )
		{
			e.printStackTrace();
			rmi = false;
		}
		Exception onRegister = null;
		try
		{
			RmiTransport.register( new LocalGraphDatabase( "target/neo" ), RMI_RESOURCE );
		}
		catch (Exception ex)
		{
			onRegister = ex;
		}
		try
		{
			neo = new RemoteGraphDatabase( RMI_RESOURCE );
		}
		catch (Exception ex)
		{
			if ( rmi )
			{
				if ( onRegister != null )
				{
					throw onRegister;
				}
				else
				{
					throw ex;
				}
			}
			else
			{
				neo = null;
			}
		}
	}
	
	private void transactional(Runnable body)
	{
		Assume.assumeNotNull(neo);
		Transaction tx = neo.beginTx();
		try
		{
			body.run();
			tx.success();
		}
		finally
		{
			tx.finish();
		}
	}
	
	@Test
	public void testGetReferenceNode() throws Exception
	{
		transactional(new Runnable()
		{
			public void run()
			{
				Assert.assertNotNull(neo.getReferenceNode());
			}
		});
	}

	@Test
	public void testCreateNode() throws Exception {
		transactional(new Runnable()
		{
			public void run()
			{
				Assert.assertNotNull(neo.createNode());
			}
		});
	}
	
	private enum TestType implements RelationshipType
	{
		TEST
	}
	
	@Test
	public void testCreateRelationship() throws Exception {
		transactional(new Runnable()
		{
			public void run()
			{
				Node start = neo.createNode();
				Node end = neo.createNode();
				Relationship relationship = start.createRelationshipTo(
						end, TestType.TEST);
				Assert.assertNotNull(relationship);
				Assert.assertTrue(relationship.isType(TestType.TEST));
				Assert.assertEquals(start, relationship.getStartNode());
				Assert.assertEquals(end, relationship.getEndNode());
				Assert.assertEquals(start, relationship.getOtherNode(end));
				Assert.assertEquals(end, relationship.getOtherNode(start));
				Assert.assertTrue(Arrays.equals(new Node[] { start, end },
						relationship.getNodes()));
			}
		});
	}

	@Test
	public void testNodeProperties() throws Exception {
		transactional(new Runnable()
		{
			public void run()
			{
				properties(neo.createNode());
			}
		});
	}

	@Test
	public void testRelationshipProperties() throws Exception {
		transactional(new Runnable()
		{
			public void run()
			{
				properties(neo.createNode().createRelationshipTo(
						neo.createNode(), TestType.TEST));
			}
		});
	}
	
	private static void properties(PropertyContainer container) {
		container.setProperty("Key", "Value");
		container.setProperty("Keys", new String[] { "value1", "value2" });
		container.setProperty("int", 4);
		container.setProperty("long", 4L);
		container.setProperty("float", (float)4.0);
		container.setProperty("double", (double)4.0);
		container.setProperty("shorts", new short[] { 1, 2, 3, 4 });
	}
}
