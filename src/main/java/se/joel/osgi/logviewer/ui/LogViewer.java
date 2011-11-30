/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-11-29
 * Time: 22:34
 */
package se.joel.osgi.logviewer.ui;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;

import javax.swing.*;
import java.awt.*;

/**
 * The log viewer.
 */
public class LogViewer {

    private LogViewer.LogEntryListener logEntryListener;
    private LogReaderService logReader;
    private JFrame mainFrame;

    public LogViewer(LogReaderService logReader) {
        this.logReader = logReader;
        mainFrame = createUI();
        mainFrame.setVisible(true);
    }

    private JFrame createUI() {
        JFrame mainFrame = new JFrame("OSGi Log Viewer");
        mainFrame.setLayout(new BorderLayout());
        // addMenuBar(mainFrame);
        // addFilterBar(mainFrame);
        // addLogView(mainFrame);
        // addDetailsPanel(mainFrame);
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
            System.out.println(entry.getMessage());
        }
    }
}
