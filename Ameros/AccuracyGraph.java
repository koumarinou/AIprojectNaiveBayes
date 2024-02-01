import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccuracyGraph extends JPanel {
    private List<Integer> trainingSizes;
    private List<Double> trainingAccuracies;
    private List<Double> testAccuracies;
   

    public AccuracyGraph(List<Integer> trainingSizes, List<Double> trainingAccuracies, List<Double> testAccuracies) {
        this.trainingSizes = trainingSizes;
        this.trainingAccuracies = trainingAccuracies;
        this.testAccuracies = testAccuracies;
        
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAxes(g);
        drawAccuracyGraph(g, trainingSizes, trainingAccuracies, Color.RED);
        drawAccuracyGraph(g, trainingSizes, testAccuracies, Color.BLUE);
       
    }

    private void drawAxes(Graphics g) {
        g.setColor(Color.BLACK);
        // x
        g.drawLine(50, getHeight() - 50, getWidth() - 50, getHeight() - 50);
        // y
        g.drawLine(50, 50, 50, getHeight() - 50);
    
        // times x
        for (int size : trainingSizes) {
            int xPosition = size + 40;
            g.drawString(String.valueOf(size), xPosition, getHeight() - 35);
        }
    
        // times y
        for (double i = 0; i <= 1; i += 0.1) { 
            int yPosition = getHeight() - 50 - (int)(i * (getHeight() - 100));
            g.drawString(String.format("%.1f", i), 25, yPosition);
        }
    }

    private void drawAccuracyGraph(Graphics g, List<Integer> sizes, List<Double> accuracies, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        if (sizes.size() < 2) return;
        for (int i = 0; i < sizes.size() - 1; i++) {
            int x1 = sizes.get(i) + 50;
            int y1 = getHeight() - 50 - (int) (accuracies.get(i) * (getHeight() - 100));
            int x2 = sizes.get(i + 1) + 50;
            int y2 = getHeight() - 50 - (int) (accuracies.get(i + 1) * (getHeight() - 100));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

}