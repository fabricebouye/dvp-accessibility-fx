/* 
 * Copyright (C) 2015-2016 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test.chart;

import java.util.stream.IntStream;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import test.Main;

/** 
 * Implementation rapide et sale d'un <a href=\"https://en.wikipedia.org/wiki/Bullet_graph\">bullet chart</a>. 
 * <br>Ce code de ma création provient d'une question postée sur OTN (The Oracle Technical Network) : <a href="https://community.oracle.com/message/13355772">Bullet chart in javafx</a>.
 * Tout comme la rose des vents, ce contrôle demande a être rendu accessible pour qu'il soit pleinement utilisable par un utilisateur non-voyant.
 * @author Fabrice Bouyé 
 */
public final class BulletGraph extends Region {

    private static final PseudoClass VERTICAL_PSEUDO_CLASS = PseudoClass.getPseudoClass("vertical"); // NOI18N.  
    private static final String USER_AGENT_STYLE_SHEET = "BulletChart.css"; // NOI18N.  

    private final NumberAxis axis = new NumberAxis(0d, 100d, 25d);
    private final Region performanceMeasureMarker = new Region();
    private final Region comparativeMeasureMarker = new Region();
    private final Group quantitativeScaleGroup = new Group();
    protected final Region plotArea = new Region() {
        {
            getChildren().add(quantitativeScaleGroup);
            getChildren().add(performanceMeasureMarker);
            getChildren().add(comparativeMeasureMarker);
        }

        @Override
        protected void layoutChildren() {
            layoutPlotChildren();
        }
    };
    private final Text titleLabel = new Text();
    private final Text descriptionLabel = new Text();
    private final TextFlow titleFlow = new TextFlow(titleLabel, new Text("\n"), descriptionLabel); // NOI18N.  
    private final Tooltip performanceMeasureTip = new Tooltip();
    private final Tooltip comparativeMeasureTip = new Tooltip();

    /** 
     * Creates a new instance. 
     */
    public BulletGraph() {
        super();
        setId("bulletChart"); // NOI18N.  
        getStyleClass().add("bullet-chart"); // NOI18N.  
        //  
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        //  
        axis.setSide(Side.BOTTOM);
        getChildren().add(axis);
        //
        accessibleTextProperty().bind(new StringBinding() {
            {
                bind(titleProperty(), descriptionProperty());
            }

            @Override
            public void dispose() {
                bind(titleProperty(), descriptionProperty());
            }

            @Override
            protected String computeValue() {
                final String title = titleProperty().get();
                final String description = descriptionProperty().get();
                final StringBuilder result = new StringBuilder();
                result.append(title);
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(description);
                return result.toString();
            }
        });
        //
        performanceMeasureMarker.getStyleClass().add("performance-measure-marker"); // NOI18N.  
        performanceMeasureTip.textProperty().bind(performanceMeasureProperty().asString());
        Tooltip.install(performanceMeasureMarker, performanceMeasureTip);
        performanceMeasureMarker.accessibleTextProperty().bind(new StringBinding() {
            {
                bind(performanceMeasure);
            }

            @Override
            public void dispose() {
                unbind(performanceMeasure);
            }

            @Override
            protected String computeValue() {
                final double value = performanceMeasure.get();
                final String key = "bullet-graph.performance-mesure.accessible.text"; // NOI18N.  
                return String.format(Main.I18N.getString(key), value);
            }
        });
        //  
        comparativeMeasureMarker.getStyleClass().add("comparative-measure-marker"); // NOI18N.  
        comparativeMeasureTip.textProperty().bind(comparativeMeasureProperty().asString());
        Tooltip.install(comparativeMeasureMarker, comparativeMeasureTip);
        comparativeMeasureMarker.accessibleTextProperty().bind(new StringBinding() {
            {
                bind(comparativeMeasure);
            }

            @Override
            public void dispose() {
                unbind(comparativeMeasure);
            }

            @Override
            protected String computeValue() {
                final double value = comparativeMeasure.get();
                final String key = "bullet-graph.comparative-mesure.accessible.text"; // NOI18N.  
                return String.format(Main.I18N.getString(key), value);
            }
        });
        //  
        plotArea.getStyleClass().add("plot-area"); // NOI18N.  
        getChildren().add(plotArea);
        //  
        getChildren().add(titleFlow);
        titleFlow.getStyleClass().add("title-flow"); // NOI18N.  
        titleLabel.textProperty().bind(titleProperty());
        titleLabel.getStyleClass().add("title"); // NOI18N.  
        descriptionLabel.textProperty().bind(descriptionProperty());
        descriptionLabel.getStyleClass().add("description"); // NOI18N.  
        //  
        axis.lowerBoundProperty().addListener(layoutRequestListener);
        axis.upperBoundProperty().addListener(layoutRequestListener);
        comparativeMeasureProperty().addListener(layoutRequestListener);
        performanceMeasureProperty().addListener(layoutRequestListener);
        orientationProperty().addListener(layoutRequestListener);
        titleProperty().addListener(layoutRequestListener);
        descriptionProperty().addListener(layoutRequestListener);
        titleAxisGapProperty().addListener(layoutRequestListener);
        getQuantitativeScale().addListener((Change<? extends Double> change) -> {
            resetQuantitativeShapes();
            prepareForLayout();
        });
        //  
        resetQuantitativeShapes();
        prepareForLayout();
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource(USER_AGENT_STYLE_SHEET).toExternalForm();
    }

