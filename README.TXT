Postavka:

Imamo bazu studenata.
Svaki student moze imati indeks, a i ne mora (moze biti null).
U indeks se pisu ocjene za neki predmet.
Radi jednostavnosti, cuvamo samo ime predmeta u samoj ocjeni.


Za indeks se cuva vrijeme kreiranje sloga u bazu. Primjetite da se vrijednost te kolone ne postavlja kroz kod, nego ima default vrijednost u create table SQL-u.
Odnosno, default vrijednost je "current_timestamp", sto predstavlja trenutno vrijeme na bazi pri citanju vrijednosti te varijable. (sto mozete uvijek provjeriti sa queryem "select current_timestamp")
Ovaj slog se ne bi trebao moci azurirati naknadno, pa se izostavlja u UPDATE queryu.

Tabele - http://prntscr.com/m343ya

