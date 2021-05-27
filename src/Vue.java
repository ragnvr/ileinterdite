import javax.swing.*;
import java.awt.*;

//fenetre principale, zone d affichage, bouton
public class Vue {
    private JFrame frame;
    private VueGrille grille;
    private VueCommandes commandes;

    //fenetre graph vient de swing
    //plus 2 composantes

    public Vue (Model modele) {
        frame = new JFrame();
        frame.setTitle("L'île interdite ");

        //2 autre compo
        frame.setLayout(new BorderLayout());
        grille = new VueGrille(modele);
        commandes = new VueCommandes(modele);

        frame.add(new Affichage(modele));
        frame.add(grille, BorderLayout.WEST);
        frame.add(commandes, BorderLayout.SOUTH);

        //pour placer les elements "layout"
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
