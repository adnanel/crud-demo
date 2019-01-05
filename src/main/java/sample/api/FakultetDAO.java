package sample.api;

import sample.api.model.Indeks;
import sample.api.model.Ocjena;
import sample.api.model.Student;

import java.util.List;

interface FakultetDAO {
    Indeks getIndeksById(long id);
    Student getStudentById(long id);
    Ocjena getOcjenaById(long id);


    List<Ocjena> getOcjeneByIndeks(long indeksId);
    List<Ocjena> getOcjeneByIndeks(Indeks indeks);

    List<Indeks> getAllIndeksi();
    List<Ocjena> getAllOcjenas();
    List<Student> getAllStudents();

    Indeks addIndeks(Student s, Indeks obj);
    Ocjena addOcjena(Ocjena obj);
    Student addStudent(Student obj);

    // Kod indeksa se nema sta azurirati
    // (ne zelimo da indeks moze promjeniti svog vlasnika, studenta), tako da nema metode
    // Indeks saveIndeks(Indeks obj);
    Ocjena saveOcjena(Ocjena obj);
    Student saveStudent(Student obj);

    Indeks getIndeksByStudentId(long studentId);

    boolean deleteIndeks(long id);
    boolean deleteOcjena(long id);
    boolean deleteStudent(long id);

    // za zatvaranje ovog DAO-a, odnosno svih otvorenih resursa.
    void close();
}
