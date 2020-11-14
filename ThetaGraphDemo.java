import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ThetaGraphDemo {
	static ArrayList<Vertex> vertices;
	static ArrayList<Edge> edges;
	static CoordinatePanel coordPanel;
	static JPanel bottomPanel;
	static ThetaGraphConstructor graphConstructor;
	static int numSector;
	
	public static void main(String[] args){
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		graphConstructor = new ThetaGraphConstructor();
		numSector = 2;
		
		JFrame jf = new JFrame("ThetaGraphDemo");
		jf.setSize(800,800);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		coordPanel = new CoordinatePanel(new BorderLayout());
		
		bottomPanel = new JPanel(new BorderLayout());
		String[] n = {"n = 2","n = 3","n = 4","n = 5","n = 6","n = 7",
		              "n = 8","n = 9","n = 10","n = 11","n = 12"};
		JComboBox chooseN = new JComboBox(n);
		chooseN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				numSector = chooseN.getSelectedIndex() + 2;
				System.out.println(numSector);
			}
		});
		JButton construct = new JButton("Construt Theta Graph");
		construct.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				System.out.println("starting!");
				coordPanel.remove(ThetaGraphDemo.bottomPanel);
				graphConstructor.start();
			}
		});
		bottomPanel.add(chooseN, BorderLayout.LINE_START);
		bottomPanel.add(construct, BorderLayout.LINE_END);
		
		coordPanel.add(bottomPanel, BorderLayout.PAGE_END);		
		coordPanel.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent m){
				vertices.add(new Vertex(m.getX(), m.getY()));
				coordPanel.repaint();
			}
		});
		
		jf.add(coordPanel);
		jf.setVisible(true);
	}
}

class CoordinatePanel extends JPanel{
	public CoordinatePanel(BorderLayout bl){
		super(bl);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//draw edges
		g.setColor(Color.GRAY);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(5f));
		for(Edge e: ThetaGraphDemo.edges)
			g2d.drawLine(e.v1.x, e.v1.y, e.v2.x, e.v2.y);
		
		//draw vertices
		int size = 10;
		g.setColor(Color.BLACK);
		for(Vertex v: ThetaGraphDemo.vertices){			
			g.fillOval(v.x-(size/2), v.y-(size/2), size, size);
			g.drawString("(" + v.x + "," + v.y + ")", v.x, v.y + 10);
		}
	}
}

class StartConstructionListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent arg0){
		ThetaGraphDemo.coordPanel.remove(ThetaGraphDemo.bottomPanel);
		ThetaGraphDemo.graphConstructor.start();
	}
}

class ThetaGraphConstructor extends Thread{
	@Override
	public void run(){
		for (int i = 0; i < ThetaGraphDemo.vertices.size(); i++){       // for all vertices,
			Vertex[] closestVertices = new Vertex[ThetaGraphDemo.numSector];
			double[] closestVerticesDistance = new double[ThetaGraphDemo.numSector];
			Arrays.fill(closestVerticesDistance, Double.POSITIVE_INFINITY);
			for (int j = i+1; j < ThetaGraphDemo.vertices.size(); j++){ // look at each unexamined vertex, and
				// classify each of those vertex as a member of a sector by:
				int dx = ThetaGraphDemo.vertices.get(j).x - ThetaGraphDemo.vertices.get(i).x;
				int dy = ThetaGraphDemo.vertices.get(j).y - ThetaGraphDemo.vertices.get(i).y;
				
				double angle; // finding the angle of the vector from vertex to vertex
				if (dx == 0){ // same x coord (arctan would be undefined)
					if (dy > 0)
						angle = Math.PI / 2;
					if (dy < 0)
						angle = Math.PI / -2;
					else; // same coordinates
						continue;
				}
				else if (dx > 0)
					angle = Math.atan((float)dy / dx);
				else
					angle = Math.atan((float)dy / dx) + Math.PI;
				if (angle < 0)
					angle += 2 * Math.PI;
				int sector = (int)((angle * ThetaGraphDemo.numSector) / (2 * Math.PI));
				
				// find angle of bisector given sector number,
				// find unit vector in its direction,
				// and take the cross product of said unit vector and vector from vertex to vertex to find projected distance
				double bisectorAngle = (sector + 0.5) *  (2 * Math.PI / ThetaGraphDemo.numSector);
				double projectedDistance = Math.hypot(Math.cos(bisectorAngle) * dx, Math.sin(bisectorAngle) * dy);
				
				if (closestVerticesDistance[sector] > projectedDistance){
					closestVerticesDistance[sector] = projectedDistance;
					closestVertices[sector] = ThetaGraphDemo.vertices.get(j);
				}
			}
			Vertex v1 = ThetaGraphDemo.vertices.get(i);
			for (Vertex v2: closestVertices){
				if (v2 != null){
					try{
						ThetaGraphDemo.edges.add(new Edge(v1, v2));
						ThetaGraphDemo.coordPanel.repaint();
						Thread.sleep(250);
					} catch (InterruptedException e){}
				}
			}
		}
	}
}

class Vertex{
	int x, y;
	public Vertex(int x, int y){
		this.x = x;
		this.y = y;
	}
}

class Edge{
	Vertex v1, v2;
	public Edge(Vertex v1, Vertex v2){
		this.v1 = v1;
		this.v2 = v2;
	}
}