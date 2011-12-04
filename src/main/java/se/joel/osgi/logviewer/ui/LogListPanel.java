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
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * List of log items.
 */
public class LogListPanel extends JPanel {
    private JTable logItemTable;
    private LogItemTableModel model;

    {
        setLayout(new MigLayout("", "grow,fill", "grow,fill"));
        logItemTable = new JTable();
        logItemTable.setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(logItemTable);
        logItemTable.setFillsViewportHeight(true);
        add(scrollPane);
    }

    public void setModel(LogItemTableModel model) {
        this.model = model;
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

    public void clear() {
        model.clearEntryMap();
    }

    public static class LogItemTableModel extends AbstractTableModel {
        private static final Pattern DEFAULT_PATTERN = Pattern.compile(".*");
        private static String[] columnNames = new String[]{"Time", "Level", "Message"};
        private final List<LogEntry> logEntries = new LinkedList<LogEntry>();
        private final List<LogEntry> filteredLogEntries = new ArrayList<LogEntry>();
        private Pattern filterPattern = DEFAULT_PATTERN;

        public String getColumnName(int col) {
            return columnNames[col];

        }

        public void addLogEntry(final LogEntry logEntry) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    logEntries.add(logEntry);
                    if (filterPattern.matcher(Util.makeString(logEntry)).matches()) {
                        synchronized (filteredLogEntries) {
                            filteredLogEntries.add(logEntry);
                        }
                        fireTableRowsInserted(filteredLogEntries.size() - 2, filteredLogEntries.size() - 1);
                    }
                }
            });
        }

        public LogEntry getLogEntryAt(int index) {
            synchronized (filteredLogEntries) {
                if (0 <= index && index < filteredLogEntries.size()) {
                    return filteredLogEntries.get(index);
                }
            }
            return null;
        }

        public void setFilter(final Pattern filterPattern) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (filterPattern.toString().length() == 0) {
                        LogItemTableModel.this.filterPattern = DEFAULT_PATTERN;
                    } else {
                        LogItemTableModel.this.filterPattern = filterPattern;
                    }
                    Set<LogEntry> newFilteredLogEntries = new HashSet<LogEntry>();
                    for (LogEntry logEntry : logEntries) {
                        if (LogItemTableModel.this.filterPattern.matcher(Util.makeString(logEntry)).matches()) {
                            newFilteredLogEntries.add(logEntry);
                        }
                    }

                    synchronized (filteredLogEntries) {
                        filteredLogEntries.clear();
                        filteredLogEntries.addAll(newFilteredLogEntries);
                    }
                    fireTableDataChanged();
                }
            });
        }

        @Override
        public int getRowCount() {
            synchronized (filteredLogEntries) {
                return filteredLogEntries.size();
            }
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int row, int col) {
            synchronized (filteredLogEntries) {
                return filteredLogEntries.get(row);
            }
        }

        public void clearEntryMap() {
            synchronized (filteredLogEntries) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logEntries.clear();
                        filteredLogEntries.clear();
                        fireTableDataChanged();
                    }
                });
            }

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
