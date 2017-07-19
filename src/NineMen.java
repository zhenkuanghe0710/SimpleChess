import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.Math
;
public class NineMen extends JPanel {
	private final int SIZE = 7;
	private JPanel mainPanel;
	private ImagePanel gamePanel;		//
	private JPanel playerPanel1;   	// stores pieces of players
	private JPanel playerPanel2;
	private JLabel infoLabel;
	private JButton resetButton;
	private Piece currentPiece;		// just-selected piece
	private Piece[][] pieces = new Piece[SIZE][SIZE];
	private ArrayList<Piece> playerPieces1;
	private ArrayList<Piece> playerPieces2;
	private int playerHit1;
	private int playerHit2;
	private int playerTurn;
	private int destroyTurn;       // When user has a right to destroy any piece of enemy

	public NineMen() {
		playerPieces1 = new ArrayList<Piece>();
		playerPieces2 = new ArrayList<Piece>();
		setLayout(new BorderLayout());
		gamePanel = new ImagePanel();
		gamePanel.setLayout(new GridLayout(SIZE, SIZE));
		gamePanel.setPreferredSize(new Dimension(SIZE * 70, SIZE * 70));
		gamePanel.setBackground(Color.white);
		gamePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				pieces[i][j] = new Piece(0, i, j);
				pieces[i][j].addActionListener(new Listener());
				gamePanel.add(pieces[i][j]);
			}
		}

		playerPanel1 = new JPanel(new GridLayout(SIZE + 2, 1));
		playerPanel1.setBackground(Color.white);
		playerPanel1.setBorder(BorderFactory.createLineBorder(Color.black));

		playerPanel2 = new JPanel(new GridLayout(SIZE + 2, 1));
		playerPanel2.setBackground(Color.white);
		playerPanel2.setBorder(BorderFactory.createLineBorder(Color.black));

		infoLabel = new JLabel();
		infoLabel.setPreferredSize(new Dimension(SIZE * 70, 70));


		resetButton = new JButton("Reset Game");
		resetButton.setPreferredSize(new Dimension(SIZE * 70, 70));
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}

		});

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(gamePanel, BorderLayout.CENTER);
		mainPanel.add(infoLabel, BorderLayout.NORTH);
		mainPanel.add(resetButton, BorderLayout.SOUTH);

		add(mainPanel, BorderLayout.CENTER);
		add(playerPanel1, BorderLayout.WEST);
		add(playerPanel2, BorderLayout.EAST);

		reset();
	}

	public void reset() {
		destroyTurn = 0;
		playerTurn = 1;
		playerHit1 = 0;
		playerHit2 = 0;
		gamePanel.setEnabled(true);

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				 // If the piece is in the center
				if (i == SIZE/2 && j == SIZE/2 ) {
					pieces[i][j].setID(3);
				// If the piece is in diagonal line
				} else if (i == j || SIZE - 1 - i == j ||
						   i == SIZE/2 || j == SIZE/2) {
					pieces[i][j].setID(0);
				} else {
					pieces[i][j].setID(3);
				}
			}
		}

		playerPanel1.removeAll();
		playerPieces1.clear();
		for (int i = 0; i < SIZE + 2; i++) {
			Piece piece = new Piece(0, -1, -1);
			piece.setEnabled(false);
			piece.setID(1);
			playerPieces1.add(piece);
			playerPanel1.add(piece);

		}

		playerPanel2.removeAll();
		playerPieces2.clear();
		for (int i = 0; i < SIZE + 2; i++) {
			Piece piece = new Piece(0, -1, -1);
			piece.setEnabled(false);
			piece.setID(2);
			playerPieces2.add(piece);
			playerPanel2.add(piece);

		}

		infoLabel.setText("Blue-Player's Turn");
		recharge();
		repaint();
		updateUI();
	}

	/**
	 * Check if a piece can be moved to new postion
	 */
	public boolean canMove(Piece oldPosition,Piece newPosition) {
		boolean result = false;
		int X1 = oldPosition.getXCoor();
		int X2 = newPosition.getXCoor();
		int Y1 = oldPosition.getYCoor();
		int Y2 = newPosition.getYCoor();
		int stepX = Math.abs(X1 - X2);
		int stepY = Math.abs(Y1 - Y2);
		if (newPosition.getID() == 0) {
			if (X1 == -1) {
				result = true;
			} else if (X1 == X2 && stepY <= Math.abs(SIZE/2 - X1)) {
				result = true;
			} else if (Y1 == Y2 && stepX <= Math.abs(SIZE/2 - Y1)) {
				result = true;
			} else if (X1 == X2 && X1 == SIZE/2 && stepY == 1) {
				result = true;
			} else if (Y1 == Y2 && Y1 == SIZE/2 && stepX == 1) {
				result = true;
			}
		}

		return result;
	}
	/**
	 * Check if a piece is on the a straight row with 2 others
	 */
	public boolean isStraightRow(Piece piece) {
		boolean result = false;
		int x = piece.getXCoor();
		int y = piece.getYCoor();

		// Check vertically
		if (pieces[y][y].getID() == playerTurn &&
			pieces[SIZE - 1 - y][y].getID() == playerTurn &&
			pieces[SIZE/2][y].getID() == playerTurn) {
			result = true;
		}

		if (y == SIZE/2 && x > SIZE/2) {
			if (pieces[4][SIZE/2].getID() == playerTurn &&
				pieces[5][SIZE/2].getID() == playerTurn &&
				pieces[6][SIZE/2].getID() == playerTurn) {
				result = true;

			}

		}
		if (y == SIZE/2 && x < SIZE/2) {
			if (pieces[0][SIZE/2].getID() == playerTurn &&
				pieces[1][SIZE/2].getID() == playerTurn &&
				pieces[2][SIZE/2].getID() == playerTurn) {
				result = true;

			}
		}


		// Check horizontally
		if (pieces[x][x].getID() == playerTurn &&
			pieces[x][SIZE - 1 - x].getID() == playerTurn &&
			pieces[x][SIZE/2].getID() == playerTurn) {
			result = true;

		}
		if (x == SIZE/2 && y > SIZE/2) {
			if (pieces[SIZE/2][4].getID() == playerTurn &&
				pieces[SIZE/2][5].getID() == playerTurn &&
				pieces[SIZE/2][6].getID() == playerTurn) {
				result = true;
			}

		}
		if (x == SIZE/2 && y < SIZE/2) {
			if (pieces[SIZE/2][0].getID() == playerTurn &&
				pieces[SIZE/2][1].getID() == playerTurn &&
				pieces[SIZE/2][2].getID() == playerTurn) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * Move a piece to another position
	 */
	public void move(Piece oldPosition, Piece newPosition) {
			newPosition.setID(oldPosition.getID());
			oldPosition.setID(0);
	}

	/**
	 * Get a piece from playerPanel1 and playerPanel2
	 * depending on which turn of players
	 */
	public boolean recharge() {
		boolean result = false;
		if (playerPieces1.size() > 0 && playerTurn == 1) {
			currentPiece = playerPieces1.get(0);
			currentPiece.setEnabled(true);
			playerPieces1.remove(0);
			result = true;
		} else if (playerPieces2.size() > 0 && playerTurn == 2) {
			currentPiece = playerPieces2.get(0);
			currentPiece.setEnabled(true);
			playerPieces2.remove(0);
			result = true;
		} else {
			currentPiece = null;
		}

		return result;
	}

	/**
	 * Switch Player
	 */
	private void switchPlayer() {
		if (playerTurn == 1) {
			playerTurn = 2;
			infoLabel.setText("Red-Player's Turn");

		} else {
			playerTurn = 1;
			infoLabel.setText("Blue-Player's Turn");
		}
	}

	private boolean checkWinner() {
		if (playerHit1 == SIZE) {
			infoLabel.setText("Blue-Player Won!");
			gamePanel.setEnabled(false);
			return true;
		} else if (playerHit2 == SIZE) {
			infoLabel.setText("Red-Player Won!");
			gamePanel.setEnabled(false);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Listener Class
	 */
	class Listener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			Piece clickedPiece = (Piece)event.getSource();
			//If current user does not have a right to destroy a piece of the enemy
			if (destroyTurn == 0) {

				// If there is no picked piece yet
				if (currentPiece == null) {

					// Player picks a piece to move if it is his/her turn
					if (clickedPiece.getID() == playerTurn)	{
						currentPiece = clickedPiece;
					}
				} else if (currentPiece.getID() == clickedPiece.getID() && clickedPiece.getID() == playerTurn) {
					currentPiece = clickedPiece;

				// If he/she wants to move picked piece to another position
				} else if(clickedPiece != currentPiece) {

					/**
					 * If a piece can be moved:
					 * move it, then switch turn
					 */
					if (canMove(currentPiece, clickedPiece)) {
						move(currentPiece, clickedPiece);
						//currentPiece = null;
						// Check if the piece just put is in the same line with 2 others
						if (isStraightRow(clickedPiece)) {
							destroyTurn = playerTurn;
							if (playerTurn == 1) {
								infoLabel.setText("Blue-Player, please pick a red piece to destroy");
							} else {
								infoLabel.setText("Red-Player, please pick a red piece to destroy");
							}
						} else {
							switchPlayer();
							// If current player still has pieces which have not been put
							// Get another piece
							recharge();
						}

					}
				}

			// If current player does not have a right to destroy a piece of the enemy
			// Destroy the piece player picks the switch turn
			} else if (clickedPiece.getID() == 2 && destroyTurn == 1 ||
				       clickedPiece.getID() == 1 && destroyTurn == 2) {
				clickedPiece.setID(0);
				if (playerTurn == 1) {
					playerHit1++;
				} else {
					playerHit2++;
				}
				if (!checkWinner()) {
					destroyTurn = 0;
					switchPlayer();
					recharge();
				}

			}
		}

	}

	public static void main(String args[]) {
		//Create a new JFrame
		JFrame frame = new JFrame ("Game");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		NineMen panel = new NineMen();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}

class ImagePanel extends JPanel {

	public void paintComponent(Graphics g) {
		g.drawImage(new ImageIcon("bg1.png").getImage(), 0, 0, null);
	}

}