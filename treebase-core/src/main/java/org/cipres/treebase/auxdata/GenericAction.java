


package org.cipres.treebase.auxdata;

/* Action(f) is a filter object that has the same side effects as f,
 * but that returns its argument unchanged.
 */
public class GenericAction implements Action {
	boolean disabled = false;
	Filter f;
	public GenericAction() { }
	GenericAction(Filter filter) { f = filter; }
	public Value perform(Value v) {
		if (isDisabled()) return v;
		f.perform(v);
		return v;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean d) {
		disabled = d;
	}
}
