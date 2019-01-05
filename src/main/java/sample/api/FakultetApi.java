package sample.api;

import sample.api.model.Indeks;
import sample.api.model.Ocjena;
import sample.api.model.Student;

import java.util.List;

// DAO nije javna klasa, a API jeste (servis).
// razlog je sto ne zelimo da se van ovog paketa (sample.api) koristi DAO, nego iskljucivo ova API klasa.
public class FakultetApi {
    public static FakultetApi createSqliteApi() {
        return new FakultetApi(new FakultetSqliteDAO());
    }
    public static FakultetApi createXmlApi() {
        // ToDo - vjezba za studente - implementirajte FakultetXmlDAO
        // return new FakultetApi(FakultetXmlDAO.getInstance());
        return null;
    }

    private FakultetDAO dao;

    private FakultetApi(FakultetDAO dao) {
        this.dao = dao;
    }

    // ovdje ide "servisni layer", odnosno sve metode koje trebaju vasoj aplikaciji. Sva biznis logika.

    // primjer neke "slozenije" funkcije, daje sve studente sa barem jednom ocjenom iznad 5.
    // Ovo ne ide u DAO jer DAO treba da bude sto jednostavniji (idealno samo CRUD)
    // Student - "ali zasto bi servis ucitao sve unose pa onda filtrirao, zar nije to previse ucitavanja iz baze ako imamo recimo 500000 unosa?"
    // Autor - "da, to bi se popravilo uz malo fleksibilniji DAO, gdje ne bi imali samo metodu getEntityById, nego getEntityByCriteria() koja
    // bi primila neki specijalni objekat na osnovu kojeg se generise where klauza. Ali to spada u naprednije tehnike koje idu van opsega ovog predmeta."
    public List<Student> getStudentsWithPassableGrades() {
        List<Student> allStudents = dao.getAllStudents();
        allStudents.removeIf(s -> {
            boolean shouldRemove = true;
            for ( Ocjena o : dao.getOcjeneByIndeks(s.getIndeks()) ) {
                if ( o.getOcjena() > 5 ) {
                    shouldRemove = false;
                }
            }
            return shouldRemove;
        });
        return allStudents;
    }


    // cesto imamo situaciju gdje servisna metoda samo propagira poziv u DAO, npr:
    public Student getStudentById(long id) {
        return dao.getStudentById(id);
    }
    // iako se ovo cini kao viska kod, servis u ovom slucaju sluzi da ubacimo jedan ZAJEDNICKI layer za sve DAO-e
    // odnosno, mozemo sutra imati 10 DAO-a (sqlite, xml, oracle, mysql, ...)
    // i u svakom ce biti getStudentById(), i ova metoda iznad ce pozvati tu metodu kod trenutno podesenog DAO-a.
    // Sada se postavlja problem - sta ako ce se ponasanje te metode nekako izmjeniti? treba provjeriti da li je neki uvjet
    // ispunjen, ako nije da vrati null, iako taj student postoji. Da ne bi morali mijenjati svih 10 DAO-a, dobra je stvar
    // sto imamo servis u koji upravo to treba da se radi (jer je to ipak servisna logika).

    public Indeks getIndeksById(long id) {
        return dao.getIndeksById(id);
    }
    public Ocjena getOcjenaById(long id) {
        return dao.getOcjenaById(id);
    }

    public List<Ocjena> getOcjeneByIndeks(long indeksId) {
        return dao.getOcjeneByIndeks(indeksId);
    }
    public List<Ocjena> getOcjeneByIndeks(Indeks indeks) {
        return dao.getOcjeneByIndeks(indeks);
    }

    public List<Indeks> getAllIndeksi() {
        return dao.getAllIndeksi();
    }
    public List<Ocjena> getAllOcjenas() {
        return dao.getAllOcjenas();
    }
    public List<Student> getAllStudents() {
        return dao.getAllStudents();
    }

    public Indeks addIndeks(Student s, Indeks obj) {
        return dao.addIndeks(s, obj);
    }
    public Ocjena addOcjena(Ocjena obj) {
        return dao.addOcjena(obj);
    }
    public Student addStudent(Student obj) {
        return dao.addStudent(obj);
    }
    public Ocjena saveOcjena(Ocjena obj) {
        return dao.saveOcjena(obj);
    }
    public Student saveStudent(Student obj) {
        return dao.saveStudent(obj);
    }

    public Indeks getIndeksByStudentId(long studentId) {
        return dao.getIndeksByStudentId(studentId);
    }

    public boolean deleteIndeks(long id) {
        return dao.deleteIndeks(id);
    }
    public boolean deleteOcjena(long id) {
        return dao.deleteOcjena(id);
    }
    public boolean deleteStudent(long id) {
        return dao.deleteStudent(id);
    }

    public void close() {
        dao.close();
    }
}
