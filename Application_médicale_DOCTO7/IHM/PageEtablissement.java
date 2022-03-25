package IHM;
import Modele.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

public class PageEtablissement extends JFrame {

    private Modele modele;

    final private JTextField nomTextField = new JTextField();
    final private JTextField adresseTextField = new JTextField();
    final private JTextField codePTextField = new JTextField();
    final private JTextField villeTextField = new JTextField();

    final private JButton bValider = new JButton("Valider");
    final private JButton bAnnuler = new JButton("Annuler");


    public PageEtablissement(Modele modele) {
        super();
        this.modele = modele;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 2));
        centerPanel.add(new JLabel("Nom"));
        centerPanel.add(nomTextField);
        centerPanel.add(new JLabel("Adresse"));
        centerPanel.add(adresseTextField);

        centerPanel.add(new JLabel("Code Postal"));
        centerPanel.add(codePTextField);

        centerPanel.add(new JLabel("Ville"));
        centerPanel.add(villeTextField);

        this.getContentPane().add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(bValider);
        southPanel.add(bAnnuler);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.bValider.addActionListener(new ActionAjouter());
        this.bAnnuler.addActionListener(new ActionAjouter());

        this.pack();
        this.setTitle("Nouvel Etablissement");
        this.setVisible(true);
        this.setSize(new Dimension(300, 300));
        this.setLocation(500, 500);
    }

    private class ActionAjouter implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            JButton bouton = (JButton) evt.getSource();
            if (bouton == PageEtablissement.this.bValider) {
                String nom = PageEtablissement.this.nomTextField.getText();
                String adresse = PageEtablissement.this.adresseTextField.getText();
                String codep = PageEtablissement.this.codePTextField.getText();
                String ville = PageEtablissement.this.villeTextField.getText();

                Etablissement etablissement = new Etablissement(nom, adresse, codep, ville);
                try {
                    PageEtablissement.this.modele.ajouterEtablissement(etablissement);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

            new PageSignUpPro(PageEtablissement.this.modele);
            PageEtablissement.this.dispose();
        }
    }
}