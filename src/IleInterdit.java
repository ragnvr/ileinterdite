import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class IleInterdit {

    public static void main(String args[]) {

            System.out.println();
            System.out.println();
            System.out.println("Bienvenue dans le jeu de l'île interdite !");
            System.out.println();
            Scanner sc = new Scanner(System.in);
            int difficulte = 2;
            while (!(difficulte == 1 || difficulte==0)){
                System.out.println("Veuillez choisir la difficulté : \n  '0' pour le mode facile \n  '1' pour le mode normal");
                try { difficulte = sc.nextInt();} catch (Exception e){
                    System.out.println("Bien tenté...");
                    difficulte = 1; //par defaut si l utilisateur entre une valeur non attendue
                }
            }
            System.out.println("Difficulte choisi : " + difficulte + "\nForce a toi !");
            Model grille = new Model(difficulte);
    }
}
