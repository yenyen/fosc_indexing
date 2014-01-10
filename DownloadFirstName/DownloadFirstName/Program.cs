using System;
using System.Net;
using System.Xml.Linq;
using System.Text;
using System.Collections.Generic;
using System.Linq;
using System.IO;

namespace DownloadFirstName
{
    class MainClass
    {
		private static char[] ALPHABET = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		public static void Main(string[] args)
        {
			List<string> sectionNames = new List<string>();
			List<string> allNames = new List<string>();
			using (WebClient client = new WebClient ())
			{
				foreach (char letter in ALPHABET)
				{
					Console.Write("Download " + letter + "... ");
					int i = 0;
					do
					{
						sectionNames.Clear();
						string html = client.DownloadString("http://dictionnaire-prenom.bebevallee.com/prenom-commencant-par-A/?&sexe=&taille=&popularite=&compose=&tendance=&date=&debut=" + letter + "&milieu=&fin=&ordre=Alphabetique&sens=1&start=" + i);

						var rows = html
							.Split(new string[] { "<table class=\"simple\">" }, StringSplitOptions.RemoveEmptyEntries)[1]
							.Split(new string[] { "</table>" }, StringSplitOptions.RemoveEmptyEntries)[0]
							.Split(new string[] { "<tr>" }, StringSplitOptions.RemoveEmptyEntries).Skip(2);

						foreach (var row in rows)
						{
							var cells = row.Split(new string[] { "<td" }, StringSplitOptions.RemoveEmptyEntries);
							var name = cells[2].Split('>')[2].Replace("</a", "");
							var number = cells[3].Split(new string[] { "<b>", "</b>" }, StringSplitOptions.RemoveEmptyEntries)[1];
							sectionNames.Add(name + " " + number);
						}
						allNames.AddRange(sectionNames);
						i += 100;
					} while(sectionNames.Count == 100);
					Console.WriteLine("done");
				}
			}
			File.WriteAllLines("names.txt", allNames.ToArray());
        }

    }
}
