//class for song By --Xiaocase
package songdata;

public class Song {
	public String name;
	public int score;

	public Song(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "Song [name=" + name + ", score=" + score + "]";
	}
	
}
