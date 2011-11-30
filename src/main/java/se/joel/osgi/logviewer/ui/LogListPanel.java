/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-11-30
 * Time: 20:30
 */
package se.joel.osgi.logviewer.ui;

import net.miginfocom.swing.MigLayout;
import org.osgi.service.log.LogEntry;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * List of log items.
 */
public class LogListPanel extends JPanel {
    private JTable logItemTable;

    {
        setLayout(new MigLayout("", "grow,fill", "grow,fill"));
        logItemTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(logItemTable);
        logItemTable.setFillsViewportHeight(true);
        add(scrollPane);
    }

    public void setModel(LogItemTableModel model) {
        logItemTable.setModel(model);
        logItemTable.getColumnModel().getColumn(0).setMinWidth(180);
        logItemTable.getColumnModel().getColumn(0).setMaxWidth(200);
        logItemTable.getColumnModel().getColumn(1).setMinWidth(50);
        logItemTable.getColumnModel().getColumn(1).setMaxWidth(80);
    }

    public void addSelectionListener(ListSelectionListener listener) {
        logItemTable.getSelectionModel().addListSelectionListener(listener);
    }

    public void removeSelectionListener(ListSelectionListener listener) {
        logItemTable.getSelectionModel().removeListSelectionListener(listener);
    }

    public static class LogItemTableModel extends AbstractTableModel {
        private static final Pattern DEFAULT_PATTERN = Pattern.compile(".*");
        private static String[] columnNames = new String[]{"Time", "Level", "Message"};
        private List<LogEntry> logEntries = new LinkedList<LogEntry>();
        private List<LogEntry> filteredLogEntries = new ArrayList<LogEntry>();
        private Pattern filterPattern = DEFAULT_PATTERN;

        public String getColumnName(int col) {
            return columnNames[col];

        }

        public void addLogEntry(LogEntry logEntry) {
            logEntries.add(logEntry);
            if (filterPattern.matcher(Util.makeString(logEntry)).matches()) {
                filteredLogEntries.add(logEntry);
                fireTableDataChanged();
            }
        }

        public LogEntry getLogEntryAt(int index) {
            if (0 <= index && index < filteredLogEntries.size()) {
                return filteredLogEntries.get(index);
            }
            return null;
        }

        public void setFilter(Pattern filterPattern) {
            if (filterPattern.toString().length() == 0) {
                this.filterPattern = DEFAULT_PATTERN;
            } else {
                this.filterPattern = filterPattern;
            }
            filteredLogEntries.clear();
            for (LogEntry logEntry : logEntries) {
                if (this.filterPattern.matcher(Util.makeString(logEntry)).matches()) {
                    filteredLogEntries.add(logEntry);
                }
            }
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return filteredLogEntries.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0:
                    return Util.timeToString(filteredLogEntries.get(row));
                case 1:
                    return Util.levelToString(filteredLogEntries.get(row));
                case 2:
                    return filteredLogEntries.get(row).getMessage();
            }
            return "";
        }
    }
}
