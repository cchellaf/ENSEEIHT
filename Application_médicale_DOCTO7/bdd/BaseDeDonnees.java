package bdd;

import Modele.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.*;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class BaseDeDonnees {
    public static final int MAX_MESSAGE_LENGTH = 300;

    private DataSource dataSource;

    public BaseDeDonnees(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {

        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = null;
        String request;

        if (utilisateur instanceof Patient) {
            Patient patient = (Patient) utilisateur;
            request = "INSERT INTO PATIENT(LOGIN, PASSWORD, NOM, PRENOM, TELEPHONE," +
                    " SECURITE_SOCIALE, DATE_NAISSANCE, TAILLE, POIDS)" +
                    " values(?, ?, ? ,? ,?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, patient.getLogin());
            preparedStatement.setString(2, patient.getHashPassword());
            preparedStatement.setString(3, patient.getNom());
            preparedStatement.setString(4, patient.getPrenom());
            preparedStatement.setString(5, patient.getTelephone());
            preparedStatement.setString(6, patient.getSecuriteSociale());
            preparedStatement.setDate(7, Date.valueOf(patient.getDateNaissance()));
            preparedStatement.setString(8, patient.getTaille());
            preparedStatement.setString(9, patient.getPoids());


        } else if (utilisateur instanceof Medecin) {
            Medecin medecin = (Medecin) utilisateur;
            int idEtablissement =  this.getIdEtablissement(medecin.getEtablissement());
            request = "INSERT INTO MEDECIN(LOGIN, PASSWORD, NOM, PRENOM," +
                    " TELEPHONE, SPECIALITE, ID_ETABLISSEMENT)  values(?, ?, ? ,? ,?, ?, ?)";
            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, medecin.getLogin());
            preparedStatement.setString(2, medecin.getHashPassword());
            preparedStatement.setString(3, medecin.getNom());
            preparedStatement.setString(4, medecin.getPrenom());
            preparedStatement.setString(5, medecin.getTelephone());
            preparedStatement.setString(6, medecin.getSpecialite());
            preparedStatement.setInt(7, idEtablissement);

        }

        preparedStatement.executeUpdate();
    }

    public void supprimerUtilisateur(Object utilisateur) { }

    public void ajouterEtablissement(Etablissement etablissement) throws SQLException {

        Connection connection = this.dataSource.getConnection();
        String request = "INSERT INTO ETABLISSEMENT(NOM, ADRESSE, CODE_POSTAL, VILLE)" +
                " values(?, ?, ? ,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, etablissement.getNom());
        preparedStatement.setString(2, etablissement.getAdresse());
        preparedStatement.setString(3, etablissement.getCodePostal());
        preparedStatement.setString(4, etablissement.getCodePostal());

        preparedStatement.executeUpdate();
    }

    public Patient getPatient(String login, String hashPassword) throws SQLException {
        Patient patient = null;
        String request = "SELECT * FROM PATIENT WHERE LOGIN = ? AND PASSWORD = ?";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, hashPassword);
        preparedStatement.executeQuery();
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            String log = rs.getString(2);
            String pass = rs.getString(3);
            String nom = rs.getString(4);
            String prenom = rs.getString(5);
            String tel = rs.getString(6);

            String secu = rs.getString(7);
            Date dateDN = rs.getDate(8);
            LocalDate ldateDN = dateDN.toLocalDate();
            String taille = rs.getString(9);
            String poids = rs.getString(10);

            patient = new Patient(id, log, pass, nom, prenom, tel, secu, ldateDN, taille, poids);

        }
        return patient;
    }

    public Medecin getMedecin(String login, String hashPassword) throws SQLException {
        Medecin medecin = null;
        String request = "SELECT * FROM MEDECIN WHERE LOGIN = ? AND PASSWORD = ?";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, hashPassword);
        preparedStatement.executeQuery();
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            int id_etab = rs.getInt(8);
            Etablissement etab = this.getEtablissement(id_etab);

            int id = rs.getInt(1);
            String log = rs.getString(2);
            String pass = rs.getString(3);
            String nom = rs.getString(4);
            String prenom = rs.getString(5);
            String tel = rs.getString(6);
            String spec = rs.getString(7);
            medecin = new Medecin(id, log, pass, nom, prenom, tel, spec, etab);

        }
        return medecin;
    }

    private Etablissement getEtablissement(int id) throws SQLException {
        Etablissement etablissement = null;
        String request = "SELECT * FROM ETABLISSEMENT WHERE ID = ?";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            String nom = rs.getString(2);
            String adresse = rs.getString(3);
            String cp = rs.getString(4);
            String ville = rs.getString(5);
            etablissement = new Etablissement(id, nom, adresse, cp, ville);
        }
        return etablissement;
    }

    private int getIdEtablissement(Etablissement etablissement) throws SQLException {
        int id = etablissement.getId();
        if (id == 0) {
            String request = "SELECT ID FROM ETABLISSEMENT " +
                    "WHERE NOM = ? " +
                    "AND ADRESSE = ? " +
                    "AND CODE_POSTAL = ? " +
                    "AND VILLE = ?";
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, etablissement.getNom());
            preparedStatement.setString(2, etablissement.getAdresse());
            preparedStatement.setString(3, etablissement.getCodePostal());
            preparedStatement.setString(4, etablissement.getVille());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
                etablissement.setId(id);
            } else {
                throw new SQLException("Etablissement ne se trouve pas dans la bdd");
            }
        }

        return id;
    }

    private Patient getPatient(int id) throws SQLException {
        Patient patient = null;
        String request = "SELECT * FROM PATIENT WHERE ID = ?";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            String log = rs.getString(2);
            String pass = rs.getString(3);
            String nom = rs.getString(4);
            String prenom = rs.getString(5);
            String tel = rs.getString(6);

            String secu = rs.getString(7);
            Date dateDN = rs.getDate(8);
            LocalDate ldateDN = dateDN.toLocalDate();
            String taille = rs.getString(9);
            String poids = rs.getString(10);

            patient = new Patient(id, log, pass, nom, prenom, tel, secu, ldateDN, taille, poids);
        } else {
            throw new SQLException("Patient ne se trouve pas dans la bdd");
        }
        return patient;
    }

    private Medecin getMedecin(int id) throws SQLException {
        Medecin medecin = null;
        String request = "SELECT * FROM MEDECIN WHERE ID = ?";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            String log = rs.getString(2);
            String pass = rs.getString(3);
            String nom = rs.getString(4);
            String prenom = rs.getString(5);
            String tel = rs.getString(6);
            String spec = rs.getString(7);
            int idEtab = rs.getInt(8);
            Etablissement etab = this.getEtablissement(id);
            medecin = new Medecin(id, log, pass, nom, prenom, tel, spec, etab);
        } else {
            throw new SQLException("Medecin ne se trouve pas dans la bdd");
        }
        return medecin;
    }

    private int getIdUtilisateur(Utilisateur utilisateur) throws SQLException {
        int id = utilisateur.getId();
        if (id == 0) {
            String type = "PATIENT";
            if (utilisateur instanceof Medecin) {
                type = "MEDECIN";
            }
            String request = "SELECT ID FROM " +
                    type +
                    " WHERE LOGIN = ?";
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, utilisateur.getLogin());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
                utilisateur.setId(id);
            } else {
                throw new SQLException("Utilisateur (" + type + ") ne se trouve pas dans la bdd");
            }
        }
        return id;
    }

    public Etablissement getEtablissement(String nom) throws SQLException {
        Etablissement etablissement = null;
        String request = "SELECT * FROM ETABLISSEMENT WHERE NOM = ?";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, nom);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            String adresse = rs.getString(3);
            String cp = rs.getString(4);
            String ville = rs.getString(5);
            etablissement = new Etablissement(id, nom, adresse, cp, ville);
        }
        return etablissement;
    }

    public List<String> getNomsEtablissements() throws SQLException {
        List<String> resultat = new ArrayList<>();
        String request = "SELECT DISTINCT NOM FROM ETABLISSEMENT ORDER BY NOM";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            String nom = rs.getString(1);
            resultat.add(nom);
        }
        return resultat;
    }

    public List<Medecin> getMedecins(String specialite) throws SQLException {
        List<Medecin> resultat = new ArrayList<>();
        String request = "SELECT * FROM MEDECIN WHERE SPECIALITE = ?";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, specialite);
        preparedStatement.executeQuery();
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int id_etab = rs.getInt(8);
            Etablissement etab = this.getEtablissement(id_etab);

            String log = rs.getString(2);
            String pass = rs.getString(3);
            String nom = rs.getString(4);
            String prenom = rs.getString(5);
            String tel = rs.getString(6);
            String spec = rs.getString(7);
            resultat.add(new Medecin(log, pass, nom, prenom, tel, spec, etab));
        }
        return resultat;
    }

    public List<String> getSpecialites() throws SQLException {
        List<String> resultat = new ArrayList<>();
        String request = "SELECT DISTINCT SPECIALITE FROM MEDECIN";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            String specialite = rs.getString(1);
            resultat.add(specialite);
        }
        return resultat;
    }

    public void ajouterRendezVous(RendezVous rendezVous) throws SQLException {
        Date jour = Date.valueOf(rendezVous.getJour());
        Time heure = Time.valueOf(rendezVous.getHeure());
        int idMedecin = this.getIdUtilisateur(rendezVous.getMedecin());
        int idPatient = this.getIdUtilisateur(rendezVous.getPatient());

        String request = "INSERT INTO RENDEZ_VOUS(JOUR, HEURE, ID_MEDECIN, ID_PATIENT, BILAN)" +
                " VALUES(?, ?, ?, ?, ?)";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setDate(1, jour);
        preparedStatement.setTime(2, heure);
        preparedStatement.setInt(3, idMedecin);
        preparedStatement.setInt(4, idPatient);
        preparedStatement.setCharacterStream(5, new StringReader(""));
        preparedStatement.executeUpdate();
    }

    public List<RendezVous> getRendezVous(Utilisateur utilisateur, Date dateRdv) throws SQLException {
        List<RendezVous> resultat = new ArrayList<>();
        int id = this.getIdUtilisateur(utilisateur);
        String typeId = "ID_PATIENT";
        if (utilisateur instanceof Medecin) {
            typeId = "ID_MEDECIN";
        }
        String request = "SELECT * FROM RENDEZ_VOUS WHERE " +
                typeId +
                " = ? AND JOUR = ? ORDER BY HEURE";
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        preparedStatement.setDate(2, dateRdv);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Time time = resultSet.getTime(2);
            LocalTime localTime = time.toLocalTime();
            int idMedecin = resultSet.getInt(3);
            Medecin medecin = this.getMedecin(idMedecin);
            int idPatient = resultSet.getInt(4);
            Patient patient = this.getPatient(idPatient);
            RendezVous rendezVous = new RendezVous(dateRdv.toLocalDate(),
                    localTime, medecin, patient);
            resultat.add(rendezVous);
        }
        return resultat;
    }

    public List<RendezVous> getRendezVous(Utilisateur utilisateur) throws SQLException {
        List<RendezVous> resultat = new ArrayList<>();
        int id = this.getIdUtilisateur(utilisateur);
        String typeId = "ID_PATIENT";
        if (utilisateur instanceof Medecin) {
            typeId = "ID_MEDECIN";
        }
        String request = "SELECT * FROM RENDEZ_VOUS WHERE " +
                typeId + " = ?";

        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Date date = resultSet.getDate(1);
            LocalDate localDate = date.toLocalDate();
            Time time = resultSet.getTime(2);
            LocalTime localTime = time.toLocalTime();
            int idMedecin = resultSet.getInt(3);
            Medecin medecin = this.getMedecin(idMedecin);
            int idPatient = resultSet.getInt(4);
            Patient patient = this.getPatient(idPatient);

            //String bilan = resultSet.getString(5);
            Reader reader = resultSet.getCharacterStream(5);
            char[] buffer = new char[MAX_MESSAGE_LENGTH];
            try {
                reader.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String bilan = new String(buffer);

            RendezVous rendezVous = new RendezVous(localDate,
                    localTime, medecin, patient, bilan);
            resultat.add(rendezVous);
        }
        return resultat;
    }

    public PlanningJour getPlanningJour(Medecin medecin, Date jour) throws SQLException {
        PlanningJour planningJour = new PlanningJour(jour.toLocalDate(), 20);
        LocalTime heureDepart = planningJour.getHeureDepart();
        List<RendezVous> listeRendezVous = this.getRendezVous(medecin, jour);
        for (RendezVous rendezVous : listeRendezVous) {
            LocalTime heureRdv = rendezVous.getHeure();
            Duration duration = Duration.between(heureDepart, heureRdv);
            int numeroCreneau = planningJour.getNumeroCreneau(duration);
            planningJour.setOccupe(numeroCreneau);
        }
        return planningJour;
    }

    public void mettreAJourRendezVous(RendezVous rendezVous) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        String request = "UPDATE RENDEZ_VOUS" +
                " SET BILAN = ? " +
                "WHERE ID_PATIENT = ? AND ID_MEDECIN = ?";
        int idPatient = this.getIdUtilisateur(rendezVous.getPatient());
        int idMedecin = this.getIdUtilisateur(rendezVous.getMedecin());
        String bilan = rendezVous.getBilan();
        Reader reader = new StringReader(bilan);
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setCharacterStream(1, reader);
        preparedStatement.setInt(2, idPatient);
        preparedStatement.setInt(3, idMedecin);
        preparedStatement.executeUpdate();
    }
}
