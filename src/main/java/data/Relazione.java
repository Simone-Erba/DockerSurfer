package data;

import java.io.Serializable;

/**
 *   a data class for represent the relationship between images: father, child or brother
 * @author Simone-Erba
 
 */
public class Relazione implements Serializable {
	int layersinComune;
	boolean padre;
	boolean figlio;
	boolean fratello;

	public int getLayersinComune() {
		return layersinComune;
	}

	public void setLayersinComune(int layersinComune) {
		this.layersinComune = layersinComune;
	}

	public boolean isPadre() {
		return padre;
	}

	public void setPadre(boolean padre) {
		this.padre = padre;
	}

	public boolean isFiglio() {
		return figlio;
	}

	public void setFiglio(boolean figlio) {
		this.figlio = figlio;
	}

	public boolean isFratello() {
		return fratello;
	}

	public void setFratello(boolean fratello) {
		this.fratello = fratello;
	}

	public Relazione(int layersinComune, boolean padre, boolean figlio, boolean fratello) {
		super();
		this.layersinComune = layersinComune;
		this.padre = padre;
		this.figlio = figlio;
		this.fratello = fratello;
	}

}