    /** 
     * Called when values are invalidated. 
     * <br>Call for a relayout. 
     */
    private final InvalidationListener layoutRequestListener = observable -> prepareForLayout();

    private void resetQuantitativeShapes() {
        // Clear old shapes.  
        quantitativeScaleGroup.getChildren().clear();
        // Create new ones.  
        IntStream.range(0, quantitativeScale.size())
                .forEach(index -> {
                    final Region region = new Region();
                    final String style = String.format("quantitative-scale%d", index + 1); // NOI18N.  
                    region.getStyleClass().add(style);
                    final double value = quantitativeScale.get(index);
                    final String key = "bullet-graph.quantitative-mesure.accessible.text"; // NOI18N.  
                    region.setAccessibleText(String.format(Main.I18N.getString(key), index + 1, value));
                    quantitativeScaleGroup.getChildren().add(region);
                });
    }

    private void prepareForLayout() {
        final Orientation orientation = getOrientation();
        final boolean isVertical = orientation == Orientation.VERTICAL;
        pseudoClassStateChanged(VERTICAL_PSEUDO_CLASS, isVertical);
        axis.setSide(isVertical ? Side.LEFT : Side.BOTTOM);
        if (!maxWidthProperty().isBound()) {
            setMaxWidth(isVertical ? USE_PREF_SIZE : Double.MAX_VALUE);
        }
        if (!maxHeightProperty().isBound()) {
            setMaxHeight(isVertical ? Double.MAX_VALUE : USE_PREF_SIZE);
        }
        requestLayout();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double width = getWidth();
        final double height = getHeight();
        final Insets insets = getInsets();
        final double areaX = insets.getLeft();
        final double areaY = insets.getTop();
        final double areaW = Math.max(0, width - (insets.getLeft() + insets.getRight()));
        final double areaH = Math.max(0, height - (insets.getTop() + insets.getBottom()));
        layoutChartChildren(areaX, areaY, areaW, areaH);
    }

