/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Serdar
 */
public class BarChartPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private final JFreeChart barChart;
    private final CategoryPlot plot;
    private final ChartPanel chartPanel;

    /**
     * Creates new form StatisticSegmentLengthBarChart
     */
    public BarChartPanel() {
        initComponents();
        barChart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, true, true, true);
        plot = barChart.getCategoryPlot();

        BarRenderer barRenderer = new BarRenderer();
        barRenderer.setMaximumBarWidth(.05);
        barRenderer.setBasePaint(Color.BLUE);
        barRenderer.setAutoPopulateSeriesPaint(false);
        barRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barRenderer.setBaseItemLabelsVisible(true);
        plot.setRenderer(barRenderer);

        plot.setBackgroundPaint(Color.white);
        barChart.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(true);

        chartPanel = new ChartPanel(barChart, false);
        chartPanel.setVerticalAxisTrace(false);
        chartPanel.setDisplayToolTips(true);
        chartPanel.setBackground(Color.white);
        add(chartPanel, BorderLayout.CENTER);
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

    public ChartPanel getChartPanel() {
        return chartPanel;
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
