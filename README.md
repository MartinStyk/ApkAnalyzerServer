# pa165-activity-tracker [![Build Status](https://travis-ci.org/MartinStyk/pa165-activity-tracker.svg?branch=master)](https://travis-ci.org/MartinStyk/pa165-activity-tracker)
This project was created as a part of course [FI:PA165 Enterprise Java applications](https://is.muni.cz/auth/predmet/fi/podzim2016/PA165) on Masaryk University, Brno, Czech Republic.

##How to run
* This project uses maven. Run command ``mvn clean install`` to build the project.
* [More info about the Web app] (https://github.com/MartinStyk/pa165-activity-tracker/blob/master/activity-tracker-spring-mvc/README.md)
* [More info about the Rest interface] (https://github.com/MartinStyk/pa165-activity-tracker/blob/master/activity-tracker-rest/README.md)

##Collaborators:
* [Jan Grundmann](https://github.com/jangrundmann)  [:mag_right:](https://github.com/MartinStyk/pa165-activity-tracker/commits?author=jangrundmann)
* [Adam Laurenčík](https://github.com/adamlaurencik)   [:mag_right:](https://github.com/MartinStyk/pa165-activity-tracker/commits?author=adamlaurencik)
* [Petra Ondřejková](https://github.com/ondrejkova)  [:mag_right:](https://github.com/MartinStyk/pa165-activity-tracker/commits?author=ondrejkova)
* [Martin Styk](https://github.com/martinstyk)  [:mag_right:](https://github.com/MartinStyk/pa165-activity-tracker/commits?author=martinstyk)

##Assignment
This application is used for tracking sport activities of users. Users can be grouped into teams. Sport activities differs in amount of energy and calories spent while practicing it. Amount of energy spent depends on more factors like, age, weight, or height or sportman(sportwoman). Application uses [formula](http://www.shapesense.com/fitness-exercise/calculators/activity-based-calorie-burn-calculator.aspx) to compute energy spent on exercise.
Application tracks time of sport activity and burnt calories.

Aplication consists of 4 entities
* User
* Team
* Sport activity
* Activity report 

##### Czech version

System bude evidovat seznam uzivatelu / tymu a sportovnich aktivit, ktere uzivatel muze provozovat. Kazda sportovni aktivita vyzaduje ruzne energeticke vydaje. Ty zalezi na vice udajich, zejmena na vaze jedince, veku i pohlavi. Systém pro výpočet těchto hodnot využívá vzorec zveřejněný na http://www.shapesense.com/fitness-exercise/calculators/activity-based-calorie-burn-calculator.aspx. System bude zaznamenavat kazdou aktivitu, cas kdy byla provozovana a spalene kalorie.

V systemu budou 4 entity:
* uzivatel (vek, vaha, jmeno, pohlavi...)
* team (nazev, clenove)
* sportovni aktivita (nazev...)
* zaznam sportovni aktivity (cas, vzdalenost, doba trvani...)

##Analysis models
###Use case diagram
![use case](https://raw.githubusercontent.com/MartinStyk/pa165-activity-tracker/master/Use Case diagram.jpg)
###Entity class diagram
![class](https://raw.githubusercontent.com/MartinStyk/pa165-activity-tracker/master/Class diagram.jpg)