    /** 
     * Layout chart in given area. 
     * @param areaX Area's X coordinate. 
     * @param areaY Area's Y coordinate. 
     * @param areaW Area's width. 
     * @param areaH Area's height. 
     */
    protected void layoutChartChildren(double areaX, double areaY, double areaW, double areaH) {
        final Orientation orientation = getOrientation();
        final double titleAxisGap = Math.max(0, getTitleAxisGap());
        switch (orientation) {
            case VERTICAL: {
                final double titleW = Math.min(areaW, titleFlow.getWidth());
                final double titleH = titleFlow.prefHeight(titleW);
                final double titleY = areaY;
                final double axisX = areaX;
                final double axisY = titleY + titleH + titleAxisGap;
                final double axisW = axis.getWidth();
                final double axisH = areaH - axisY;
                layoutInArea(axis, axisX, axisY, axisW, axisH, 0, HPos.LEFT, VPos.TOP);
                final double plotChildrenX = axisX + axisW;
                final double plotChildrenY = axisY;
                final double plotChildrenW = areaW - axisW;
                final double plotChildrenH = axisH;
                layoutInArea(plotArea, plotChildrenX, plotChildrenY, plotChildrenW, plotChildrenH, 0, HPos.LEFT, VPos.TOP);
                final double titleX = plotChildrenX + (plotChildrenW - titleW) / 2;
                layoutInArea(titleFlow, titleX, titleY, titleW, titleH, 0, HPos.LEFT, VPos.TOP);
            }
            break;
            case HORIZONTAL:
            default: {
                final double titleW = Math.min(areaW / 2, titleFlow.getWidth());
                final double titleH = titleFlow.prefHeight(titleW);
                final double titleX = areaX;
                final double titleY = areaY + (areaH - titleH) / 2;
                layoutInArea(titleFlow, titleX, titleY, titleW, titleH, 0, HPos.LEFT, VPos.TOP);
                final double axisX = titleX + titleW + titleAxisGap;
                final double axisW = areaW - axisX;
                final double axisH = axis.getHeight();
                final double axisY = areaY + areaH - axisH;
                layoutInArea(axis, axisX, axisY, axisW, axisH, 0, HPos.LEFT, VPos.TOP);
                final double plotChildrenX = axisX;
                final double plotChildrenY = areaY;
                final double plotChildrenW = axisW;
                final double plotChildrenH = areaH - axisH;
                layoutInArea(plotArea, plotChildrenX, plotChildrenY, plotChildrenW, plotChildrenH, 0, HPos.LEFT, VPos.TOP);
            }
        }
        layoutPlotChildren();
    }

    /** 
     * Layout plot children in plot area. 
     */
    protected void layoutPlotChildren() {
        final Orientation orientation = getOrientation();
        final double width = plotArea.getWidth();
        final double height = plotArea.getHeight();
        final double lowerBound = axis.getLowerBound();
        final double upperBound = axis.getUpperBound();
        final double performanceMeasure = getPerformanceMeasure();
        final double comparativeMeasure = getComparativeMeasure();
        switch (orientation) {
            case VERTICAL: {
                IntStream.range(0, quantitativeScale.size())
                        .forEach(index -> {
                            final Region region = (Region) quantitativeScaleGroup.getChildren().get(index);
                            double stop = quantitativeScale.get(index);
                            double previousStop = (index == 0) ? 0 : quantitativeScale.get(index - 1);
                            double w = width;
                            double h = height * (stop - previousStop);
                            double x = 0;
                            double y = height - height * stop;
                            region.relocate(x, y);
                            region.setMinSize(w, h);
//                            layoutInArea(region, x, y, w, h, 0, HPos.LEFT, VPos.TOP);  
                        });
                double performanceW = performanceMeasureMarker.getWidth();
                double performanceH = height * (performanceMeasure - lowerBound) / (upperBound - lowerBound);
                double performanceX = (width - performanceW) / 2;
                double performanceY = height - performanceH;
                layoutInArea(performanceMeasureMarker, performanceX, performanceY, performanceW, performanceH, 0, HPos.LEFT, VPos.TOP);
                double comparativeW = comparativeMeasureMarker.getWidth();
                double comparativeH = comparativeMeasureMarker.getHeight();
                double comparativeX = (width - comparativeW) / 2;
                double comparativeY = height - height * (comparativeMeasure - lowerBound) / (upperBound - lowerBound) - comparativeH / 2;
                layoutInArea(comparativeMeasureMarker, comparativeX, comparativeY, comparativeW, comparativeH, 0, HPos.LEFT, VPos.TOP);
            }
            break;
            case HORIZONTAL:
            default: {
                IntStream.range(0, quantitativeScale.size())
                        .forEach(index -> {
                            final Region region = (Region) quantitativeScaleGroup.getChildren().get(index);
                            double stop = quantitativeScale.get(index);
                            double previousStop = (index == 0) ? 0 : quantitativeScale.get(index - 1);
                            double w = width * (stop - previousStop);
                            double h = height;
                            double x = width * previousStop;
                            double y = 0;
                            region.relocate(x, y);
                            region.setMinSize(w, h);
//                            layoutInArea(region, x, y, w, h, 0, HPos.LEFT, VPos.TOP);  
                        });
                double performanceW = width * (performanceMeasure - lowerBound) / (upperBound - lowerBound);
                double performanceH = performanceMeasureMarker.getHeight();
                double performanceX = 0;
                double performanceY = (height - performanceH) / 2;
                layoutInArea(performanceMeasureMarker, performanceX, performanceY, performanceW, performanceH, 0, HPos.LEFT, VPos.TOP);
                double comparativeW = comparativeMeasureMarker.getWidth();
                double comparativeH = comparativeMeasureMarker.getHeight();
                double comparativeX = width * (comparativeMeasure - lowerBound) / (upperBound - lowerBound) - comparativeW / 2;
                double comparativeY = (height - comparativeH) / 2;
                layoutInArea(comparativeMeasureMarker, comparativeX, comparativeY, comparativeW, comparativeH, 0, HPos.LEFT, VPos.TOP);
            }
        }
    }

