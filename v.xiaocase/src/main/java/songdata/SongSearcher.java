//Connecting database(mongoDB) and Grading songs By --Xiaocase
package songdata;

import com.mongodb.MongoClient;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import com.mongodb.Block;

import static com.mongodb.client.model.Filters.*;

import picture.ProcessPicture;
import youtube.SearchLink;

import java.io.IOException;
import java.util.ArrayList;

public class SongSearcher {
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> collection;
	Song song;
	ProcessPicture pp;
	static ArrayList<Song> songs = new ArrayList<Song>();
	
	//weight
	int emotionweight = 1000;
	int PNweight = 100;
	int locationweight = 10000;
	int nameweight = 10000;
	int keywordweight = 10;
	
	//connect to database
	public void connect() throws IOException, InterruptedException{
		mongoClient = new MongoClient("localhost",27017);
		database = mongoClient.getDatabase("songs");
		collection = database.getCollection("song") ;
		System.out.println("Connecting databas...");
		System.out.println(collection.count()+ " labels in database");
	}
	
	//Search database by label, return all songs in this label
	public ArrayList<String> search(String l){
		ArrayList<String> songname = new ArrayList<String>();
		Block<Document> printBlock = new Block<Document>() {
		     @Override
		     public void apply(final Document document) {
		    	 String data = document.get("songs", String.class);
		    	 String[] song=data.split("/");	
		    	 for(String s: song){
		    	 songname.add(s);
		    	 }
		     }
		};
		collection.find(eq("label", l)).forEach(printBlock);
		return songname;
	}
	
	//grade songs
	public void gradeSong(){
		System.out.println("Searching labels...");
		
		int anger = pp.picture.getAnger();
		int joy = pp.picture.getJoy();
		int surprise = pp.picture.getSurprise();
		int sorrow = pp.picture.getSorrow();
		int PositiveorNegative = pp.picture.getPositiveorNegative();
		ArrayList<String> keyword=pp.picture.getKeyword();
		ArrayList<String> temp;
		
		//add score for songs that in this anger level
		temp=search("anger"+anger);
		while(!temp.isEmpty()){
			String name =temp.remove(0).trim();
			song=new Song(name,emotionweight);
			songs.add(song);
		}
		//add score for songs that in this joy level
		temp=search("joy"+joy);
		while(!temp.isEmpty()){
			String name =temp.remove(0).trim();
			song=new Song(name,emotionweight);
			songs.add(song);
		}
		//add score for songs that in this surprise level
		temp=search("surprise"+surprise);
		while(!temp.isEmpty()){
			String name =temp.remove(0).trim();
			song=new Song(name,emotionweight);
			songs.add(song);
		}	
		//add score for songs that in this sorrow level
		temp=search("sorrow"+sorrow);
		while(!temp.isEmpty()){
			String name =temp.remove(0).trim();
			song=new Song(name,emotionweight);
			songs.add(song);
		}
		//add score for songs that in this PositiveorNegative level
		temp=search("positiveornegative"+PositiveorNegative);
		while(!temp.isEmpty()){
			String name =temp.remove(0).trim();
			song=new Song(name,PNweight);
			songs.add(song);
		}		
		//add score for songs that have those keywords
		int weight =keywordweight/keyword.size();
		for(String k:keyword){
			temp=search(k);
			if(temp.isEmpty()) continue;
			//if this picture has location information
			if(pp.picture.getHasLocation()){
				while(!temp.isEmpty()){
					String name =temp.remove(0).trim();
					song=new Song(name,locationweight);
					songs.add(song);
				}
				pp.picture.setHasLocation(false);
				continue;
			}
			//add score for songs that have this keyword
			while(!temp.isEmpty()){
				String name =temp.remove(0).trim();
				song=new Song(name,weight);
				songs.add(song);
			}
		}
		//add score for songs that have the first 3 keywords in song name 
		int count=1;
		for(String k: keyword){
			temp=search("SooooooooogName>"+k);
			while(!temp.isEmpty()){
				String name =temp.remove(0).trim();
				if(count==1)song=new Song(name,nameweight);
				else song=new Song(name,nameweight/2);
				songs.add(song);
			}
			count++;
			if(count>3) break;
		}
		System.out.println("Grading Songs...");
	}
	
	//return the top 5 songs
	public ArrayList<String> findBest(){
		System.out.println("Sorting Songs...");
		//Combine the same songs
		for(int i=0;i<songs.size();i++){
			for(int j=i+1;j<songs.size();j++){
				if(songs.get(i).name.equals(songs.get(j).name)){
					songs.get(i).setScore(songs.get(i).score+songs.remove(j).score);
				}
			}
		}
		
		//find the best five
		ArrayList<String> sortedsong = new ArrayList<>();
		Song max;
		int count=0;
		while(count<5){
			max = new Song("max",0);
			if(count<2){
				for(Song s: songs){
					if(s.score>max.score) {
						max = s;
					}
				}
			}
			else if(count<4){
				for(Song s: songs){
					if(s.score<10000&&s.score>max.score) {
						max = s;
					}
				}
			}
			else{
				for(Song s: songs){
					if(s.score<1000&&s.score>max.score) {
						max = s;
					}
				}
			}
			boolean hasSame = false;
			for(String s: sortedsong){
				if(s.split("_")[1].equals(max.name.split("_")[1])){
					hasSame = true;
					break;
				}
			}
			if(!hasSame){
				sortedsong.add(max.name);
				count++;
			}
			songs.remove(max);
		}
		return sortedsong;
	}
	
	//show all songs in list
	//test
	public void showAll(){
		for(Song s: songs){
			System.out.println(s.toString());
		}
	}
	
	//let's go!!
	public void letsGo(String path) throws IOException, InterruptedException {
		connect();
		pp = new ProcessPicture(path);
		gradeSong();
		// showAll();
		ArrayList<String> result = findBest();
		System.out.println("Recommend Songs:");
		SearchLink sl = new SearchLink();
		String r0 = format(result.get(0));
		String r1 = format(result.get(1));
		String r2 = format(result.get(2));
		String r3 = format(result.get(3));
		String r4 = format(result.get(4));

		System.out.println("%" + r0 + "," + sl.getSongLink(r0));
		System.out.println("%" + r1 + "," + sl.getSongLink(r1));
		System.out.println("%" + r2 + "," + sl.getSongLink(r2));
		System.out.println("%" + r3+ "," + sl.getSongLink(r3));
		System.out.println("%" + r4 + "," + sl.getSongLink(r4));
	}
	
	public String format(String songname){
		String song = songname.replace("_", ",");
		return song;
	}
 	public static void main(String[] args) throws IOException, InterruptedException{
 		SongSearcher ss = new SongSearcher();
 		ss.letsGo("C:/Users/XiaoCase/Desktop/image/webwxgetmsgimg (5).jpg");
	}
}
