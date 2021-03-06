/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tajo.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tajo.datum.Datum;
import tajo.datum.DatumFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestFrameTuple {
  private Tuple tuple1;
  private Tuple tuple2;

  @Before
  public void setUp() throws Exception {
    tuple1 = new VTuple(11);
    tuple1.put(new Datum[] {
        DatumFactory.createBool(true),
        DatumFactory.createByte((byte) 0x99),
        DatumFactory.createChar('9'),
        DatumFactory.createShort((short) 17),
        DatumFactory.createInt(59),
        DatumFactory.createLong(23l),
        DatumFactory.createFloat(77.9f),
        DatumFactory.createDouble(271.9f),        
        DatumFactory.createString("hyunsik"),
        DatumFactory.createBytes("hyunsik".getBytes()),
        DatumFactory.createIPv4("192.168.0.1")
    });
    
    tuple2 = new VTuple(11);
    tuple2.put(new Datum[] {
        DatumFactory.createBool(true),
        DatumFactory.createByte((byte) 0x99),
        DatumFactory.createChar('9'),
        DatumFactory.createShort((short) 17),
        DatumFactory.createInt(59),
        DatumFactory.createLong(23l),
        DatumFactory.createFloat(77.9f),
        DatumFactory.createDouble(271.9f),        
        DatumFactory.createString("hyunsik"),
        DatumFactory.createBytes("hyunsik".getBytes()),
        DatumFactory.createIPv4("192.168.0.1")
    });
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public final void testFrameTuple() {
    Tuple frame = new FrameTuple(tuple1, tuple2);
    assertEquals(22, frame.size());
    for (int i = 0; i < 22; i++) {
      assertTrue(frame.contains(i));
    }
    
    assertEquals(DatumFactory.createLong(23l), frame.get(5));
    assertEquals(DatumFactory.createLong(23l), frame.get(16));
    assertEquals(DatumFactory.createIPv4("192.168.0.1"), frame.get(10));
    assertEquals(DatumFactory.createIPv4("192.168.0.1"), frame.get(21));
  }
}
