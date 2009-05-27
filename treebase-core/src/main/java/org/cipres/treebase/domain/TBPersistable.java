/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
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

package org.cipres.treebase.domain;

/**
 * A tagging interface for all persisted treebase objects.
 * 
 * Created on Sep 27, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface TBPersistable {

	/**
	 * Default column length for the String type in the database.
	 */
	public static final int COLUMN_LENGTH_30 = 30;
	public static final int COLUMN_LENGTH_50 = 50;
	public static final int COLUMN_LENGTH_100 = 100;
	public static final int COLUMN_LENGTH_STRING = 255;
	public static final int COLUMN_LENGTH_500 = 500;
	public static final int COLUMN_LENGTH_STRING_1K = 1000;
	public static final int COLUMN_LENGTH_STRING_NOTES = 2000;
	public static final int COLUMN_LENGTH_STRING_MAX = 5000;
	
	// Needed to make these bigger.  MJD 20090420
	public static final int CITATION_TITLE_COLUMN_LENGTH = 500;
	public static final int CITATION_ABSTRACT_COLUMN_LENGTH = 10000;

	/**
	 * Return the id.
	 * 
	 * @return
	 */
	public Long getId();

}
