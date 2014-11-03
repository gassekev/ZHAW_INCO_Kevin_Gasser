package zhaw;

public class FoundStringEncData implements Comparable< FoundStringEncData>{
	public int offset;
	public int charsIn;
	public char nextChar;
	public FoundStringEncData() {
		offset = 0; charsIn = 0;
	}
	public FoundStringEncData( int o, int cIn, char nCh) {
		offset = o; charsIn = cIn; nextChar = nCh;
	}
	@Override
	public int compareTo(FoundStringEncData o) {
		return this.charsIn < o.charsIn ? -1 : (this.charsIn > o.charsIn ? 1 : 0);
	}
	boolean isEmpty() {
		return charsIn <= 0;
	}
}


