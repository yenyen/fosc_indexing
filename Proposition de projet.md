#Bid Data Analytics - Lien entre personnes et entreprises
####Komanda Phanzu - Aurélien Thévoz

##Proposition de projet

###Problème de départ

La *Feuille Officiel Suisse du Commerce ([www.fosc.ch](www.fosc.ch))* regroupe quotidiennement un grand nombre de publications officielles (registre du commerce, faillites, poursuites, etc.) de toute la Suisse.

###But du projet

Le but du projet est de trouver les liens entre les entreprises et les personnes dans base de données de la FOSC dans le but de pouvoir répondre à deux questions :

- Qui est impliqué dans une entreprise ?
- Dans quelle entreprise est impliqué une personne ?

Comme objectif secondaire, il est également possible d'indexer les articles par personne et par entreprise pour répondre aux deux questions suivantes :

- Qu'elles sont les articles lié à une entreprise ?
- Qu'elles sont les articles lié à une personne ?

###Base de données

L'état propose un API (soap) pour récupérer les articles au format XML ou PDF. Cette API est accessible uniquement par abonnement (1an CHF 500.-) ou en période d'essais d'une semaine. 

Afin de pouvoir commencer le projet dès le début de celui-ci, nous avons activé la période d'essais et téléchargé tous les articles (au format XML) depuis une année (environ 600MB.)

Ci-dessous, l'exemple d'un article (la ligne commence par l'id du document, suivi du contenu de l'article) :

	1185409 <?xml version="1.0" encoding="UTF-8"?><FORM ACTIVE.PAGE="1" FOSC_NR="223" ID="1185409" LANG="DE" PUBLISH_NR="1" USER="46"><HR01><NOTICE.REF>ts131114151447</NOTICE.REF><PUB.HEAD><CANTON.NAME>ZG</CANTON.NAME><PUB.DATE>18.11.2013</PUB.DATE></PUB.HEAD><HR01.SPEC><HRA.LOG EHRA.NOTICE.ID="3268484"><HRA.OFFICE><HRA.OFFICE.ID>170</HRA.OFFICE.ID><HRA.OFFICE.NAME>Handelsregisteramt des Kantons Zug</HRA.OFFICE.NAME></HRA.OFFICE><HRA.LOG.DATE>13.11.2013</HRA.LOG.DATE><HRA.LOG.NUM>15157</HRA.LOG.NUM></HRA.LOG><HR.FIRMS><FIRM.ID>CH17030383086</FIRM.ID><FIRM INFO.VER="OLD"><NAME>Crown Metals Trading AG</NAME><LEG.FORM><LEG.FORM.ID>3</LEG.FORM.ID><LEG.FORM.DESCR>Aktiengesellschaft</LEG.FORM.DESCR></LEG.FORM><SH.REG.OFFICE><CITY>Zug</CITY></SH.REG.OFFICE></FIRM><FIRM INFO.VER="NEW"><NAME>Crown Metals Trading AG</NAME><LEG.FORM><LEG.FORM.ID>3</LEG.FORM.ID><LEG.FORM.DESCR>Aktiengesellschaft</LEG.FORM.DESCR></LEG.FORM><SH.REG.OFFICE><BFS.NUM>1711</BFS.NUM><CITY>Zug</CITY></SH.REG.OFFICE></FIRM></HR.FIRMS><HR.FIRM.ACT><STATUS.CHANGED TYPE="01"/></HR.FIRM.ACT><HR.PUB.CONTENT><FT TYPE="F">Crown Metals Trading AG (Crown Metals Trading SA) (Crown Metals Trading Ltd.),</FT><FT TYPE="S"> in Zug</FT>, CH-170.3.038.308-6, Dammstrasse 19, 6301 Zug, Aktiengesellschaft (Neueintragung). Statutendatum: 31.10.2013. Zweck: Handel mit Metallen aller Art, insbesondere Handel mit Magnesiumbarren sowie weiteren eisenfreien Metallen und Legierungen; vollst?ndige Zweckumschreibung gem?ss Statuten. Aktienkapital: CHF 380'000.00. Liberierung Aktienkapital: CHF 380'000.00. Aktien: 380 Namenaktien zu CHF?1'000.00. Publikationsorgan: SHAB. Mitteilungen an die Aktion?re erfolgen?per Brief?oder E-Mail an die im Aktienbuch verzeichneten Adressen. Mit Erkl?rung vom 18.10.2013 wurde auf die eingeschr?nkte Revision verzichtet. Eingetragene Personen: Xu, Qi, chinesischer Staatsangeh?riger, in Shanghai (CN), Pr?sident des Verwaltungsrates, mit Einzelunterschrift; Sticher, Walter, von Luzern, in R?schlikon, Mitglied des Verwaltungsrates, mit Einzelunterschrift. </HR.PUB.CONTENT></HR01.SPEC><SUBMITION><ZIPCODE>3003</ZIPCODE><CITY>Bern</CITY><SUBMIT.DATE>14.11.2013</SUBMIT.DATE><SUBMITOR>EHRA</SUBMITOR></SUBMITION></HR01></FORM>
	
La base de données est dans les trois langues nationales. Pour commencer nous allons nous concentrer sur les articles en français (détectable par la propriété *LANG*)
	
Afin de pouvoir retrouver les noms des personnes dans le texte des articles, nous avons télécharger une liste de prénoms sur le site : [dictionnaire-prenom.bebevallee.com](http://dictionnaire-prenom.bebevallee.com)

###Algorithme

Retrouvé le nom de l'entreprise est assez facile. Elle est toujours dans le champ *<name>* de l'article. Par contre, les noms des personnes apparaissent n'importe où dans le texte. Ainsi, la première difficulté consiste à détecter les noms des personnes du reste du texte.

Il semblerait que les mots précédents les noms sont souvent identiques. Ainsi, nous allons dans un premier temps utilisé la liste des prénoms téléchargée, pour détecté qu'elles sont les mots précédent et avec quelle probabilité un mot est suivi d'un nom.

Lors d'un deuxième traitement on pourra retrouver les noms à partir des mots précédents.







