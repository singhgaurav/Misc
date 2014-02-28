/**
 * 
 */
package com.sample;

import org.junit.Before;
import org.junit.Test;

import com.sample.TF;

/**
 * @author gsingh
 *
 */
public class TFTest {
	
	private TF tfs;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tfs = new TF();
	}

	/**
	 * Test method for {@link com.sample.TF#setVersionName(java.lang.String)}.
	 */
	@Test
	public void testGetVersionName() {
		tfs.getVersionName();
	}

	/**
	 * Test method for {@link com.sample.TF#setBranchName(java.lang.String)}.
	 */
	@Test
	public void testGetBranchName() {
	}

}
