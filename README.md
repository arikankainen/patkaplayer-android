# Pätkä Player

Android-appi audioklippien soittamiseen. Klippien tulee olla MP3-muodossa, ja sijaita puhelimen muistissa yhdessä pääkansiossa, esim. `Pätkät`. Tuon kansion pitää sisältää alikansioita, joihin itse klipit sijoitetaan.

![Pätkä Player](/docs/main.png)

## Käyttö

Aluksi täytyy valita kansio jonka alikansioissa klipit ovat. Kansio valitaan painamalla valikkoriviltä kolmea pistettä ja valitsemalla `Select folder`. Tämän jälkeen valitun kansion alikansiot näkyvät ohjelmassa listana, joista yhden voi avata kerrallaan ja valita sieltä klippejä soitettavaksi. Jos käytetään ohjelman `Play`-nappia, soitetaan auki olevasta kansiosta satunnainen klippi. Mikäli ei olla minkään kansion sisällä eli näkymänä on vain lista kansioista, `Play`-nappi soittaa satunnaisen klipin satunnaisesta kansiosta. `Toisto`-nappi soittaa edellisen klipin uudelleen, ja `Stop`-nappi luonnollisesti keskeyttää klipin. `Play`-napin päällä näkyy edellisen klipin nimi ja kansio, ja tekstiä klikkaamalla voi siirtyä suoraan kyseiseen kansioon.

Ohjelmassa on myös hakutoiminto, jolla näkymään saa pelkästään tietyn sanan sisältävät klipit. `Play`-nappi soittaa tällöin satunnaisen klipin hakutuloksista. Alariviltä löytyy myös kuvake jossa on kaatunut kahdeksikko. Sen päälle kytkemällä uuden klipin soitto ei katkaise edellistä, vaan klipit soivat päällekkäin. Korkeintaan 5 klippiä voi soida yhtäaikaa.

Alarivin kellokuvakkeesta saa päälle ajastimen, jolloin satunnainen klippi soitetaan asetuksissa määritellyn ajan välein. Aika määritellään painamalla valikkorivin kolmea pistettä ja valitsemalla `Timer settings`. `Minimum delay`:lla valitaan minimiaika ja `Maximum delay`:lla maksimiaika. Näiden kahden valitun ajan väliltä ohjelma arpoo satunnaisen ajan. Kun ajastin on päällä on puhelimen nukkuminen estetty, eli akkua kuluu enemmän. Jos käytössä on Androidin versio 4.2 tai uudempi, tulee ajastimen ollessa päällä puhelimen ilmoituksiin pysyvä ilmoitus siitä että ajastin on päällä.

## Lataus

En ota mitään vastuuta ohjelman mahdollisesti aiheuttamista vahingoista; kukin käyttää ohjelmaa omalla vastuullaan.

Vaatii Androidin version 4.0.3 (julkaistu 2011) tai sitä uudemman. Käyttö vaatii myös lukuoikeuden laitteen tiedostoihin. Ohjelmalla itsellään ei pysty lisäämään tai muokkaamaan klippejä, vaan siihen tarvitaan tiedostojenhallintaohjelma tai kytkeminen tietokoneeseen.

**[Lataa uusin versio](https://github.com/arikankainen/patkaplayer-android/releases)**
