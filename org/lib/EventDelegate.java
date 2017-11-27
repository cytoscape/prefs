package org.lib;

import java.lang.ref.WeakReference;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

public class EventDelegate<T extends EventObject> implements EventListener {
	private static boolean test = false;
	private boolean dispatchLater = false;
	private EventListenerList		listenerList		= new EventListenerList();
//	private Class<T>				listenerClass;
	
	public EventDelegate(boolean dispatchLater)
	{
		this();
		this.dispatchLater = dispatchLater;
	}
	
	public EventDelegate() {
	}
	
//	/** Return an instance pointer to this delegate incapable of handling dispatch orders */
//	public ListenableDelegate<T> getListenOnly()		{	return new ListenableDelegate<T>(this);	}
//	
	public void dispatch(final T event) {
		Runnable runner = new Runnable()
		{
			public void run()
			{
				// Guaranteed to return a non-null array
			     Object[] listeners = listenerList.getListenerList();
			     // Process the listeners last to first, notifying
			     // In this class, all event listeners are interested in this event
			     for (int i = listeners.length-2; i>=0; i-=2) {
			    	 ListenerWrapper<T> wrapper = (ListenerWrapper<T>)listeners[i+1];
			    	 EventListener listener = wrapper.get();
			    	 if (listener == null)
			    	 {
			    		 if (test) System.out.println("-- Removing dead listener reference --");
			    		 listenerList.remove(ListenerWrapper.class, wrapper);		//prune dead listeners
			    	 }
//			    	 else
//			    		 listener.fjEvent(event);
			     }
			}
		};
		if (dispatchLater)
			SwingUtilities.invokeLater(runner);
		else
			runner.run();
		
	}
	
	public void add(EventListener listener)
	{
		add(listener, false);
	}
	
	public void add(EventListener listener, boolean weakly)
	{
		if (listener == null) return;
		ListenerWrapper<T> wrapper = weakly ? new WeakListenerWrapper<T>(listener) : new NormalListenerWrapper<T>(listener);
		listenerList.add(ListenerWrapper.class, wrapper);
	}
	public void remove(EventListener listener)
	{
		if (listener == null) return;
		ListenerWrapper<T> wrapper = new NormalListenerWrapper<T>(listener);		//since we're removing, the wrapper is only used for comparison and the 'weakness' doesn't matter
		listenerList.remove(ListenerWrapper.class, wrapper);
	}
	
//	@Override public void fjEvent(T event)	{	dispatch(event);	}
	
	protected static abstract class ListenerWrapper<T extends EventObject> implements EventListener {
		public boolean isValid()
		{
			return get() != null;
		}
		
		public abstract EventListener get();
		
		public boolean equals(Object o)
		{
			if (this == o) return true;			//same wrapper
			Object target = get();
			if (target == null) return false;
			if (o instanceof ListenerWrapper)
			{
				o = ((ListenerWrapper<?>) o).get();
				return o == null ? false : target.equals(o);	//o is a wrapper of the same target
			}
			return target.equals(o);			//o is the target
		}
	}
	
	public static class NormalListenerWrapper<T extends EventObject> extends ListenerWrapper<T>
	{
		private EventListener target;
		public NormalListenerWrapper(EventListener target) {		this.target = target;	}
		@Override public EventListener get()				{		return target;			}
		@Override public String toString()
		{
			return target == null ? "<empty wrapper>" : target.toString();
		}
	}
	
	public static class WeakListenerWrapper<T extends EventObject> extends ListenerWrapper<T>
	{
		private WeakReference<EventListener> ref;
		public WeakListenerWrapper(EventListener target)	{		this.ref = new WeakReference<EventListener>(target);	}
		@Override public EventListener get()				{		return ref.get();		}
		@Override public String toString()
		{
			EventListener l = get();
			return l == null ? "<expired weak listener wrapper>" : ("weak listener wrapper(" + l + ")");
		}
	}
//	
//	/** TODO AM 12/24/12 Testing output class.  Should be a unit test someday. */
//	public static void main(String[] args)
//	{
//		System.out.println("Running test: 3 hard refs, 3 unreferenced soft refs, 1 referenced soft ref, and 1 referenced hard ref");
//		EventDelegate<EventObject> delegate = new EventDelegate<EventObject>();
//		TestEventListener referencedSoftListener = new TestEventListener("Soft, but externally referenced 1");
//		TestEventListener referencedHardListener = new TestEventListener("Hard, and externally referenced 1");
//		delegate.add(new TestEventListener("Hard ref 1"));
//		delegate.add(new TestEventListener("Soft ref 1"), true);
//		delegate.add(new TestEventListener("Hard ref 2"));
//		delegate.add(new TestEventListener("Hard ref 3"));
//		delegate.add(new TestEventListener("Soft ref 2"), true);
//		delegate.add(new TestEventListener("Soft ref 3"), true);
//		delegate.add(referencedHardListener, false);
//		delegate.add(referencedSoftListener, true);
//		System.out.println("Firing event BEFORE GC ------------");
//		delegate.dispatch(new EventObject(new Object()));
//		System.gc();
//		System.out.println("Firing event AFTER GC ------------");
//		delegate.dispatch(new EventObject(new Object()));
//		System.out.println("------- done ---------");
//		System.out.println("Remaining listener count (should be 5): " + delegate.listenerList.getListenerCount());
//		System.out.println("Removing referenced listeners.");
//		delegate.remove(referencedSoftListener);
//		delegate.remove(referencedHardListener);
//		System.out.println("------- Firing event ---------");
//		delegate.dispatch(new EventObject(new Object()));
//		System.out.println("Remaining listener count (should be 3): " + delegate.listenerList.getListenerCount());
//	}
	
//	private static class TestEventListener implements EventListener
//	{
//		private String name;
//		public TestEventListener(String name)	{	this.name = name;	}
//		@Override public void event(EventObject event) {
//			System.out.println("Event dispatched to " + this);
//		}
//		@Override public String toString()		{	return String.format("EventListener[%s]", name);	}
//	}
	
}
