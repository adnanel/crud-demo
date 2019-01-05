package sample.api;

import org.intellij.lang.annotations.Language;
import sample.api.model.Indeks;
import sample.api.model.Ocjena;
import sample.api.model.Student;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Note - inace je losa praksa gutati sve ove izuzetke kao u ovoj klasi, ali posto je svrha projekta pokazati implementaciju CRUD-a, trudio sam se da bude sto manje koda koji nije vezan za ovo.
// Note 2 - u ovoj implementaciji ima prilicno dosta dupliciranja koda. Ovo bi izbjegli pomocu funkcija koje bi dinamicki generisale SQL (kroz neke pomocne objekte), specificno WHERE dio query-a. Opet, cilj nam je da minimiziramo kompleksnost implementacije.
// Note 3 - ova klasa NAMJERNO NIJE PUBLIC!! Pogledaj klasu FakultetApi za detaljnije...
class FakultetSqliteDAO implements FakultetDAO {
    private static final String URL = "jdbc:sqlite:baza.db";

    private Connection conn;


    private void closeStatement(Statement stmt) {
        if ( stmt == null ) {
            return;
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeStatement(PreparedStatement stmt) {
        if ( stmt == null ) {
            return;
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public FakultetSqliteDAO() {
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            stmt.execute("select * from indeks");
            stmt.execute("select * from ocjena");
            stmt.execute("select * from student");
        } catch ( Exception ex ) {
            // pretpostavljam da nema tabela
            createTables();
        } finally {
            closeStatement(stmt);
        }
    }

    private void createTables() {
        @Language("SQLite")
        String[] sqls = new String[] {
            // brisemo tabele koje vec postoje
            "drop table if exists ocjena",
            "drop table if exists indeks",
            "drop table if exists student",
            // pa onda ispocetka kreiramo sve tabele.
            "create table student(id integer primary key autoincrement, first_name varchar(50), last_name varchar(50))",
            "create table indeks(id integer primary key autoincrement, student_id integer references student(id), created_at datetime default current_timestamp)",
            "create table ocjena(id integer primary key autoincrement, predmet varchar(50), ocjena integer, indeks_id integer references indeks(id))"
        };


        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for ( String sql : sqls ) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
    }


    @Override
    public Indeks getIndeksByStudentId(long studentId) {
        @Language("SQLite")
        String sql = "select id, student_id, created_at from indeks where student_id = ?";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);

            stmt.setLong(1, studentId);
            ResultSet set = stmt.executeQuery();

            if ( set.next() ) {
                String date = set.getString(3);
                // posto SQLite cuva datum u obliku "2019-01-04 17:01:37" a LocalDateTime.parse trazi format
                // "2019-01-04T17:01:37", mi cemo jednostavno space replace-ati sa T prije parsiranja.
                date = date.replace(" ", "T");
                return new Indeks(
                        set.getLong(1),
                        LocalDateTime.parse(date)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }

        return null;
    }


    @Override
    public Indeks getIndeksById(long id) {
        @Language("SQLite")
        String sql = "select id, student_id, created_at from indeks where id = ?";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            ResultSet set = stmt.executeQuery();

            if ( set.next() ) {
                String date = set.getString(3);
                date = date.replace(" ", "T");
                return new Indeks(
                    set.getLong(1),
                    LocalDateTime.parse(date)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }

        return null;
    }

    @Override
    public Student getStudentById(long id) {
        @Language("SQLite")
        String sql = "select id, first_name, last_name from student where id = ?";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            ResultSet set = stmt.executeQuery();

            if ( set.next() ) {
                return new Student(
                        set.getLong(1),
                        set.getString(2),
                        set.getString(3),
                        getIndeksByStudentId(id)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }

        return null;
    }

    @Override
    public Ocjena getOcjenaById(long id) {
        @Language("SQLite")
        String sql = "select id, predmet, ocjena, indeks_id from ocjena where id = ?";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);

            stmt.setLong(1, id);
            ResultSet set = stmt.executeQuery();

            if ( set.next() ) {
                return new Ocjena(
                        set.getLong(1),
                        getIndeksById(set.getLong(4)),
                        set.getString(2),
                        set.getInt(3)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }

        return null;
    }

    @Override
    public List<Indeks> getAllIndeksi() {
        @Language("SQLite")
        String sql = "select id, student_id, created_at from indeks";

        PreparedStatement stmt = null;
        try {
            List<Indeks> res = new ArrayList<>();

            stmt = conn.prepareStatement(sql);

            ResultSet set = stmt.executeQuery();

            while ( set.next() ) {
                String date = set.getString(3);
                date = date.replace(" ", "T");
                res.add(new Indeks(
                        set.getLong(1),
                        LocalDateTime.parse(date)
                ));
            }

            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }

        return null;
    }

    @Override
    public List<Ocjena> getOcjeneByIndeks(long indeksId) {
        @Language("SQLite")
        String sql = "select id, predmet, ocjena, indeks_id from ocjena where indeks_id = ?";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, indeksId);
            ResultSet set = stmt.executeQuery();

            List<Ocjena> res = new ArrayList<>();
            while ( set.next() ) {
                Ocjena i = new Ocjena(
                        set.getLong(1),
                        getIndeksById(indeksId),
                        set.getString(2),
                        set.getInt(3)
                );
                res.add(i);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }


        return null;
    }

    @Override
    public List<Ocjena> getOcjeneByIndeks(Indeks indeks) {
        return getOcjeneByIndeks(indeks.getId());
    }

    @Override
    public List<Ocjena> getAllOcjenas() {
        @Language("SQLite")
        String sql = "select id, predmet, ocjena from ocjena";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);

            ResultSet set = stmt.executeQuery();

            List<Ocjena> res = new ArrayList<>();
            while ( set.next() ) {
                Ocjena i = new Ocjena(
                        set.getLong(1),
                        getIndeksById(set.getLong(2)),
                        set.getString(3),
                        set.getInt(4)
                );
                res.add(i);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }

        return null;
    }

    private long getLastInsertedId() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            ResultSet s = stmt.executeQuery("select last_insert_rowid() as id");

            if (s.next()) {
                return s.getLong("id");
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return -1;
    }

    @Override
    public List<Student> getAllStudents() {
        return null;
    }

    @Override
    public Indeks addIndeks(Student s, Indeks obj) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("insert into indeks(student_id) values (?)");
            stmt.setLong(1, s.getId());

            stmt.execute();

            return getIndeksById(getLastInsertedId());
        } catch (SQLException e) {
            return null;
        } finally {
            closeStatement(stmt);
        }
    }

    @Override
    public Ocjena addOcjena(Ocjena obj) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("insert into ocjena(predmet, ocjena, indeks_id) values (?, ?, ?)");
            stmt.setString(1, obj.getPredmet());
            stmt.setInt(2, obj.getOcjena());
            stmt.setLong(3, obj.getIndeks().getId());

            stmt.execute();

            return getOcjenaById(getLastInsertedId());
        } catch (SQLException e) {
            return null;
        } finally {
            closeStatement(stmt);
        }
    }

    @Override
    public Student addStudent(Student obj) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("insert into student(first_name, last_name) values (?, ?)");
            stmt.setString(1, obj.getFirstName());
            stmt.setString(2, obj.getLastName());

            stmt.execute();

            return getStudentById(getLastInsertedId());
        } catch (SQLException e) {
            return null;
        } finally {
            closeStatement(stmt);
        }
    }

    @Override
    public Ocjena saveOcjena(Ocjena obj) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("update ocjena set ocjena = ?, predmet = ?, indeks_id = ? where id = ?");

            stmt.setInt(1, obj.getOcjena());
            stmt.setString(2, obj.getPredmet());

            // posto moze biti student bez Indeksa, moramo moci postaviti indeks_id na NULL. setLong NE SMIJE PRIMITI NULL (jer prima long, a ne Long)
            if ( obj.getIndeks() != null ) {
                stmt.setLong(3, obj.getIndeks().getId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.execute();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return getOcjenaById(obj.getId());
    }

    @Override
    public Student saveStudent(Student obj) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("update student set first_name = ?, last_name = ? where id = ?");

            stmt.setString(1, obj.getFirstName());
            stmt.setString(2, obj.getLastName());
            stmt.setLong(3, obj.getId());
            stmt.execute();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return getStudentById(obj.getId());
    }

    @Override
    public boolean deleteIndeks(long id) {
        PreparedStatement stmt = null;
        try {
            boolean exists = getOcjenaById(id) != null;
            if ( exists ) {
                stmt = conn.prepareStatement("delete from indeks where id = ?");
                stmt.setLong(1, id);
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return false;
    }

    @Override
    public boolean deleteOcjena(long id) {
        PreparedStatement stmt = null;
        try {
            boolean exists = getOcjenaById(id) != null;
            if ( exists ) {
                stmt = conn.prepareStatement("delete from ocjena where id = ?");
                stmt.setLong(1, id);
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return false;
    }

    @Override
    public boolean deleteStudent(long id) {
        PreparedStatement stmt = null;
        try {
            boolean exists = getStudentById(id) != null;
            if ( exists ) {
                stmt = conn.prepareStatement("delete from student where id = ?");
                stmt.setLong(1, id);
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return false;
    }

    @Override
    public void close() {
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
