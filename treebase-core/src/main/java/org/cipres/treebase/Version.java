/*
 * CIPRES Copyright (c) 2009, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.cipres.treebase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides version information for TreeBASE.
 * 
 * <p>Version information is loaded from version.properties, which is populated
 * at build time with git commit information.
 * 
 * @author mjd 20090311
 *
 */
public class Version {
	
	private static final String VERSION_PROPERTIES = "/version.properties";
	private static final Properties versionProps = new Properties();
	
	static {
		try (InputStream is = Version.class.getResourceAsStream(VERSION_PROPERTIES)) {
			if (is != null) {
				versionProps.load(is);
			}
		} catch (IOException e) {
			// Ignore - will use fallback values
		}
	}
	
	/**
	 * Git commit ID (short hash or tag).
	 */
	public static final String VCSID = versionProps.getProperty("git.commit.id", "unknown");
	
	/**
	 * Git commit timestamp.
	 */
	public static final String VCSDateString = versionProps.getProperty("git.commit.time", "unknown");
	
	/**
	 * Build timestamp.
	 */
	public static final String BuildTime = versionProps.getProperty("git.build.time", "unknown");
}
