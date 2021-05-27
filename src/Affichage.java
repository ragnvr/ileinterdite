import javax.swing.*;
import java.awt.*;

public class Affichage extends JPanel {

    private Model modele;

    public Affichage(Model modele) {
        this.modele = modele;
        Dimension dim = new Dimension(400, 400);
        this.setPreferredSize(dim);
    }

//    public void update(){
//        repaint();
//    }

    //meth aff heritee de jpanel
    //  @Override
//    public void paintComponent(Graphics g) {
//        //preparer l espace utilisee
//        super.repaint();
//        for (int i=0; i<42; i++){
//            for (int j=0; j<42; j++){
//                paintCellule(g, modele.getCellule(i,j), i, j);
//            }
//        }
//
//    }
//    private void paintCellule(Graphics g, Model.Cellule cellule, int i, int j) {
//        if (cellule.estVivante()) g.setColor(Color.BLACK);
//        else {
//            g.setColor(Color.WHITE);
//            g.fillRect(i * 10, j * 10, 10, 10);//arg ! coord dun coin du rect, larg, hauteur
//        }
//    }
//}
}