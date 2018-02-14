//Connecting database(mongoDB) and Grading songs By --Xiaocase
package songdata;

import com.mongodb.MongoClient;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import com.mongodb.Block;

import static com.mongodb.client.model.Filters.*;

import picture.ProcessPicture;

import java.io.IOException;
import java.util.ArrayList;

public class SongSearcher {
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> collection;
	Song song;
	ProcessPicture pp;
	ArrayList<Song> songs = new ArrayList<Song>();

	// weight
	int emotionweight = 10;
	int PNweight = 30;
	int locationweight = 50;
	int keywordweight = 20;

	// connect to database
	public void connect(String path) throws IOException, InterruptedException {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("songs");
		collection = database.getCollection("song");
		System.out.println("Connecting databas...");
		System.out.println(collection.count() + " labels in database");
		pp = new ProcessPicture(path);
	}

	// Search database by label, return all songs in this label
	public ArrayList<String> search(String l) {
		final ArrayList<String> songname = new ArrayList<>();
		Block<Document> printBlock = new Block<Document>() {
			@Override
			public void apply(final Document document) {
				String data = document.get("songs", String.class);
				int index = data.indexOf(" ");
				while (!(index == -1)) {
					String name = data.substring(0, index + 1).trim();
					data = data.substring(index + 1, data.length()).trim();
					index = data.indexOf(" ");
					songname.add(name);
				}
				if (data != "\r\n")
					songname.add(data);

			}
		};
		collection.find(eq("label", l)).forEach(printBlock);
		return songname;
	}

	// grade songs
	public void gradeSong() {
		System.out.println("Searching labels...");

		int anger = pp.picture.getAnger();
		int joy = pp.picture.getJoy();
		int surprise = pp.picture.getSurprise();
		int sorrow = pp.picture.getSorrow();
		int PositiveorNegative = pp.picture.getPositiveorNegative();
		ArrayList<String> keyword = pp.picture.getKeyword();
		ArrayList<String> temp;

		// add score for songs that in this anger level
		temp = search("anger" + anger);
		while (!temp.isEmpty()) {
			Boolean exist = false;
			String name = temp.remove(0);
			for (Song s : songs) {
				if (s.name == name) {
					s.setScore(s.score + emotionweight);
					exist = true;
				}
			}
			if (!exist) {
				song = new Song(name, emotionweight);
				songs.add(song);
			}
		}
		// add score for songs that in this joy level
		temp = search("joy" + joy);
		while (!temp.isEmpty()) {
			Boolean exist = false;
			String name = temp.remove(0);
			for (Song s : songs) {
				if (s.name == name) {
					s.setScore(s.score + emotionweight);
					exist = true;
				}
			}
			if (!exist) {
				song = new Song(name, emotionweight);
				songs.add(song);
			}
		}
		// add score for songs that in this surprise level
		temp = search("surprise" + surprise);
		while (!temp.isEmpty()) {
			Boolean exist = false;
			String name = temp.remove(0);
			for (Song s : songs) {
				if (s.name == name) {
					s.setScore(s.score + emotionweight);
					exist = true;
				}
			}
			if (!exist) {
				song = new Song(name, emotionweight);
				songs.add(song);
			}
		}
		// add score for songs that in this sorrow level
		temp = search("sorrow" + sorrow);
		while (!temp.isEmpty()) {
			Boolean exist = false;
			String name = temp.remove(0);
			for (Song s : songs) {
				if (s.name == name) {
					s.setScore(s.score + emotionweight);
					exist = true;
				}
			}
			if (!exist) {
				song = new Song(name, emotionweight);
				songs.add(song);
			}
		}
		// add score for songs that in this PositiveorNegative level
		temp = search("PositiveorNegative" + PositiveorNegative);
		while (!temp.isEmpty()) {
			Boolean exist = false;
			String name = temp.remove(0);
			for (Song s : songs) {
				if (s.name == name) {
					s.setScore(s.score + PNweight);
					exist = true;
				}
			}
			if (!exist) {
				song = new Song(name, PNweight);
				songs.add(song);
			}
		}
		// add score for songs that have those keywords
		for (String k : keyword) {
			temp = search(k);
			if (temp.isEmpty())
				continue;
			// if this picture has location information
			if (pp.picture.getHasLocation()) {
				Boolean exist = false;
				String name = temp.remove(0);
				for (Song s : songs) {
					if (s.name == name) {
						s.setScore(s.score + locationweight);
						exist = true;
					}
				}
				if (!exist) {
					song = new Song(name, locationweight);
					songs.add(song);
				}
				pp.picture.setHasLocation(false);
				continue;
			}
			// add score for songs that have this keyword
			while (!temp.isEmpty()) {
				Boolean exist = false;
				String name = temp.remove(0);
				for (Song s : songs) {
					if (s.name == name) {
						s.setScore(s.score + keywordweight);
						exist = true;
					}
				}
				if (!exist) {
					song = new Song(name, keywordweight);
					songs.add(song);
				}
			}
		}
		System.out.println("Grading Songs...");
	}

	// return the top 5 songs
	public String[] findBest() {
		System.out.println("Sorting Songs...");
		String[] sortedsong = new String[5];
		Song max;
		for (int i = 0; i < 5; i++) {
			max = new Song("max", 0);
			for (Song s : songs) {
				if (s.score > max.score) {
					max = s;
				}
			}
			sortedsong[i] = max.name;
			songs.remove(max);
		}
		return sortedsong;
	}

	// show all songs in list
	// test
	public void showAll() {
		for (Song s : songs) {
			System.out.println(s.toString());
		}
	}

	// let's go!!
	public void letsGo() throws IOException, InterruptedException {
		connect("/Users/jiananwen/Desktop/123.jpeg");
		gradeSong();
		// showAll();
		String[] result = findBest();
		System.out.print("Recommend Songs:");
		System.out.println(result[0] + " " + result[1] + " " + result[2] + " " + result[3] + " " + result[4]);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		SongSearcher ss = new SongSearcher();
//		ss.letsGo();
		String image = args[0];
		ss.connect(image);
		ss.gradeSong();
		String[] result = ss.findBest();
		System.out.print("Recommend Songs:");
		System.out.println(result[0] + " " + result[1] + " " + result[2] + " " + result[3] + " " + result[4]);
	
		
	}
}
