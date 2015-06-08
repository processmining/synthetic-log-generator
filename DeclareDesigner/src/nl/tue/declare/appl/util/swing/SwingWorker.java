package nl.tue.declare.appl.util.swing;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */
import java.beans.*;
import java.util.*;

import javax.swing.*;

/**
 * This is the 3rd version of SwingWorker (also known as
 * SwingWorker 3), an abstract class that you subclass to
 * perform GUI-related work in a dedicated thread.  For
 * instructions on using this class, see:
 *
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * Note that the API changed slightly in the 3rd version:
 * You must now invoke start() on the SwingWorker after
 * creating it.
 */

public abstract class SwingWorker {
  private Object value; // see getValue(), setValue()
  private List<PropertyChangeListener> listeners;
  private Integer progress;

  /**
   * Class to maintain reference to current worker thread
   * under separate synchronization control.
   */
  private static class ThreadVar {
    private Thread thread;
    ThreadVar(Thread t) {
      thread = t;
    }

    synchronized Thread get() {
      return thread;
    }

    synchronized void clear() {
      thread = null;
    }
  }

  private ThreadVar threadVar;

  /**
   * Get the value produced by the worker thread, or null if it
   * hasn't been constructed yet.
   */
  protected synchronized Object getValue() {
    return value;
  }

  /**
   * Set the value produced by worker thread
   */
  private synchronized void setValue(Object x) {
    value = x;
  }

  /**
   * Compute the value to be returned by the <code>get</code> method.
   */
  public abstract Object construct();

  /**
   * Called on the event dispatching thread (not on the worker thread)
   * after the <code>construct</code> method has returned.
   */
  public void finished() {
  }

  /**
   * A new method that interrupts the worker thread.  Call this method
   * to force the worker to stop what it's doing.
   */
  public void interrupt() {
    Thread t = threadVar.get();
    if (t != null) {
      t.interrupt();
    }
    threadVar.clear();
  }

  /**
   * Return the value created by the <code>construct</code> method.
   * Returns null if either the constructing thread or the current
   * thread was interrupted before a value was produced.
   *
   * @return the value created by the <code>construct</code> method
   */
  public Object get() {
    while (true) {
      Thread t = threadVar.get();
      if (t == null) {
        return getValue();
      }
      try {
        t.join();
      }
      catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // propagate
        return null;
      }
    }
  }

  /**
   * Start a thread that will call the <code>construct</code> method
   * and then exit.
   */
  public SwingWorker() {
    super();
    listeners = new ArrayList<PropertyChangeListener>();
    progress = new Integer(0);
    final Runnable doFinished = new Runnable() {
      public void run() {
        finished();
      }
    };

    Runnable doConstruct = new Runnable() {
      public void run() {
        boolean ok = true;
        try {
          setValue(construct());
        }
        catch (OutOfMemoryError err) {
          ok = false;
        }
        finally {
          threadVar.clear();
        }
        if (ok) {
          SwingUtilities.invokeLater(doFinished);
        }
      }
    };

    Thread t = new Thread(doConstruct);
    threadVar = new ThreadVar(t);
  }

  /**
   * Sleep the worker thread.
   * @param ms The number of milliseconds to sleep the thread
   */
  public void sleep(int ms) throws InterruptedException {
    //Thread t = threadVar.get();
    //if (t != null) {
      Thread.sleep(ms);
    //}
  }

  /**
   * Start the worker thread.
   */
  public void start() {
    Thread t = threadVar.get();
    if (t != null) {
      t.start();
    }
  }

  public void join() throws InterruptedException {
    Thread t = threadVar.get();
    if (t != null) {
      t.join();
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    if (listener != null) {
      listeners.remove(listener);
    }
  }

  protected final void setProgress(int progress) {
    Integer p = new Integer(progress);
    Iterator<PropertyChangeListener> i = listeners.iterator();
    while (i.hasNext()) {
      PropertyChangeListener listener = i.next();
      listener.propertyChange(new PropertyChangeEvent(this, "progress",
          this.progress, p));
    }
    this.progress = p;
  }

}
