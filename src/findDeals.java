import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * Timothy Carta
 * SER305
 * 12/4/2020
 * 
 * The findDeals class is used to gather deals from keywords provided by the user.
 * The user will provide a list of keywords as well as a frequency (in minutes) to look for new deals.
 * These deals will be displayed to the user in an organized and visually appealing way.
 * 
 * Time Complexity: The overall time complexity is O(n*m) where n is the number of elements on the webpage and m is the number of
 * keywords entered by the user. This is the overall time complexity of the class because of the methods within the class this is the worst complexity.
 * 
 * Space Complexity: The space complexity is O(n) where n is the amount of deals. Once the deals are found 
 * the extra space used, then used to display the deals as well. In the worst case all of the keywords that the user inputs will be 
 * found causing for the most space possible but still falling under O(n).
 * 
 */

public class findDeals {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("How many keywords will you be searching for?");
		int num = in.nextInt(); //get how many keywords the user wants to search for
		in.nextLine(); //remove the blank space at the end of the line
		
		String[] keywords = new String[num]; //initialize the array to the size of the number of keywords
		
		//Get the keywords from the user
		for (int i = 0; i < num; i++) {
			System.out.println("What is your next keyword?");
			keywords[i] = in.nextLine().toLowerCase();
		}
		System.out.println("How often (in minutes) would you like to search for deals?");
		int time = in.nextInt();
		in.close();
		//Make a timer that searches every given interval for the keywords and displays any found to the user.
		while (true) {
			try {
				TimeUnit.MINUTES.sleep(time);
				displayItems(grabItems(keywords));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The grabItems method takes an array of keywords
	 * and is used to search through dealsea.com and find non-expired items.
	 * the method returns a linked list with all valid items.
	 * 
	 * Time Complexity:O(n*m) where n is the number of elements on the webpage and m is the number of keywords provided by the user.
	 * Assuming Jsoup returning information takes O(n) time where n is the number of elements on the webpage
	 * parsing the webpage takes O(n) time because you must go through every element on the page. 
	 * Every item in the items will be compared to m keywords provided by the user.
	 * 
	 * Space Complexity: O(n) Where n is the number of elements on the webpage
	 * In the worst case every element will be considered an item which means that every item will be appended to the foundItems
	 * linkedlist. 
	 */
	public static LinkedList grabItems(String[] keywords) {
		Document doc;
		LinkedList foundItems = new LinkedList();
		try {
			doc = Jsoup.connect("https://dealsea.com/").get();
			Elements items = doc.getElementsByClass("dealcontent");
			String[] titles = new String[items.size()];
			for (Element item : items) {//Get every item in the front page.
				String title = item.getElementsByTag("strong").select("a").text(); //Get the title of the item
				String expired = item.getElementsByClass("colr_red").text(); //Get the expired status of the item	
				
				//Go through all of the users keywords.
				for (String keyword: keywords) {
					if (!expired.contains("Expired") && title.toLowerCase().contains(keyword)) {
						//place the item in the linked list because it matches the keywords and is not expired
						foundItems.add(title); 
					}
				}
			}
			return foundItems;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The displayItems method will take a linked list and display all items to the user
	 * in a visually appealing way.
	 * 
	 * Time Complexity: O(n) where n is the size of the items linked list. It takes O(n) time to iterate through the 
	 * list, and all other operations are O(1).
	 * 
	 * Space Complexity: O(1) because every extra space used is constant size. At worst the String created is the length of the amount of keywords
	 * entered by the user, but the String is still O(1) space.
	 * 
	 */
	public static void displayItems(LinkedList items) {
		//TODO Go through each element in the linked list and somehow display it to the user.
		final JFrame parent = new JFrame();
		parent.pack();
		String deals = "These Deals Are Available:\n\n";
		for(int i = 0; i < items.size(); i ++){
			deals = deals + items.get(i) + "\n\n";
		}
		JOptionPane.showMessageDialog(parent, deals);
		parent.setVisible(true);
	}

}