    public ValueAxis getAxis() {
        return axis;
    }

    private final DoubleProperty comparativeMeasure = new SimpleDoubleProperty(this, "comparativeMeasure", 0); // NOI18N.  

    public final double getComparativeMeasure() {
        return comparativeMeasure.get();
    }

    public final void setComparativeMeasure(double value) {
        comparativeMeasure.set(value);
    }

    public final DoubleProperty comparativeMeasureProperty() {
        return comparativeMeasure;
    }
    private final DoubleProperty performanceMeasure = new SimpleDoubleProperty(this, "performanceMeasure", 0); // NOI18N.  

    public final double getPerformanceMeasure() {
        return performanceMeasure.get();
    }

    public final void setPerformanceMeasure(double value) {
        performanceMeasure.set(value);
    }

    public final DoubleProperty performanceMeasureProperty() {
        return performanceMeasure;
    }

    private final ReadOnlyObjectWrapper<Orientation> orientation = new ReadOnlyObjectWrapper<>(this, "orientation", Orientation.HORIZONTAL); // NOI18N.  

    public final Orientation getOrientation() {
        return orientation.get();
    }

    public final void setOrientation(Orientation value) {
        Orientation v = (value == null) ? Orientation.HORIZONTAL : value;
        orientation.set(v);
    }

    public final ReadOnlyObjectProperty<Orientation> orientationProperty() {
        return orientation.getReadOnlyProperty();
    }

    private final StringProperty title = new SimpleStringProperty(this, "title", null); // NOI18N.  

    public final String getTitle() {
        return title.get();
    }

    public final void setTitle(String value) {
        title.set(value);
    }

    public final StringProperty titleProperty() {
        return title;
    }

    private final StringProperty description = new SimpleStringProperty(this, "description", null); // NOI18N.  

    public final String getDescription() {
        return description.get();
    }

    public final void setDescription(String value) {
        description.set(value);
    }

    public final StringProperty descriptionProperty() {
        return description;
    }

    private final DoubleProperty titleAxisGap = new SimpleDoubleProperty(this, "titleAxisGap", 6); // NOI18N.  

    public final double getTitleAxisGap() {
        return titleAxisGap.get();
    }

    public final void setTitleAxisGap(double value) {
        titleAxisGap.set(value);
    }

    public final DoubleProperty titleAxisGapProperty() {
        return titleAxisGap;
    }
    private final ReadOnlyListWrapper<Double> quantitativeScale = new ReadOnlyListWrapper<>(this, "quantitativeScale", FXCollections.observableArrayList(0.75, 0.90, 1.0)); // NOI18N.  

    public final ObservableList<Double> getQuantitativeScale() {
        return quantitativeScale.get();
    }

    public final ReadOnlyListProperty<Double> quantitativeScaleProperty() {
        return quantitativeScale.getReadOnlyProperty();
    }

}
