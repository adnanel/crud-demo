package sample.api.model;

public class Ocjena {
    long id;
    Indeks indeks;
    String predmet;
    int ocjena;

    public Ocjena() {
    }

    public Ocjena(long id, Indeks indeks, String predmet, int ocjena) {
        this.id = id;
        this.indeks = indeks;
        this.predmet = predmet;
        this.ocjena = ocjena;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Indeks getIndeks() {
        return indeks;
    }

    public void setIndeks(Indeks indeks) {
        this.indeks = indeks;
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public int getOcjena() {
        return ocjena;
    }

    public void setOcjena(int ocjena) {
        this.ocjena = ocjena;
    }
}
