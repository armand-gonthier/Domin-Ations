package DominAtions;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class Game {
	
	private int nbrJoueur;
	private Joueur joueurs[];
	private ArrayList<Domino> listDominos;
	
	JFrame fenetre;
	HashMap<String, JTextField> texts;

	public Game(int nbrJoueur, JFrame fenetre) {
		this.nbrJoueur = nbrJoueur;
		this.joueurs = new Joueur[this.nbrJoueur];
		
		this.listDominos = this.readCsvDomino();
		
		this.fenetre = fenetre;
		
	}
	
	public void initFrame() {
		this.fenetre.getContentPane().removeAll();
		this.fenetre.repaint();
		
		this.fenetre.setSize(400, this.nbrJoueur * 100); 
	    
	    this.fenetre.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    c.ipadx = 0;
	    c.ipady = 0;
	    
	    this.texts = new HashMap<String, JTextField>();
	    
	    int i = 0;
	    for(; i < this.nbrJoueur; i++) {
	    		c.gridx = 0;
	    		c.gridy = i;
	    		this.fenetre.add(new JLabel("Joueur " + (1+i) +", quel est votre nom : "), c);
	    		c.gridx = 1;
	    		JTextField nom = new JTextField();
	    		nom.setPreferredSize( new Dimension( 200, 24 ) );
	    		this.texts.put("nom" + i, nom);
	    		this.fenetre.add(nom, c);
	    }
	    
	    c.gridx = 1;
	    c.gridy = i + 2;
	    
	    JButton buttonSubmit;
		buttonSubmit = new JButton("Valider");
		this.fenetre.add(buttonSubmit, c);
		buttonSubmit.addActionListener(new ButtonSubmit(this));
		
	}
	
	class ButtonSubmit implements ActionListener {
		
		Game g;
		
		public ButtonSubmit(Game g) {
			super();
			this.g = g;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.g.initJoueurs();
		}
	}
	
	public void begin() {
		int nbDom;
		if(this.nbrJoueur == 3) {
			nbDom = 3;
		} else {
			nbDom = 4;
		}

		Tour turn = new Tour(nbDom, this.joueurs, this.listDominos, this.fenetre, this);
		turn.firstTurn();

	}
	
	
	public void initJoueurs() {
		int nbRois;
		if(this.nbrJoueur == 2) {
			nbRois = 2;
		} else {
			nbRois = 1;
		}
		
		for(int i = 0; i < this.nbrJoueur; i++) {
			String name = this.texts.get("nom" + i).getText();
			this.joueurs[i] = new Joueur(name, nbRois, i);
		}
		
		this.begin();
	}
	
	public void printDominosList() {
		for(int i = 0; i < this.listDominos.size(); i++) {
			this.listDominos.get(i).print();
		}
	}
	
	public ArrayList<Domino> choixDomino(ArrayList<Domino> allDominos) {
		int nbrRetire = 0;
		
		switch(this.nbrJoueur) {
			case 3:
				nbrRetire = 12;
				break;
			case 4:
				break;
			default:
				nbrRetire = 24;
				break;
		}
		
		for(int i = 0; i < nbrRetire; i++) {
			int randomIndex = (int)((Math.random() * allDominos.size()));
			allDominos.remove(randomIndex);
		}
		
		Collections.shuffle(allDominos);
		return allDominos;
	}
	
	public ArrayList<Domino> readCsvDomino(){
		
		ArrayList<Domino> allDominos = new ArrayList<Domino>();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("dominos.csv"));
			
			String line = br.readLine(); // Pour sauter les headers
			
	        while((line = br.readLine()) != null && !line.isEmpty()){
	        		String[] info = line.split(",");
	        		Terrain[] terrains = new Terrain[2];
	        		terrains[0] = new Terrain(info[1]);
	        		terrains[1] = new Terrain(info[3]);
	        		int[] couronnes = new int[2];
	        		couronnes[0] = Integer.parseInt(info[0]);
	        		couronnes[1] = Integer.parseInt(info[2]);
	        		int numero = Integer.parseInt(info[4]);
	        		
	        		Domino dom = new Domino(terrains, couronnes, numero);
	        		allDominos.add(dom);
	        }
	        br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	
		return choixDomino(allDominos);
	}


	public void remplissagecase(Joueur j) {
		Royaume royaume = j.royaume;
		
		for(int x=0; x < royaume.terrain.length; x++){
			for(int y=0; y < royaume.terrain[y].length; y++){
				j.listTer.add(royaume.terrain[x][y]);
			}
		}
	}
	
	public java.util.ArrayList<Terrain> memeTerrain(Terrain ter){
		ArrayList<Terrain> sameTer = new ArrayList<>();
		if (ter.type == Type.Chateau) {
			return null;
		}
		
		Terrain caseA = new Terrain(ter.X, ter.Y-1);
		Terrain caseB = new Terrain(ter.X, ter.Y+1);
		Terrain caseC = new Terrain(ter.X-1, ter.Y);
		Terrain caseD = new Terrain(ter.X+1, ter.Y);
		
		if(ter.type == caseA.type) {
			sameTer.add(caseA);
		}if(ter.type == caseB.type) {
			sameTer.add(caseB);
		}if(ter.type == caseC.type) {
			sameTer.add(caseC);
		}if(ter.type == caseD.type) {
			sameTer.add(caseD);
		}
		
		return sameTer;
		
}

	public void delimiterzone(Joueur joueur) {
		for(Terrain monTer : joueur.listTer) {
			boolean isContained = false;
			ArrayList<Terrain> liste = joueur.listZone;
			if(liste.contains(monTer)) {
				isContained = true;
			}
			
			if(!isContained) {
				for(Terrain terrains : memeTerrain(monTer)) {
					joueur.listZone.add(terrains);
				}
			}
		}
		
	}

	public void scoreTotal(Joueur joueur) {
		ArrayList<Integer> scoreZone = new ArrayList<>();
		
		
		ArrayList<Terrain> list = joueur.listZone;
			int nbrCouronneZone = 0;
			int nbrCaseZone = 0;
			
			for(Terrain ter: list) {
				nbrCaseZone++;
				nbrCouronneZone += ter.nbCouronne;
			}
			int scoreinter=nbrCouronneZone*nbrCaseZone;
			scoreZone.add(scoreinter);
		
		int scoreFinal = 0;
		for(int score: scoreZone) {
			scoreFinal+=score;
		}
		
		joueur.setScore(scoreFinal);
	}
}

