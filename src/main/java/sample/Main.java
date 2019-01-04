package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.api.FakultetApi;
import sample.api.model.Indeks;
import sample.api.model.Ocjena;
import sample.api.model.Student;

import java.time.LocalDateTime;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        // Primjer upotrebe

        // napravimo servis koji koristi bazu
        FakultetApi api = FakultetApi.createSqliteApi();
        // a mogli bi koristiti i XML
        // api = FakultetApi.createXmlApi();
        // samo treba implementirati taj DAO, sve ostalo bi radilo ispravno.

        // id studenta postavljamo sta hocemo, jer u DAO svakako tu vrijednost ignorisemo pri kreiranju, korisitmo ID koji baza postavi.
        Student s = new Student(-1, "Student", "Studentic", null); // kao indeks saljemo null jer ga jos nema.

        s = api.addStudent(s); // zasto s postavljamo na novu vrijednost?
                               // jer ce DAO vratiti novu instancu studenta kojoj
                               // je postavljen ID na ispravnu vrijednost.
                               // Mogli smo i unutar APIa setovati vrijednost studentu kojeg dobijemo preko reference.

        Indeks indeks = new Indeks(); // za indeks nista ne postavljamo jer se sve vrijednosti automatski postavljaju (id, created_at)
        indeks = api.addIndeks(s, indeks);

        Ocjena[] ocjene = new Ocjena[] {
                new Ocjena(-1, indeks, "IM1", 10),
                new Ocjena(-1, indeks, "IM2", 10),
                new Ocjena(-1, indeks, "IM3", 10),
                new Ocjena(-1, indeks, "IM4", 10),
                new Ocjena(-1, indeks, "IM5", 10),
                new Ocjena(-1, indeks, "IM6", 10),
                new Ocjena(-1, indeks, "IM7", 10),
                new Ocjena(-1, indeks, "IM8", 10),
                new Ocjena(-1, indeks, "RPR", 6)
        };

        for ( Ocjena o : ocjene ) {
            api.addOcjena(o);
        }


        // testiramo citanje iz baze
        Student s2 = api.getStudentById(s.getId());

        System.out.println(String.format("Student %s %s ima ocjene:", s2.getFirstName(), s2.getLastName()));

        // dobavljamo indeks
        Indeks i2 = api.getIndeksByStudentId(s2.getId());

        // dobavljamo ocjene za indeks
        List<Ocjena> o2 = api.getOcjeneByIndeks(i2);

        for ( Ocjena o : o2 ) {
            System.out.println(String.format("%s - %d", o.getPredmet(), o.getOcjena()));
        }

        api.close(); // cim zavrsimo sa API-em, treba ga zatvoriti
    }
}
