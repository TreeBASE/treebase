package org.cipres.treebase.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FlatFileReader<RecordType> implements Iterable<RecordType> {
	Reader source;
	String lineTerminator = "\n";
	StringBuilder buf = new StringBuilder("");
	private boolean atEOF = false;
	int recNumber = 0;
	RecordFactory<RecordType> recordFactory = null;
	
	public FlatFileReader () {	
	}
	
	public FlatFileReader (Reader source) {
		this.source = source;
	}

	public RecordType getRecord() throws IOException {
		String line = nextLine();
		try {
			return line == null ? null : recordFactory.makeRecord(line);
		} catch (RecordIOException e) {
			throw new IOException(e.getMessage() + atLine());
		}
	} 
	
	protected String atLine() {
		return " at record " + getRecNumber();
	}
	

	private int findLineTerminator () {
		return buf.indexOf(lineTerminator);
	}
	
	private boolean hasCompleteLine () {
		return findLineTerminator() != -1;
	}
	
	private boolean readyToYield () {
		return hasCompleteLine() || isAtEOF();
	}
	
	private boolean isAtEOF () {
		return atEOF;
	}
	
	public String nextLine() throws IOException {
		if (buf == null) { return null; }
		
		if (readyToYield()) {
			int lineTermPosition = findLineTerminator();
			String line;
			if (lineTermPosition == -1) {
				assert isAtEOF();
				line = buf.toString();
				if (line.equals("")) {
					line = null;
				}
				buf = null;
			} else {
				line = buf.substring(0, lineTermPosition + 1 - lineTerminator.length());
				buf.delete(0, lineTermPosition + 1);
			}
			recNumber += 1;
			return line;
		}
		
		// We don't have a complete record in the buffer yet
		fillBuffer();
		return nextLine();
	}
	
	protected void fillBuffer () throws IOException {
		char[] cbuf = new char[8192];
		int nr = source.read(cbuf);
		
		if (nr == -1) {
			atEOF = true;
		} else {
			String newChars = new String(cbuf);
			
			buf.append(newChars.substring(0, nr));
		}
	}
	
	public FlatFileIterator iterator() {
		return new FlatFileIterator(this);
	}
	
	class FlatFileIterator implements Iterator<RecordType> {
		FlatFileReader<RecordType> target;
		RecordType nextRec = null;
		
		public FlatFileIterator(FlatFileReader<RecordType> flatFileReader) {
			target = flatFileReader;
		}
		
		private void fillRec() {
			if (nextRec == null) {
				try { nextRec = target.getRecord(); }
				catch (IOException e) {
					throw new NoSuchElementException(e.getMessage());
				}
			}
		}

		public boolean hasNext() {
			fillRec();
			return nextRec != null;		
		}

		public RecordType next() {
			fillRec();
			RecordType nextRec = this.nextRec;
			this.nextRec = null;
			return nextRec;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	
	public Reader getSource() {
		return source;
	}
	public void setSource(Reader source) {
		this.source = source;
	}
	public String getLineTerminator() {
		return lineTerminator;
	}
	public void setLineTerminator(String lineTerminator) {
		this.lineTerminator = lineTerminator;
	}

	/**
	 * @return the recNumber
	 */
	protected int getRecNumber() {
		return recNumber;
	}

	/**
	 * @param recNumber the recNumber to set
	 */
	protected void setRecNumber(int recNumber) {
		this.recNumber = recNumber;
	}

	/**
	 * @return the recordFactory
	 */
	protected RecordFactory<RecordType> getRecordFactory() {
		return recordFactory;
	}

	/**
	 * @param recordFactory the recordFactory to set
	 */
	protected void setRecordFactory(RecordFactory<RecordType> recordFactory) {
		this.recordFactory = recordFactory;
	}
	
	
}