/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-11-29
 * Time: 22:24
 */
package se.joel.osgi.logviewer;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogReaderService;
import se.joel.osgi.logviewer.ui.LogViewer;

/**
 * This is the starter for the logviewer.
 */
public class Main {
    private LogReaderService logReader;
    private ComponentContext componentContext;
    private LogViewer logViewer;

    protected void setLogReader(LogReaderService logReader) {
        this.logReader = logReader;
    }

    protected void unsetLogReader(LogReaderService logReader) {
        this.logReader = null;
    }

    protected void activate(ComponentContext componentContext) {
        this.componentContext = componentContext;
        logViewer = new LogViewer(logReader);
        logViewer.start();
    }

    protected void deactivate(ComponentContext componentContext) {
        this.componentContext = null;
        logViewer.stop();
        logViewer = null;
    }
}
