package IHM;

import Modele.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;

public class PageSignUp extends JFrame {

    private Modele modele;

    final private JTextField loginTextField = new JTextField();
    final private JTextField passwordTextField = new JTextField();
    final private JTextField nomTextField = new JTextField();
    final private JTextField prenomTextField = new JTextField();
    final private JTextField telephoneTextField = new JTextField();

    final private JTextField securiteSocialeTextField = new JTextField();
    final private JTextField dateNaissanceTextField = new JTextField();
    final private JTextField tailleTextField = new JTextField();
    final private JTextField poidsTextField = new JTextField();

    final private JButton bValider = new JButton("Valider");
    final private JButton bAnnuler = new JButton("Annuler");


    public PageSignUp(Modele modele) {
        super();
        this.modele = modele;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(9, 2));
        centerPanel.add(new JLabel("Login"));
        centerPanel.add(loginTextField);
        centerPanel.add(new JLabel("Password"));
        centerPanel.add(passwordTextField);
        centerPanel.add(new JLabel("Prenom"));
        centerPanel.add(prenomTextField);
        centerPanel.add(new JLabel("Nom"));
        centerPanel.add(nomTextField);
        centerPanel.add(new JLabel("Telephone"));
        centerPanel.add(telephoneTextField);

        centerPanel.add(new JLabel("Securite Sociale"));
        centerPanel.add(securiteSocialeTextField);
        centerPanel.add(new JLabel("Date de Naissance"));
        centerPanel.add(dateNaissanceTextField);
        centerPanel.add(new JLabel("Taille (cm)"));
        centerPanel.add(tailleTextField);
        centerPanel.add(new JLabel("Poids (Kg)"));
        centerPanel.add(poidsTextField);

        this.getContentPane().add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(bValider);
        southPanel.add(bAnnuler);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.bValider.addActionListener(new ActionSignUp());
        this.bAnnuler.addActionListener(new ActionSignUp());

        this.pack();
        this.setTitle("Docto7");
        this.setVisible(true);
        this.setSize(new Dimension(300, 300));
        this.setLocation(500, 500);
    }

    private class ActionSignUp implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JButton bouton = (JButton) evt.getSource();
            if (bouton == PageSignUp.this.bValider) {
                String login = PageSignUp.this.loginTextField.getText();
                String password = PageSignUp.this.passwordTextField.getText();
                String hashPassword = Securite.hashMD5(password);
                String nom = PageSignUp.this.nomTextField.getText();
                String prenom = PageSignUp.this.prenomTextField.getText();
                String telephone = PageSignUp.this.telephoneTextField.getText();

                String securiteSociale = PageSignUp.this.securiteSocialeTextField.getText();
                String stringDateNaissance = PageSignUp.this.dateNaissanceTextField.getText();
                LocalDate dateNaissance = LocalDate.parse(stringDateNaissance);
                String taille = PageSignUp.this.tailleTextField.getText();
                String poids = PageSignUp.this.poidsTextField.getText();

                Utilisateur utilisateur = new Patient(login, hashPassword, nom, prenom, telephone,
                        securiteSociale, dateNaissance, taille, poids);
                try {
                    PageSignUp.this.modele.ajouterUtilisateur(utilisateur);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

            new PageLogin(PageSignUp.this.modele);
            PageSignUp.this.dispose();
        }
    }
}
