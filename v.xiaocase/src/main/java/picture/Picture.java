//class for picture --By XiaoCase
package picture;

import java.util.ArrayList;

public class Picture {
	int anger;
	int joy;
	int surprise;
	int sorrow;
	int PositiveorNegative;
	ArrayList<String> keyword;
	Boolean hasLocation;		//if have location information
	
	public Picture(int anger, int joy, int surprise, int sorrow, int positiveorNegative, ArrayList<String> keyword,Boolean hasLocation) {
		this.anger = anger;
		this.joy = joy;
		this.surprise = surprise;
		this.sorrow = sorrow;
		PositiveorNegative = positiveorNegative;
		this.keyword = keyword;
		this.hasLocation=hasLocation;
	}

	public int getAnger() {
		return anger;
	}

	public void setAnger(int anger) {
		this.anger = anger;
	}

	public int getJoy() {
		return joy;
	}

	public void setJoy(int joy) {
		this.joy = joy;
	}

	public int getSurprise() {
		return surprise;
	}

	public void setSurprise(int surprise) {
		this.surprise = surprise;
	}

	public int getSorrow() {
		return sorrow;
	}

	public void setSorrow(int sorrow) {
		this.sorrow = sorrow;
	}

	public int getPositiveorNegative() {
		return PositiveorNegative;
	}

	public void setPositiveorNegative(int positiveorNegative) {
		PositiveorNegative = positiveorNegative;
	}

	public ArrayList<String> getKeyword() {
		return keyword;
	}

	public void setKeyword(ArrayList<String> keyword) {
		this.keyword = keyword;
	}
	
	public Boolean getHasLocation() {
		return hasLocation;
	}

	public void setHasLocation(Boolean hasLocation) {
		this.hasLocation = hasLocation;
	}

	@Override
	public String toString() {
		return "Picture [anger=" + anger + ", joy=" + joy + ", surprise=" + surprise + ", sorrow=" + sorrow
				+ ", PositiveorNegative=" + PositiveorNegative + ", keyword=" + keyword + ", hasLocation=" + hasLocation
				+ "]";
	}	
}
