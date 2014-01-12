package thevoz.ch;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SearchMain {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Read entreprises...");
		List<IndexName> entrepriseIndex = readFile("entreprise.txt");		
		System.out.println("Read persons...");
		List<IndexName> personIndex = readFile("person.txt");
		
		if(entrepriseIndex != null && personIndex != null)
		{
			try {
				SoapServer service;
				do
				{
					service = getService(sc);
				} while(service == null);
				
				String option;
				do
				{
					option = getOptionMenu(sc);
					if(option.equals("1"))
					{
						System.out.println("Veuillez saisir le nom d'une entreprise :");
						subMenu(sc, entrepriseIndex, service);
					}
					else if(option.equals("2"))
					{
						System.out.println("Veuillez saisir le nom d'une personne :");
						subMenu(sc, personIndex, service);
					}
				} while(!option.equals("0"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Fault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Bye");
	}
	
	static class IndexName
	{
		private String name;
		private List<NameArticle> nameArticles = new ArrayList<NameArticle>();
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public List<NameArticle> getNameArticles() {
			return nameArticles;
		}
		
		public NameArticle getAllArticles() {
			Set<Long> articles = new HashSet<Long>();
			for(NameArticle na : nameArticles) {
				for(Long article : na.getArticles()) {
					articles.add(article);
				}
			}
			
			NameArticle result = new NameArticle();
			result.setName(name);
			for(Long article : articles) {
				result.getArticles().add(article);
			}
			return result;
		}
		
		public String toString() {
			return name;
		}
	}
	
	static class NameArticle
	{
		private String name;
		private List<Long> articles = new ArrayList<Long>();
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public List<Long> getArticles() {
			return articles;
		}
		
		public String toString() {
			return name;
		}
	}
	
	private static void subMenu(Scanner sc, List<IndexName> indexes, SoapServer service) throws Fault, IOException
	{
		String name = sc.nextLine();
		List<IndexName> filter = new ArrayList<IndexName>();
		for(IndexName index : indexes) {
			if(index.getName().contains(name))
				filter.add(index);
		}
		
		int selection;
		do
		{
			selection = getSelectionInList(filter, sc);
			if(selection > 0) {
				String option;
				do
				{
					IndexName index = filter.get(selection - 1);
					System.out.println(index.getName());
					System.out.println("0. Retour");
					System.out.println("1. Liste des noms");
					System.out.println("2. Liste des articles");
					option = sc.nextLine();
					if(option.equals("1")) {					
						selectName(sc, service, index);
					} else if(option.equals("2")){
						
						selectArticle(sc, service, index.getAllArticles());
					}	
				} while(!option.equals("0"));
			}
		} while(selection != 0);
	}


	private static void selectName(Scanner sc, SoapServer service,
			IndexName index) throws Fault, IOException {
		int nameSelection;
		do
		{
			nameSelection = getSelectionInList(index.getNameArticles(), sc);
			if(nameSelection > 0) {
				NameArticle na = index.getNameArticles().get(nameSelection - 1);
				selectArticle(sc, service, na);
			}
		} while(nameSelection != 0);
	}


	private static void selectArticle(Scanner sc, SoapServer service,
			NameArticle na) throws Fault, IOException {
		int articleSelection;
		do
		{
			System.out.println(na.getName());
			articleSelection = getSelectionInList(na.getArticles(), sc);
			if(articleSelection > 0) {
				Long article = na.getArticles().get(articleSelection - 1);
				String fileName = article + ".pdf";
				byte[] data =  service.getNoticePdf(article);
				System.out.println("Fichier téléchargé");
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(data);
				fos.close();
			    Desktop dt = Desktop.getDesktop();
			    dt.open(new File(fileName));
			}
		} while(articleSelection != 0);
	}
	
	private static int getSelectionInList(List list, Scanner sc) {
		int selection;
		do
		{
			System.out.println("0. Retour");
			for(int i = 0; i < list.size(); i++) {
				System.out.println((i + 1) + ". " + list.get(i));
			}
			selection = sc.nextInt();
			if(selection >= 0 && selection <= list.size()) {
				return selection;
			}
		} while(true);
	}
	
	private static List<IndexName> readFile(String fileName)
	{
		List<IndexName> result = new ArrayList<IndexName>();
		try {
			Scanner in = new Scanner(new FileReader(fileName));
			while(in.hasNextLine()) {
				String line = in.nextLine();
				String[] elements = line.split("\t");
				IndexName index = new IndexName();
				String nameArticles;
				nameArticles = elements[1];
				index.setName(elements[0]);
				nameArticles = nameArticles.replace("&amp;", "&");
				for(String na : nameArticles.split(";")){
					String[] array = na.split(":");
					NameArticle nameArticle = new NameArticle();
					String articles;
					String name = "";
					for(int i = 0; i < array.length - 1; i++) {
						name += array[i];
					}
					nameArticle.setName(name);
					articles = array[array.length - 1];
					
					for(String a : articles.split(",")) {
						try
						{
							nameArticle.articles.add(Long.parseLong(a));
						}
						catch(NumberFormatException ex) {
						}
						
					}
					index.getNameArticles().add(nameArticle);
				}
				result.add(index);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			return null;
		}
		return result;
	}
	
	private static String getOptionMenu(Scanner sc)
	{
		System.out.println("Options :");
		System.out.println("0: Quitter");
		System.out.println("1: Rercherche d'une entreprise");
		System.out.println("2: Rercherche d'une personne");
		return sc.nextLine();
	}
	
	private static SoapServer getService(Scanner sc) throws MalformedURLException, Fault
	{
		System.out.println("Veuillez saisir votre nom d'utilisateur :");
		String user = sc.nextLine();
		System.out.println("Veuillez saisir votre mot de passe : ");
		String password = sc.nextLine();
		return ServiceHelper.getService(user, password);
	}
}
