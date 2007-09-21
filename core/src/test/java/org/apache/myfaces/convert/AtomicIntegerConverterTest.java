package org.apache.myfaces.convert;

import java.util.concurrent.atomic.AtomicInteger;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import junit.framework.TestCase;

public class AtomicIntegerConverterTest extends TestCase {

	public void testGetAsObject() {
		
		Converter converter = new AtomicIntegerConverter();
		
		assertNull( converter.getAsObject(null, null, null) );
		assertNull( converter.getAsObject(null, null, "") );
		assertNull( converter.getAsObject(null, null, " ") );
		assertTrue( 8 == ((AtomicInteger) converter.getAsObject(null, null, " 8")).intValue() );
		assertTrue( 8 == ((AtomicInteger) converter.getAsObject(null, null, "8 ")).intValue() );
		assertTrue( 8 == ((AtomicInteger) converter.getAsObject(null, null, "8")).intValue() );
		int over = Integer.MAX_VALUE + 1;
		assertTrue( over == ((AtomicInteger) converter.getAsObject(null, null, over + "")).intValue() );
		int under = Integer.MIN_VALUE - 1;
		assertTrue( under == ((AtomicInteger) converter.getAsObject(null, null, under + "")).intValue() );
		
		try {
			
			converter.getAsObject(null, null, "NaN");
			
			fail("should only take numbers");
			
		}catch(ConverterException c) { }
	}
	
	public void testGetAsString() {
		
		Converter converter = new AtomicIntegerConverter();
		
		assertEquals("", converter.getAsString(null, null, null));
		assertEquals("", converter.getAsString(null, null, ""));
		assertEquals(" ", converter.getAsString(null, null, " "));
		assertEquals("-1", converter.getAsString(null, null, new AtomicInteger(-1)));
		
		try {
			
			converter.getAsString(null, null, new Integer(0));
			
			fail("should only take atomic ints");
			
		}catch(ConverterException c) { }		
	}
	
}