//import javafx.scene.control.Cell;

import java.awt.*;
import java.lang.Enum;
import java.util.Random;
import java.util.ArrayList;

//zones sp´eciales, les cl´es que peut poss´eder un joueur, ´
//et l’action de r´ecup´erer un artefact. Etendre l’effet du bouton ´ « fin de tour » pour que le
//joueur dont c’´etait le tour re¸coive, al´eatoirement, une cl´e ou rien.
public class Model extends Observable {

    /**attributs de la classe
     *
     */
    public static final int Dimension = 6;
    public Vue vue;
    private Cellule[][] cellules;
    protected ArrayList <Player> allPlayers = new ArrayList<>();
    private int indice = 0;
    private int nbArtefacts = 0;
    private final static int chancePremiereCle = 2;
    private final static int chanceDeuxiemeCle = 4;
    private ArrayList<Cellule.Zone> keysDispo = new ArrayList<>();
    private int nbKeysDispo = 4;
    private boolean vueKey = true;
    private boolean finDeJeu = false;
    private int difficulte; // 0 facile, 1 difficile

    /**constructeur du modele
     *
     */
    public Model(int difficult) {
        this.difficulte=difficult;
        cellules = new Cellule[Dimension][Dimension];
        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {
                cellules[i][j] = new Cellule(this, i, j); //creation des cellules
            }
        }
        this.keysDispo.add(Cellule.Zone.eau);
        this.keysDispo.add(Cellule.Zone.terre);
        this.keysDispo.add(Cellule.Zone.feu);
        this.keysDispo.add(Cellule.Zone.air);
        this.initPlayers();
        this.init_model();
        this.initVue();
    }


    public boolean isFinDeJeu() {
        return finDeJeu;
    }

    /** initialise 4 joueurs
     *
     */
    private void initPlayers(){
        //creation des joueurs
        Player p1 = new Player(this ,2,1, "M. Gruau");
        Player p2 = new Player(this,1,1, "Dahalani");
        Player p3 = new Player(this,1,2, "Markandu");
        Player p4 = new Player(this,2,2, "De Souza");
        //ajoute les joueurs dans l ArrayList
        allPlayers.add(p1);
        allPlayers.add(p2);
        allPlayers.add(p3);
        allPlayers.add(p4);
        //ajoute les joueurs aux cellules correspondantes
        this.addPlayer(p1.x, p1.y, p1);
        this.addPlayer(p2.x, p2.y, p2);
        this.addPlayer(p3.x, p3.y, p3);
        this.addPlayer(p4.x, p4.y, p4);
    }

    /** return tous les joueurs
     *
     * @return
     */
    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    /**fonctions permetant les deplacements du joueur
     *
     * @param p
     */
    public void recule(Player p){
        for (Player p1 : this.allPlayers) {
            if (p1.role == p.role) {
                p1.recule();
                return;
            }
        }
    }
    /**fonctions permetant les deplacements du joueur
     *
     * @param p
     */
    public void avance(Player p){
        for (Player p1 : this.allPlayers) {
            if (p1.role == p.role) {
                p1.avance();
                return;
            }
        }
    }
    /**fonctions permetant les deplacements du joueur
     *
     * @param p
     */
    public void gauche(Player p){
        for (Player p1 : this.allPlayers) {
            if (p1.role == p.role) {
                p1.gauche();
                return;
            }
        }
    }
    /**fonctions permetant les deplacements du joueur
     *
     * @param p
     */
    public void droite(Player p){
        for (Player p1 : this.allPlayers) {
            if (p1.role == p.role) {
                p1.droite();
                return;
            }
        }
    }

    /**cree une vue pour le premier joueur
     *
     */
    private void initVue(){
        this.allPlayers.get(0).setMainPlayer();
        this.vue = new Vue(this);
    }

    /**
     *  ajoute une cle a 1 joueur selon les cles disponibles
     *  */
    private void addKeysOrNot(Player p){
        int rnd;
        if (nbKeysDispo == 0) return; //plus de cle
        if ( p.getNbKeys() >= 2 ) return;
        for (Cellule.Zone zone: keysDispo){  //chaque cle dispo

            if (p.getNbKeys() == 0) {
                rnd = new Random().nextInt(chancePremiereCle);
                if ( !(rnd ==0) ) return;
                p.addKey(zone);
                this.keysDispo.remove(zone);
                nbKeysDispo--;
                return;
            }
            if (p.getNbKeys() == 1) {
                rnd = new Random().nextInt(chanceDeuxiemeCle);
                if (!(rnd ==0)) return;
                p.addKey(zone);
                this.keysDispo.remove(zone);
                nbKeysDispo--;
                return;
            }
        }
    }

    /**passe au joueur suivant et inonde les cases
     *
     */
    public Player changementJoueur(){
        this.allPlayers.get(indice).setMainPlayer(); //enleve le statut main Char a l ancien joueur
        this.indice++;
        if (indice == 4) this.indice = 0;
        this.allPlayers.get(indice).setMainPlayer(); //defini le nouveau joueur comme Main Char
        addKeysOrNot(this.allPlayers.get(indice));
        notifyObservers();
        inonde(0);
        return this.allPlayers.get(indice);
    }

    /**ajoute le player dans la cellule x y
     *
     * @param x
     * @param y
     * @param p
     */
    public void addPlayer(int x, int y, Player p){
        this.cellules[x][y].addPlayer(p);
        notifyObservers();
    }

    /**supprime le player dans la cellule x y
     *
     * @param x
     * @param y
     * @param p
     */
    public void deletePlayer(int x, int y, Player p){
        this.cellules[x][y].deletePlayer(p);
    }


    /** defini les zones du model */
    private void init_model() {

        //creation des 4 elements dans la liste
        ArrayList<Cellule.Zone> zone = new ArrayList<>();
        zone.add(Cellule.Zone.feu);
        zone.add(Cellule.Zone.air);
        zone.add(Cellule.Zone.eau);
        zone.add(Cellule.Zone.terre);

        int rnd = new Random().nextInt(zone.size());

        Cellule.Zone element1 = zone.get(rnd);
        zone.remove(rnd);
        rnd = new Random().nextInt(zone.size());
        Cellule.Zone element2 = zone.get(rnd);
        zone.remove(rnd);
        rnd = new Random().nextInt(zone.size());
        Cellule.Zone element3 = zone.get(rnd);
        zone.remove(rnd);
        rnd = new Random().nextInt(zone.size());
        Cellule.Zone element4 = zone.get(rnd);
        zone.remove(rnd);

        //affecte aleatoirement chaque quart de la grille a un element
        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {

                if (i < Dimension / 2 && j < Dimension / 2) {
                    this.cellules[i][j].changeZone(element1);
                }
                if (i >= Dimension / 2 && j < Dimension / 2) {
                    this.cellules[i][j].changeZone(element2);
                }
                if (i < Dimension / 2 && j >= Dimension / 2) {
                    this.cellules[i][j].changeZone(element3);
                }
                if (i >= Dimension / 2 && j >= Dimension / 2) {
                    this.cellules[i][j].changeZone(element4);
                }
            }
        }
        this.initCellulesSpeciales();
    }

    /** cree les artefacts ainsi que l heliport
     *
     */
    private void initCellulesSpeciales() {
        //set Artefcat
        int rnd1 ;
        int rnd2 ;
        while (this.nbArtefacts <1){
            rnd1 = new Random().nextInt(getDimension());
            rnd2 = new Random().nextInt(getDimension());
            if ( cellules[rnd1][rnd2].getZone() == Cellule.Zone.air){
                cellules[rnd1][rnd2].setArtefact();
                this.nbArtefacts++;
            }
        }
        while (this.nbArtefacts <2){
            rnd1 = new Random().nextInt(getDimension());
            rnd2 = new Random().nextInt(getDimension());
            if ( cellules[rnd1][rnd2].getZone() == Cellule.Zone.feu){
                cellules[rnd1][rnd2].setArtefact();
                this.nbArtefacts++;
            }
        }
        while (this.nbArtefacts <3){
            rnd1 = new Random().nextInt(getDimension());
            rnd2 = new Random().nextInt(getDimension());
            if ( cellules[rnd1][rnd2].getZone() == Cellule.Zone.eau){
                cellules[rnd1][rnd2].setArtefact();
                this.nbArtefacts++;
            }
        }
        while (this.nbArtefacts <4){
            rnd1 = new Random().nextInt(getDimension());
            rnd2 = new Random().nextInt(getDimension());
            if ( cellules[rnd1][rnd2].getZone() == Cellule.Zone.terre){
                cellules[rnd1][rnd2].setArtefact();
                this.nbArtefacts++;
            }
        }

        //setHeliport
        while(true){
            rnd1 = new Random().nextInt(getDimension());
            rnd2 = new Random().nextInt(getDimension());
            if (!cellules[rnd1][rnd2].isArtefact()) {
                cellules[rnd1][rnd2].setHeliportTrue();
                return;
            }
        }

    }

    /** affichage textuel de l etat de la grille */
    public void toString_Etat() {
        for (int i = 0; i < Dimension; i++) {
            System.out.println(" ");
            for (int j = 0; j < Dimension; j++) {
                if (this.cellules[i][j].getEtat() == Cellule.Etat.normal) {
                    System.out.print(" - ");
                }
                if (this.cellules[i][j].getEtat() == Cellule.Etat.inondé) {
                    System.out.print(" I ");
                }
                if (this.cellules[i][j].getEtat() == Cellule.Etat.submergé) {
                    System.out.print(" S ");
                }
            }
        }
    }

    /** affichage textuel de l element de la grille */
    public void toString_Zone() {
        for (int i = 0; i < Dimension; i++) {
            System.out.println((" "));
            for (int j = 0; j < Dimension; j++) {
                if (this.cellules[i][j].getZone() == Cellule.Zone.normal) {
                    System.out.print(" - ");
                }
                if (this.cellules[i][j].getZone() == Cellule.Zone.eau) {
                    System.out.print(" E ");
                }
                if (this.cellules[i][j].getZone() == Cellule.Zone.feu) {
                    System.out.print(" F ");
                }
                if (this.cellules[i][j].getZone() == Cellule.Zone.terre) {
                    System.out.print(" T ");
                }
                if (this.cellules[i][j].getZone() == Cellule.Zone.air) {
                    System.out.print(" A ");
                }
            }
        }
    }

    /** affichage des joueurs presents dans le model */
    public void toString_nomPlayers(){
        for (Player p : this.allPlayers){
            System.out.println(p.role);
        }
    }

    /** affichage textuel des joueurs de la grille supposant un joueur par case */
    public void toString_allPlayers() {
        for (int i = 0; i < Dimension; i++) {
            System.out.println();
            for (int j = 0; j < Dimension; j++) {
                if (cellules[i][j].estVide()) System.out.print(" - ");
                else {
                    for (Player p : this.allPlayers) {
                        if (p.x == i && p.y == j) {
                            System.out.print(' ' + p.role.substring(0,1) + ' ');
                        }
                    }
                }
            }
        }
    }

    public void allPlayersToHelico(){

        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {
                if (cellules[i][j].isHeliport()) {
                    for (Player p : allPlayers) {
                        cellules[p.x][p.y].deletePlayer(p);

                        p.changeCellule(i,j);
                        cellules[p.x][p.y].addPlayer(p);
                    }
                    notifyObservers();
                    return;
                }
            }
        }
    }

    /**
     *remet a zero les nombre de mouvements realises par tous les joueurs
     */
    private void allPlayersToZero(){
                for (Player p : this.allPlayers) {
                    p.nbMovesToZero();
                }
    }

    /**
     *inonde 3 cases aleeatoires
     * verifie si le jeu est termine avant d inonder
     */
    private void inonde(int compteur) {
        if(checkEndGame() || checkWin()){
            this.finDeJeu = true; // le jeu est fini
            return;
        }
        if (difficulte == 0){
            inondeFacile();//si jeu en mode facile

        } else {
            int x, y;
            while (compteur < 3) {
                if (allCellulesSubmergees()) break; //quitter si elles sont toutes submergees
                x = new Random().nextInt(Dimension);
                y = new Random().nextInt(Dimension);
                if (!this.cellules[x][y].isHeliport()) {
                    if (this.cellules[x][y].verifieNormal()) {
                        this.cellules[x][y].changeEtat(Cellule.Etat.inondé);
                        compteur++;
                    } else if (this.cellules[x][y].verifieInonde()) {
                        this.cellules[x][y].changeEtat(Cellule.Etat.submergé);
                        compteur++;
                    }
                }
            }
        }
        allPlayersToZero();
        notifyObservers();
    }


    /** inonde les cases en mode de jeu facile
     *
     */
    private void inondeFacile() {
        if (!oneCellulesIsNormale()) {
            submergeFacile(0);
            return;
        } else {
            int compteur = 0;
            int x, y;
            while (compteur < 3) {
                if (!oneCellulesIsNormale()){
                    submergeFacile(compteur);
                    return;
                }
                x = new Random().nextInt(Dimension);
                y = new Random().nextInt(Dimension);
                if (!this.cellules[x][y].isHeliport()) {
                    if (this.cellules[x][y].verifieNormal()) {
                        this.cellules[x][y].changeEtat(Cellule.Etat.inondé);
                        compteur++;
                    }
                }

            }
        }
    }

    /** submerge les cases en mode de jeu facile
     *
     * @param compteur
     */
    private void submergeFacile(int compteur) {
        int x, y;
        while (compteur < 3) {
            if (allCellulesSubmergees()) return; //quitter si elles sont toutes submergees
            x = new Random().nextInt(Dimension);
            y = new Random().nextInt(Dimension);
            if (!this.cellules[x][y].isHeliport()) {
                if (this.cellules[x][y].verifieInonde()) {
                    this.cellules[x][y].changeEtat(Cellule.Etat.submergé);
                    compteur++;
                }
            }
        }
    }

    /** return la dimension du modele */
    public int getDimension() {
        return this.Dimension;
    }

    /** return la cellule a l indice i, j donne */
    public Cellule getCellule(int i, int j) {
        return this.cellules[i][j];
    }

    /** retourne les voisins d une cellule i,j donnée
     *
     * @param x
     * @param y
     * @return voisins
     */
    public ArrayList voisins(int x, int y) {

        ArrayList<Cellule> voisins = new ArrayList<>();

        try {
            if (!(cellules[x - 1][y].verifieSubmerge())) {
                voisins.add(cellules[x - 1][y]);
            }
        } catch (Exception e) { }

        try {
            if (!(cellules[x + 1][y].verifieSubmerge())) {
                voisins.add(cellules[x + 1][y]);
            }
        } catch (Exception e) { }

        try {
            if (!(cellules[x][y - 1].verifieSubmerge())) {
                voisins.add(cellules[x][y - 1]);
            }
        } catch (Exception e) { }

        try {
            if (!(cellules[x][y + 1].verifieSubmerge())) {
                voisins.add(cellules[x][y + 1]);
            }
        } catch (Exception e) { }

        return voisins;
    }


    /**
     * affiche tous les voisins d une cellule donnee
     * @param x
     * @param y
     */
    public void testvoisins(int x, int y) {
        ArrayList<Cellule> voisins = this.voisins(x, y);
        int i, j;
        for (Cellule c : voisins) {
            i = c.getX();
            j = c.getY();
            System.out.println("x= " + i);
            System.out.println("y= " + j);

            System.out.println();
            System.out.println();
        }
    }

    /**
     * seche une zone si possible
     * @param j1 le joueur qui veut secher une zone autour
     * @param endroit ou secher
     */
    public void seche(Player j1, String endroit) {
        for (Player p1 : this.allPlayers) {
            if (p1.role == j1.role) {
                int x = p1.x;
                int y = p1.y;
                if (p1.nbMoves >= 3) return;
                boolean innondeSuccess = false;
                if (endroit == "milieu") innondeSuccess = secheMilieu(x,y);
                if (endroit == "droite") innondeSuccess = secheDroite(x,y);
                if (endroit == "gauche") innondeSuccess = secheGauche(x,y);
                if (endroit == "haut") innondeSuccess = secheHaut(x,y);
                if (endroit == "bas") innondeSuccess = secheBas(x,y);
                if (innondeSuccess) j1.nbMoves=3;
                return;
            }
        }
    }

    /** seche la cellule en bas de la case donnee
     *
     * @param x
     * @param y
     * @return true si cela a ete fait
     */
    private boolean secheBas(int x, int y) {
        ArrayList <Cellule> voisins = this.voisins(x,y);
        Cellule c = new Cellule(this,x+1,y);
        for (Cellule cell : voisins){
            if (c.getX()==cell.getX()){
                if (c.getY()==cell.getY()) {
                    if (! (cellules[x+1][y].getEtat()== Cellule.Etat.inondé)) return false;
                    cellules[x+1][y].changeEtat(Cellule.Etat.normal);
                    notifyObservers();
                    return true;
                }
            }
        } return false;
    }

    /** seche la cellule en haut de la case donnee
     *
     * @param x
     * @param y
     * @return true si cela a ete fait
     */
    private boolean secheHaut(int x, int y) {
        ArrayList <Cellule> voisins = this.voisins(x,y);
        Cellule c = new Cellule(this,x-1,y);
        for (Cellule cell : voisins){
            if (c.getX()==cell.getX()){
                if (c.getY()==cell.getY()) {
                    if (! (cellules[x-1][y].getEtat()== Cellule.Etat.inondé)) return false;
                    cellules[x-1][y].changeEtat(Cellule.Etat.normal);
                    notifyObservers();
                    return true;
                }
            }
        } return false;
    }

    /** seche la cellule a gauche de la case donnee
     *
     * @param x
     * @param y
     * @return true si cela a ete fait
     */
    private boolean secheGauche(int x, int y) {
        ArrayList <Cellule> voisins = this.voisins(x,y);
        Cellule c = new Cellule(this,x,y-1);
        for (Cellule cell : voisins){
            if (c.getX()==cell.getX()){
                if (c.getY()==cell.getY()) {
                    if (! (cellules[x][y-1].getEtat()== Cellule.Etat.inondé)) return false;
                    cellules[x][y-1].changeEtat(Cellule.Etat.normal);
                    notifyObservers();
                    return true;
                }
            }
        } return false;
    }

    /** seche la cellule a droite de la case donnee
     *
     * @param x
     * @param y
     * @return true si cela a ete fait
     */
    private boolean secheDroite(int x, int y) {
        ArrayList <Cellule> voisins = this.voisins(x,y);
        Cellule c = new Cellule(this,x,y+1);
        for (Cellule cell : voisins){
            if (c.getX()==cell.getX()){
                if (c.getY()==cell.getY()) {
                    if (! (cellules[x][y+1].getEtat()== Cellule.Etat.inondé)) return false;
                    cellules[x][y+1].changeEtat(Cellule.Etat.normal);
                    notifyObservers();
                    return true;
                }
            }
        } return false;
    }

    /** seche la cellule actuelle donnee
     *
     * @param x
     * @param y
     * @return true si cela a ete fait
     */
    private boolean secheMilieu(int x, int y) {
        if (cellules[x][y].getEtat() == Cellule.Etat.inondé ) {
            if (! (cellules[x][y].getEtat()== Cellule.Etat.inondé)) return false;
            cellules[x][y].changeEtat(Cellule.Etat.normal);
            notifyObservers();
            return true;
        }
        return false;
    }

    /** fonction qui alterne la vue */
    public void altVueKeys(boolean b) {
        changeVue(b);
        notifyObservers();
    }

    /** change la vue pour afficher la cle ou l artefact des joueurs */
    private void changeVue(boolean b) {
        this.vueKey = b;
    }

    /** retourne la vue actuelle (true si la vue est pour la cle) */
    public boolean isVueKey() {
        return vueKey;
    }

    /** un joueur j1 donne recupere l artefact de la case ou il est s il possede la cle */
    public void recuperArtefact(Player j1) {
        int i = j1.x;
        int j = j1.y;
        if (cellules[i][j].isArtefact()) { // si la cellule est un artefact
             ArrayList<Cellule.Zone> zonesKeys = j1.clePossede();
             for (Cellule.Zone zone : zonesKeys){ //dans toutes les cles possedes
                 if (zone == cellules[i][j].getZone()){
                     j1.addArtefact(zone); //ajout de l artefact au joueur
                     cellules[i][j].setArtefact();
                     j1.removeKey(zone);//supprime cle au joueur
                     notifyObservers();
                     return;
                 }
             }
        }
    }

    /**verifie si toutes les cellules sont submergees ou si une case artefact est inaccessible
     *
     * @return true si elles le sont
     */
    public boolean checkEndGame() {
        if (allCellulesSubmergees() || checkInstantLose() || HelipIndispo()){
            return true;
        }
        return false;
    }

    /** return true si tous les joueurs sont a l heliport et ont les artefacts */
    public boolean checkWin() {
        boolean win = false;
        int nbArtefactsRecup = 0;
        for (Player p : allPlayers){
            if (p.getCellule().isHeliport()) nbArtefactsRecup += p.getNbArtefacts();
        }
        if (nbArtefactsRecup == 4) win = true;
        return win;
    }


    /** return true une case d artefact est submergee ou inaccessible */
    public boolean checkInstantLose() { //instantLose dans la cellule
        ArrayList<Cellule> voisin;
        int vSub = 0;
        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {
                if (checkCelluleLose(i,j)) return true;
            }
        }
        return false;
    }

    /** verifie si un artefact donne est inaccessible */
    public boolean checkCelluleLose(int x, int y) {

        int i = x;
        int j = y;
        if (cellules[i][j].isArtefact()) {
            if (cellules[i][j].getEtat() == Cellule.Etat.submergé) return true;
            ArrayList<Cellule> voisin = this.voisins(i, j);
            int vSub = 0;
            for (Cellule cellule : voisin) {
                if (cellule.getEtat()== Cellule.Etat.submergé) vSub++;
            }
            if (vSub==voisin.size()) return true;
        }
        return false;
    }

    /**
     *
     * @return true si l heliport est indispo
     *
     */
    public boolean HelipIndispo(){

        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {
                if (cellules[i][j].isHeliport()) {
                    ArrayList<Cellule> voisin = this.voisins(i, j);
                    int vSub = 0;
                    for (Cellule cellule : voisin) {
                        if (cellule.getEtat()== Cellule.Etat.submergé) vSub++;
                    }
                    if (vSub==voisin.size()) return true;
                }
            }
        }
        return false;
    }

    /** return vrai si une cellule ou plus est en etat nomral*/
    public boolean oneCellulesIsNormale(){
        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {
                if (!cellules[i][j].isHeliport()) {
                    if (cellules[i][j].etat == Cellule.Etat.normal) return true;
                }
            }
        } return false;
    }

    /** return faux si une cellule n est pas en etat inonde*/
//    private boolean allCellulesInnondees(){
//        for (int i = 0; i < Dimension; i++) {
//            for (int j = 0; j < Dimension; j++) {
//                if (!cellules[i][j].isHeliport()) {
//                    if (cellules[i][j].etat != Cellule.Etat.inondé) return false;
//                }
//            }
//        } return true;
//    }

    /** return faux si une cellule n est pas en etat submerge*/
    private boolean allCellulesSubmergees(){
        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {
                if (!cellules[i][j].isHeliport()) {
                    if (cellules[i][j].etat != Cellule.Etat.submergé) return false;
                }
            }
        } return true;
    }

    /** le Main joueur donne sa derniere cle au premier joueur sur la meme case */
    public void giveKey(Player p){
    }
    
}











