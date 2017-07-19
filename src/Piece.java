import javax.swing.*;
import java.awt.*;
public class Piece extends JButton {
	private int id;
	private int xCoor;
	private int yCoor;
	ImageIcon icon1;
	ImageIcon icon2;


	public Piece(int id, int xCoor, int yCoor) {
		icon1 = new ImageIcon("BlueButton.png");
		icon2 = new ImageIcon("RedButton.png");
		setID(id);
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		setPreferredSize(new Dimension(70, 70));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);

	}

	public void setID(int id) {
		this.id = id;
		//setText(Integer.toString(id));   // Put here to check
		if (id == 3) {
			setVisible(false);
		} else if (id == 0) {
			setIcon(null);
		} else if (id == 1) {
			//setIcon(null);
			setIcon(icon1);
		} else if (id == 2) {
			//setIcon(null);
			setIcon(icon2);
		}
		repaint();
		updateUI();
	}

	public int getID() {
		return id;
	}

	 public int getXCoor() {
	 	return xCoor;
	 }

	 public int getYCoor() {
	 	return yCoor;
	 }

	/**
	 * When a piece was chosen
	 * Change its look based on id
	 *
	 */




}