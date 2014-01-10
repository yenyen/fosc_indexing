#Welcome to Fosc indexing

The project goal is to index the articles of the FOSC.ch's database and show the links between companies, people and articles.

## Projects organization

The project is divided into three sub-projects :

### [Fosc_download](https://github.com/yenyen/fosc_indexing/tree/master/fosc_download)

This project contains the code used to download articles from the SOAP service of [FOSC.ch](http://www.fosc.ch)

### [DownloadFirstName](https://github.com/yenyen/fosc_indexing/tree/master/DownloadFirstName)

This project contains the code used to download the list of french first names from [dictionnaire-prenom.bebevallee.com](http://dictionnaire-prenom.bebevallee.com)

### [Fosc_indexing](https://github.com/yenyen/fosc_indexing/tree/master/fosc_indexing)

This project is the main project. It contains the mappers and reducers used to index the articles.

### [Data](https://github.com/yenyen/fosc_indexing/tree/master/data)

This folder contains all data extract with [fosc_download](https://github.com/yenyen/fosc_indexing/tree/master/fosc_download) and [downloadFirstName](https://github.com/yenyen/fosc_indexing/tree/master/DownloadFirstName).
