/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.inference.ui.charts;

import de.fub.utilsmodule.text.CustomNumberFormat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
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
public class AttributeSelectionBarChart extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private final JFreeChart barChart;
    private final ChartPanel chartPanel;
    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private final CategoryPlot plot;

    /**
     * Creates new form AttributeSelectionBarChart
     */
    public AttributeSelectionBarChart() {
        initComponents();
        barChart = ChartFactory.createBarChart(
                NbBundle.getMessage(AttributeSelectionBarChart.class, "AttributeSelectionBarChart.CLT_Chart_Title"),
                NbBundle.getMessage(AttributeSelectionBarChart.class, "AttributeSelectionBarChart.CLT_Domain_Axis_Name"),
                NbBundle.getMessage(AttributeSelectionBarChart.class, "AttributeSelectionBarChart.CLT_Value_Axis_Name"),
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);
        plot = barChart.getCategoryPlot();
        Font font = new JLabel().getFont().deriveFont(Font.BOLD, 14);

        barChart.getTitle().setFont(font);
        barChart.getTitle().setPaint(new Color(153, 153, 153));

        plot.setRangeAxisLocation(0, AxisLocation.TOP_OR_LEFT);

        plot.setBackgroundPaint(Color.white);
        plot.getRangeAxis().setAutoRange(true);
        plot.getRangeAxis().setUpperMargin(.1);

        BarRenderer barRenderer = new BarRenderer();
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        barRenderer.setBaseFillPaint(Color.BLUE);
        barRenderer.setBasePaint(Color.BLUE);
        barRenderer.setAutoPopulateSeriesFillPaint(false);
        barRenderer.setAutoPopulateSeriesPaint(false);
        barRenderer.setBaseItemLabelGenerator(
                new StandardCategoryItemLabelGenerator(
                StandardCategoryItemLabelGenerator.DEFAULT_LABEL_FORMAT_STRING,
                new CustomNumberFormat()));
        barRenderer.setBaseItemLabelsVisible(true);

        plot.setRenderer(barRenderer);
        chartPanel = new ChartPanel(barChart, false);
        chartPanel.setVerticalAxisTrace(false);
        add(chartPanel, BorderLayout.CENTER);
    }

    public JFreeChart getBarChart() {
        return barChart;
    }

    public DefaultCategoryDataset getDataset() {
        return dataset;
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
