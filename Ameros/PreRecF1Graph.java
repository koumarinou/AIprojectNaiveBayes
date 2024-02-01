import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PreRecF1Graph extends JPanel {
    private List<Integer> trainingSizes;
    private List<Double> trainingPrecisions;
    private List<Double> trainingRecalls;
    private List<Double> trainingF1Scores;
    

    public PreRecF1Graph(List<Integer> trainingSizes, List<Double> trainingPrecisions, List<Double> trainingRecalls, List<Double> trainingF1Scores) {
    this.trainingSizes = trainingSizes;
    this.trainingPrecisions = trainingPrecisions;
    this.trainingRecalls = trainingRecalls;
    this.trainingF1Scores = trainingF1Scores;
    
    setPreferredSize(new Dimension(800, 600));
    }
   

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAxes(g);
        drawGraph(g, trainingSizes, trainingPrecisions, Color.RED);
        drawGraph(g, trainingSizes, trainingRecalls, Color.BLUE);
        drawGraph(g, trainingSizes, trainingF1Scores, Color.GREEN);
    }

    private void drawAxes(Graphics g) {
        g.setColor(Color.BLACK);
        // Axonas x
        g.drawLine(50, getHeight() - 50, getWidth() - 50, getHeight() - 50);
        // Axonas y
        g.drawLine(50, 50, 50, getHeight() - 50);

        // Times axona x
        
        for (int size : trainingSizes) {
            int xPosition = size + 40; 
            g.drawString(String.valueOf(size), xPosition, getHeight() - 35);
        }

        // Times axona y
        for (int i = 0; i <= 10; i++) {
            int yPosition = getHeight() - 50 - (i * (getHeight() - 100) / 10);
            g.drawString(String.format("%.1f", i * 0.1), 25, yPosition);
        }
    }

    private void drawGraph(Graphics g, List<Integer> sizes, List<Double> x, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        if (sizes.size() < 2) return;
        for (int i = 0; i < sizes.size() - 1; i++) {
            int x1 = sizes.get(i) + 50;
            int y1 = (int) ((1 - x.get(i)) * (getHeight() - 100)) + 50;
            int x2 = sizes.get(i + 1) + 50;
            int y2 = (int) ((1 - x.get(i + 1)) * (getHeight() - 100)) + 50;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
}
