//package org.lib;
//
//
//public interface TransferListener {
//	void transferComplete(Object o);
//	void transferFailed(Object o, Throwable err);
//	
//	/** totalBytes is -1 if not known. */
//	void transferProgress(Object o, long bytesTransfered, long totalBytes);
//	boolean isCancelled();
//	
//	public static class Wrapper {
//		final private TransferListener l;
//		final private Object src;
//		final private long totalBytes;
//		public Wrapper(TransferListener l, Object src, long totalBytes) {
//			this.l = l; this.src = src; this.totalBytes = totalBytes;
//		}
//		public void update(long bytesTransfered) {
//			l.transferProgress(src, bytesTransfered, totalBytes);
//		}
//		public void complete() {
//			l.transferComplete(src);
//		}
//		public void failed(Exception err) {
//			l.transferFailed(src, err);
//		}
//		
//		public boolean isCancelled() {
//			return l.isCancelled();
//		}
//	}
//	
//	public class TransferAdapter implements TransferListener {
//		@Override public void transferComplete(Object o) {
//		}
//
//		@Override public void transferFailed(Object o, Throwable err) {
//		}
//
//		@Override public void transferProgress(Object o, long bytesTransfered, long totalBytes)
//		{
//			double completion = totalBytes > 0 ? 1.0 * bytesTransfered / totalBytes : 0;
//			transferProgress(o, completion);
//		}
//		
//		public void transferProgress(Object o, double completion)	{	}
//
//		@Override public boolean isCancelled() {
//			return false;
//		}
//		
//	}
//}
