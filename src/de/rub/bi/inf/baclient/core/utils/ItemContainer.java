package de.rub.bi.inf.baclient.core.utils;

/**
 * A container class for wrapping Ifc-Types with a specific displayable text. 
 * Mainly needed for passing values and displaying own context. 
 * 
 * @author Marcel Stepien
 *
 * @param <T>
 */
public class ItemContainer<T> implements Comparable<String> {
	
	private T item = null;
	private String display = null;
	
	public ItemContainer(T item, String display) {
		this.item = item;
		this.display = display;
	}

	public final T getItem() {
		return item;
	}
	
	public final String getDisplayText() {
		return display;
	}
	
	@Override
	public String toString() {
		return display;
	}

	@Override
	public int compareTo(String o) {
		return display.compareTo(o);
	}
	
	@Override
	public boolean equals(Object obj) {
		return display.equals(obj.toString());
	}
	
	@Override
	public int hashCode() {
		return display.hashCode();
	}
	
}
