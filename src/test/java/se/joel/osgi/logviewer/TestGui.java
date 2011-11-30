/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-11-30
 * Time: 00:16
 */
package se.joel.osgi.logviewer;

import net.miginfocom.swing.MigLayout;
import org.osgi.framework.*;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogService;
import se.joel.osgi.logviewer.ui.DetailsPanel;
import se.joel.osgi.logviewer.ui.FilterEditor;
import se.joel.osgi.logviewer.ui.LogListPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * TODO describe!!!
 */
public class TestGui {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setLayout(new MigLayout("", "fill,grow", "[grow 1][fill, grow 100][grow 1]"));
        final DetailsPanel detailsPanel = new DetailsPanel();
        LogListPanel logListPanel = new LogListPanel();
        final LogListPanel.LogItemTableModel listModel = new LogListPanel.LogItemTableModel();
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
                detailsPanel.setLogEntry(listModel.getLogEntryAt(listSelectionEvent.getFirstIndex()));
            }
        });

        frame.getContentPane().add(filterEditor, "span,growx,growprio 0,wrap");
        frame.getContentPane().add(logListPanel, "span,growx,growprio 100,wrap");
        frame.getContentPane().add(detailsPanel, "span,growx,growprio 0");
        frame.setSize(1000, 500);
        frame.setLocation(500, 300);
        frame.setVisible(true);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    listModel.addLogEntry(new TestLogEntry());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        }.start();
    }

    static class TestLogEntry implements LogEntry {
        Bundle bundle = new Bundle() {
            @Override
            public int getState() {
                return Bundle.ACTIVE;
            }

            @Override
            public void start(int options) throws BundleException {

            }

            @Override
            public void start() throws BundleException {

            }

            @Override
            public void stop(int options) throws BundleException {

            }

            @Override
            public void stop() throws BundleException {

            }

            @Override
            public void update(InputStream input) throws BundleException {

            }

            @Override
            public void update() throws BundleException {

            }

            @Override
            public void uninstall() throws BundleException {

            }

            @Override
            public Dictionary<String, String> getHeaders() {
                return null;
            }

            @Override
            public long getBundleId() {
                return 23;
            }

            @Override
            public String getLocation() {
                return "file://myBundle";
            }

            @Override
            public ServiceReference<?>[] getRegisteredServices() {
                return new ServiceReference<?>[0];
            }

            @Override
            public ServiceReference<?>[] getServicesInUse() {
                return new ServiceReference<?>[0];
            }

            @Override
            public boolean hasPermission(Object permission) {
                return false;
            }

            @Override
            public URL getResource(String name) {
                return null;
            }

            @Override
            public Dictionary<String, String> getHeaders(String locale) {
                return null;
            }

            @Override
            public String getSymbolicName() {
                return "MyBundle";
            }

            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return null;
            }

            @Override
            public Enumeration<URL> getResources(String name) throws IOException {
                return null;
            }

            @Override
            public Enumeration<String> getEntryPaths(String path) {
                return null;
            }

            @Override
            public URL getEntry(String path) {
                return null;
            }

            @Override
            public long getLastModified() {
                return 0;
            }

            @Override
            public Enumeration<URL> findEntries(String path, String filePattern, boolean recurse) {
                return null;
            }

            @Override
            public BundleContext getBundleContext() {
                return null;
            }

            @Override
            public Map<X509Certificate, List<X509Certificate>> getSignerCertificates(int signersType) {
                return null;
            }

            @Override
            public Version getVersion() {
                return null;
            }

            @Override
            public <A> A adapt(Class<A> type) {
                return null;
            }

            @Override
            public File getDataFile(String filename) {
                return null;
            }

            @Override
            public int compareTo(Bundle bundle) {
                return 0;
            }
        };
        private static int ctr;
        private String msg = "message#" + (++ctr);
        private long time = System.currentTimeMillis();

        @Override
        public Bundle getBundle() {
            return bundle;
        }

        @Override
        public ServiceReference getServiceReference() {
            return null;
        }

        @Override
        public int getLevel() {
            return LogService.LOG_DEBUG;
        }

        @Override
        public String getMessage() {
            return msg;
        }

        @Override
        public Throwable getException() {
            return null;
        }

        @Override
        public long getTime() {
            return time;
        }
    }

}
