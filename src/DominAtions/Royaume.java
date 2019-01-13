package DominAtions;

public class Royaume {
	
	int X = 9;
	int Y = 9;
	Terrain[][] terrain;
	
	public Royaume() {
		this.terrain = new Terrain[X][Y];
		this.initRoyaume();
	}
	
	public void initRoyaume() {
		for(int x = 0; x < this.terrain.length; x++) {
			for(int y = 0; y < this.terrain[x].length; y++) {
				this.terrain[x][y] = new Terrain();
			}
		}
		this.terrain[4][4].setType("Chateau");
	}
	
	public boolean placerDomino(Domino domino, int x, int y) {
		Type terrain1 = domino.terrain1.type;
		Type terrain2 = domino.terrain2.type;
		
		if(this.terrain[x][y].type != Type.VIDE && this.terrain[x+1][y].type != Type.VIDE) {
			return false;
		} else {
			if( (this.terrain[x-1][y].type == terrain1 || this.terrain[x][y+1].type == terrain1 || this.terrain[x][y-1].type == terrain1
					|| this.terrain[x-1][y].type == Type.Chateau || this.terrain[x][y+1].type == Type.Chateau || this.terrain[x][y-1].type == Type.Chateau)) {
				this.terrain[x][y] = new Terrain(terrain1.toString(), domino.nbCouronnes1);
				this.terrain[x+1][y] = new Terrain(terrain2.toString(), domino.nbCouronnes2);
				return true;
			}
		}
		
		return false;
		
	}
	
	public void printRoyaume() {
		for(int x = 0; x < this.terrain.length; x++) {
			System.out.print("|");
			for(int y = 0; y < this.terrain[x].length; y++) {
				this.terrain[x][y].print();
				System.out.print("|");
			}
			System.out.println("");
		}
	}
}
