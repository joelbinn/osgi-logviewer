/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-11-29
 * Time: 22:34
 */
package se.joel.osgi.logviewer.ui;

import net.miginfocom.swing.MigLayout;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The log viewer.
 */
public class LogViewer {

    private LogViewer.LogEntryListener logEntryListener;
    private LogReaderService logReader;
    private JFrame mainFrame;
    private LogListPanel.LogItemTableModel listModel;

    public LogViewer(LogReaderService logReader) {
        this.logReader = logReader;
        mainFrame = createUI();
        mainFrame.setVisible(true);
    }

    private JFrame createUI() {
        JFrame mainFrame = new JFrame("OSGi Log Viewer");
        // addMenuBar(mainFrame);
        mainFrame.setLayout(new MigLayout("", "fill,grow", "[grow 1][fill, grow 100][grow 1]"));
        final DetailsPanel detailsPanel = new DetailsPanel();
        final LogListPanel logListPanel = new LogListPanel();
        listModel = new LogListPanel.LogItemTableModel();
        logListPanel.setModel(listModel);
        FilterEditor filterEditor = new FilterEditor();
        filterEditor.addListener(new FilterEditor.FilterChangedListener() {
            @Override
            public void onFilterChanged(FilterEditor source) {
                listModel.setFilter(source.getPattern());
            }
        });
        logListPanel.addSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                detailsPanel.setLogEntry(
                    listModel.getLogEntryAt(
                        ((ListSelectionModel)listSelectionEvent.getSource()).getLeadSelectionIndex()));
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logListPanel.clear();
            }
        });

        mainFrame.getContentPane().add(filterEditor, "span,growx,growprio 0,wrap");
        mainFrame.getContentPane().add(logListPanel, "span,growx,growprio 100,wrap");
        addSeparator(mainFrame, "Log entry details");
        mainFrame.getContentPane().add(detailsPanel, "span,growx,growprio 0, wrap");
        mainFrame.getContentPane().add(clearButton, "grow 0, right");
        mainFrame.setSize(1000, 500);
        mainFrame.setLocation(500, 300);
        mainFrame.setVisible(true);
        return mainFrame;
    }

    public void start() {
        logEntryListener = new LogEntryListener();
        logReader.addLogListener(logEntryListener);
    }

    public void stop() {
        logReader.removeLogListener(logEntryListener);
        mainFrame.setVisible(false);
        mainFrame.dispose();
    }

    private class LogEntryListener implements LogListener {
        @Override
        public void logged(LogEntry entry) {
            listModel.addLogEntry(entry);
        }
    }

    private void addSeparator(JFrame panel, String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.BLUE);
        panel.getContentPane().add(l, "gapbottom 1, span, split 2, aligny center");
        panel.getContentPane().add(new JSeparator(), "gapleft rel, growx");
    }
}
