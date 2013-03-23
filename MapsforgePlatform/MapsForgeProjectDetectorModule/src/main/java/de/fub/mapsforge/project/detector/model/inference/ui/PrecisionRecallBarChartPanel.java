/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.inference.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openide.util.NbBundle;

/**
 *
 * @author Serdar
 */
public class PrecisionRecallBarChartPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private final ChartPanel chartPanel;
    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private final JFreeChart barChart;
    private final CategoryPlot plot;
    private Color precColor = new Color(0x00, 0x7a, 0xe0);
    private Color recColor = new Color(0xe0, 0x00, 0x00);

    /**
     * Creates new form PrecisionRecallBarChartPanel
     */
    public PrecisionRecallBarChartPanel() {
        super();
        initComponents();
        barChart = ChartFactory.createBarChart(
                NbBundle.getMessage(PrecisionRecallBarChartPanel.class, "CLT_Chart_Precision_Recall_Name"),
                NbBundle.getMessage(PrecisionRecallBarChartPanel.class, "CLT_Doman_Axis_Name"),
                NbBundle.getMessage(PrecisionRecallBarChartPanel.class, "CLT_Value_Axis_Name"),
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);

        Font font = new JLabel().getFont().deriveFont(Font.BOLD, 14);

        barChart.getTitle().setFont(font);
        barChart.getTitle().setPaint(new Color(153, 153, 153));

        plot = barChart.getCategoryPlot();
        NumberAxis preciAxis = new NumberAxis(NbBundle.getMessage(PrecisionRecallBarChartPanel.class, "CLT_Value_Axis_Name"));
//        preciAxis.setAutoRange(false);
        preciAxis.setRange(0, 100);
        plot.setRangeAxis(0, preciAxis);
        plot.setRangeAxisLocation(0, AxisLocation.TOP_OR_LEFT);
        plot.setBackgroundPaint(Color.white);

        BarRenderer barRenderer = new BarRenderer();
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        barRenderer.setSeriesPaint(0, precColor);
        barRenderer.setSeriesPaint(1, recColor);

        plot.setRenderer(barRenderer);

        chartPanel = new ChartPanel(barChart, false);
        chartPanel.setVerticalAxisTrace(false);
        add(chartPanel, BorderLayout.CENTER);
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public DefaultCategoryDataset getDataset() {
        return dataset;
    }

    public JFreeChart getBarChart() {
        return barChart;
    }

    public CategoryPlot getPlot() {
        return plot;
    }

    public void setTitle(TextTitle title) {
        barChart.setTitle(title);
    }

    public CategoryAxis getDomainAxis() {
        return plot.getDomainAxis();
    }

    public ValueAxis getRangeAxis() {
        return plot.getRangeAxis();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
