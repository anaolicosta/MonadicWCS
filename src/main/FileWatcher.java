package main;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.*;


/**
 * @author Ana Oliveira da Costa
 *
 */
public class FileWatcher {

	private Path pathToWatch;
	
	private WatchService watcher;
	private WatchKey key;
	
	/**
	 * Will register one or more objects with the watch service. 
	 * Any object that implements the Watchable interface can be registered. 
	 * The Path class implements the Watchable interface, 
	 * so each directory to be monitored is registered as a Path object.
	 * 
	 * @param path
	 */
	public FileWatcher(final Path pathToWatch){
		this.pathToWatch = pathToWatch;
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			key = pathToWatch.register(watcher,ENTRY_MODIFY);
					//ENTRY_CREATE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void watch(final Path expectedFile){
		
		boolean fileCreated = false;
		while(!fileCreated) {

		    // wait for key to be signaled
		    WatchKey key;
		    try {
		        key = watcher.take();
		    } catch (InterruptedException x) {
		        return;
		    }

		    for (WatchEvent<?> event: key.pollEvents()) {
		        WatchEvent.Kind<?> kind = event.kind();

		        // This key is registered only
		        // for ENTRY_CREATE events,
		        // but an OVERFLOW event can
		        // occur regardless if events
		        // are lost or discarded.
		        if (kind == OVERFLOW) {
		            continue;
		        }

		        // The filename is the
		        // context of the event.
		        WatchEvent<Path> ev = (WatchEvent<Path>)event;
		        Path filename = ev.context();

	            // Resolve the filename against the directory.
	            Path child = pathToWatch.resolve(filename);
		              
		        if(child.equals(expectedFile))
		        	fileCreated = true;
		    }

		    // Reset the key -- this step is critical if you want to
		    // receive further watch events.  If the key is no longer valid,
		    // the directory is inaccessible so exit the loop.
		    boolean valid = key.reset();
		    if (!valid) {
		        break;
		    }
		}
	}
	
	public void close() {
		try {
			watcher.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	


}
