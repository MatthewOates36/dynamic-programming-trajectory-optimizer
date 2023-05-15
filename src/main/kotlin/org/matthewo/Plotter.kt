package org.matthewo

import com.sun.java.accessibility.util.AWTEventMonitor.addActionListener
import jetbrains.datalore.base.registration.Disposable
import jetbrains.datalore.plot.MonolithicCommon
import jetbrains.datalore.vis.swing.batik.DefaultPlotPanelBatik
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.intern.toSpec
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.*

class Plotter {

    fun plot(plots: Map<String, Plot>) {
        val selectedPlotKey = plots.keys.first()
        val controller = Controller(
            plots,
            selectedPlotKey,
        )

        val window = JFrame("Graph")
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        window.contentPane.layout = BoxLayout(window.contentPane, BoxLayout.Y_AXIS)

        // Add controls
        if (plots.keys.size > 1) {
            val controlsPanel = Box.createHorizontalBox().apply {
                // Plot selector
                val plotButtonGroup = ButtonGroup()
                for (key in plots.keys) {
                    plotButtonGroup.add(
                        JRadioButton(key, key == selectedPlotKey).apply {
                            addActionListener {
                                controller.plotKey = this.text
                            }
                        }
                    )
                }

                this.add(Box.createHorizontalBox().apply {
                    border = BorderFactory.createTitledBorder("Plot")
                    for (elem in plotButtonGroup.elements) {
                        add(elem)
                    }
                })
            }
            window.contentPane.add(controlsPanel)
        }

        // Add plot panel
        val plotContainerPanel = JPanel(GridLayout())
        window.contentPane.add(plotContainerPanel)

        controller.plotContainerPanel = plotContainerPanel
        controller.rebuildPlotComponent()

        SwingUtilities.invokeLater {
            window.pack()
            window.size = Dimension(850, 400)
            window.setLocationRelativeTo(null)
            window.isVisible = true
        }
    }

    private class Controller(
        private val plots: Map<String, Plot>,
        initialPlotKey: String,
    ) {
        var plotContainerPanel: JPanel? = null
        var plotKey: String = initialPlotKey
            set(value) {
                field = value
                rebuildPlotComponent()
            }

        fun rebuildPlotComponent() {
            plotContainerPanel?.let {
                val container = plotContainerPanel!!
                // cleanup
                for (component in container.components) {
                    if (component is Disposable) {
                        component.dispose()
                    }
                }
                container.removeAll()

                // build
                container.add(createPlotPanel())
                container.revalidate()
            }
        }

        fun createPlotPanel(): JPanel {
            val rawSpec = plots[plotKey]!!.toSpec()
            val processedSpec = MonolithicCommon.processRawSpecs(rawSpec, frontendOnly = false)

            return DefaultPlotPanelBatik(
                processedSpec = processedSpec,
                preserveAspectRatio = false,
                preferredSizeFromPlot = false,
                repaintDelay = 10,
            ) { messages ->
                for (message in messages) {
                    println("[Plotter] $message")
                }
            }
        }
    }
}