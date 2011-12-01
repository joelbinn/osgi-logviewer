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
import javax.swing.table.TableCellRenderer;
import java.awt.*;
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
        logItemTable.setAutoscrolls(true);
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
        logItemTable.setDefaultRenderer(Object.class, new LogEntryRenderer());
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
            return filteredLogEntries.get(row);
        }
    }

    static class LogEntryRenderer implements TableCellRenderer {
        private static final Color SAND = new Color(255, 237, 165);
        private static final Color GREY_SAND = new Color(216, 204, 144);
        private static final Color LIGHT_BLUE = new Color(186, 229, 255);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int col) {
            String str;
            switch (col) {
                case 0:
                    str = Util.timeToString((LogEntry) object);
                    break;
                case 1:
                    str = Util.levelToString((LogEntry) object);
                    break;
                case 2:
                    str = ((LogEntry) object).getMessage();
                    break;
                default:
                    str = "N/A";
            }

            JLabel cell = new JLabel("" + str);

            if (col == 2) {
                Font font = new Font("Sans-serif", Font.BOLD, 12);
                cell.setFont(font);
            }
            cell.setOpaque(true);

            if (hasFocus) {
                cell.setBackground(GREY_SAND);
            } else if (isSelected) {
                cell.setBackground(Color.LIGHT_GRAY);
            } else if ((row % 2) == 0) {
                cell.setBackground(LIGHT_BLUE);
            } else {
                cell.setBackground(Color.WHITE);
            }

            return cell;
        }
    }
}
