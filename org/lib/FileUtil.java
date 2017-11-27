package org.lib;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileUtil {
	public static int TRANSFER_CHUNK = 512;
//	public static ArrayList<File> list;

	public static String concat(File root, String... paths) {
		String[] paths_ = new String[paths.length + 1];
		paths_[0] = root == null ? "" : root.getAbsolutePath();
		System.arraycopy(paths, 0, paths_, 1, paths.length);
		return concat(paths_);
	}

	/** joins the given file paths for the current operating system. Note: does not resolve directory traversals (/../) */
	public static String concat(String... paths) {
		// StringBuilder sb = new StringBuilder();
		// int len;
		// for (String path : paths) {
		// if (path == null)
		// continue;
		// len = sb.length();
		// if (len > 0 && !sb.substring(len-1).equals(File.separator)
		// && path.length() > 0 && !path.startsWith(File.separator) )
		// sb.append(File.separatorChar);
		//
		// sb.append(path);
		// }
		//
		// return sb.toString();
		return StringUtil.ensureSeparator(File.separator, paths);
	}

	public static String stripExt(String filename) {
		int p = filename.lastIndexOf('.');
		if (p > 0)
			return filename.substring(0, p);
		return filename;
	}

	public static String getExt(String filename) {
		int p = filename.lastIndexOf('.');
		if (p > 0)
			return filename.substring(p + 1);
		return "";
	}

	public static boolean isCSV(File f) {
		String ext = getExt(f.getName()).toLowerCase();
		return "csv".equals(ext);
	}

	public static String getBasename(String filepath) {
		return getBasename(filepath, false);
	}

	public static String getBasename(String filepath, boolean stripExt) {
		String name = new File(filepath).getName();
		return stripExt ? stripExt(name) : name;
	}

	public static String getDirname(String filepath) {
		return new File(filepath).getParent();
	}

	public static File getParent(File f) {
		if (f == null)
			return null;
		File parent = f.getParentFile();
		if (parent == null)
			return f;
		return parent;
	}

	public static String extFromFileName(String fn) {
		int indx = fn.lastIndexOf('.');

		if (indx == -1)
			return "";

		return fn.substring(indx + 1);
	}

//	
	/**
	 * Creates the file and any parent directories of the file if they don't already exist.
	 * 
	 * @param file
	 * @return true if the file was created, false otherwise.
	 * @throws IOException
	 */
	public static boolean createFile(File file) throws IOException {
		File parent = file.getParentFile();

		if (parent != null && !parent.isDirectory()) {
			parent.mkdirs();
		}

		return file.createNewFile();
	}

	public static boolean copyStreamToFile(final InputStream is, final File file) {
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		try {
			bis = new BufferedInputStream(is);
			fos = new FileOutputStream(file);
			int x;
			while ((x = bis.read()) > -1) {
				fos.write((byte) x);
			}
			is.close();
			bis.close();
			fos.close();
		} catch (Exception ex) {
			System.out.println("copyStream ex " + ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static class FileTransferable implements Transferable {
		ArrayList<File> fileList = new ArrayList<File>();

		public FileTransferable(File f) {
			fileList.add(f);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) {
			if (flavor == DataFlavor.javaFileListFlavor)
				return fileList;
			if (flavor == DataFlavor.stringFlavor)
				return fileList.toString();
			return null;
		}

		DataFlavor dfa[] = new DataFlavor[] { DataFlavor.stringFlavor, DataFlavor.javaFileListFlavor };

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return dfa;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor == DataFlavor.javaFileListFlavor || flavor == DataFlavor.stringFlavor;
		}
	}

	private static List<String> getPathList(File file) {
		List<String> result = new ArrayList<String>();
		File tmpFile = file;
		while (tmpFile != null) {
			if (tmpFile.getName().length() > 0) {
				if (!tmpFile.getName().equals(".")) {
					result.add(0, tmpFile.getName());
				}
			} else {
				// a folder without a name is the disk drive on windows, add it here
				result.add(0, tmpFile.getPath());
			}
			tmpFile = tmpFile.getParentFile();
		}
		return result;
	}
//
//	// The childFile may be in a folder below the parent file. If so, return the relative path of the child file.
//	public static String getRelativeChildPath(File parentFile, File childFile) {
//		if (parentFile == null || childFile == null)
//			return null;
//		List<String> childPaths = getPathList(childFile.getParentFile());
//		List<String> parentPaths = getPathList(parentFile.getParentFile());
//		if (parentPaths.size() <= childPaths.size()) {
//			for (int i = 0; i < parentPaths.size(); i++) {
//				String parentFolder = parentPaths.get(i);
//				String childFolder = childPaths.get(i);
//				if (!parentFolder.equals(childFolder))
//					return null; // child file is not below the parent
//			}
//		} else
//			return null;
//		StringBuffer buffer = new StringBuffer();
//		buffer.append(".").append(File.separator);
//		for (int i = parentPaths.size(); i < childPaths.size(); i++) {
//			buffer.append(childPaths.get(i)).append(File.separator);
//		}
//		buffer.append(childFile.getName());
//		return buffer.toString();
//	}

	static String LINEFEED = "\n";
//
//	public static File insertHeader(File f, String header) {
//		File outFile = new File(f.getPath() + ".wsp");
//		BufferedReader input = null;
//		BufferedWriter output = null;
//		try {
//			input = new BufferedReader(new FileReader(f));
//			output = new BufferedWriter(new FileWriter(outFile));
//			String line = null;
//			output.append(header + LINEFEED);
//			while ((line = input.readLine()) != null) {
//				line = processLine(line);
//				output.append(line + LINEFEED);
//			}
//		} catch (IOException ex) {
//			System.err.println(ex.getMessage());
//		} finally {
//			try {
//				input.close();
//				output.close();
//			} catch (IOException ex) {
//				System.err.println("Error closing files in insertHeader");
//			}
//		}
//		return outFile;
//	}
//
//	public static File cleanFile(File f) {
//		if (f.getPath().endsWith(".73"))
//			return null; // if this fails, don't keep looping
//		File outFile = new File(f.getPath() + ".73");
//		BufferedReader input = null;
//		BufferedWriter output = null;
//		try {
//			input = new BufferedReader(new FileReader(f));
//			output = new BufferedWriter(new FileWriter(outFile));
//			String line = null;
//			while ((line = input.readLine()) != null) {
//				line = processLine(line);
//				output.append(line + LINEFEED);
//			}
//		} catch (IOException ex) {
//			System.err.println(ex.getMessage());
//		} finally {
//			try {
//				input.close();
//				output.close();
//			} catch (IOException ex) {
//				System.err.println("Error closing files in cleanFile");
//			}
//		}
//		return outFile;
//	}
//
//	private static String processLine(String aLine) {
//		return aLine.replaceAll("0�", "0");
//	}

	static public String getEndOfPath(String in) {
		String endPath = "";
		if (in.indexOf('/') < 0 && !HomeEnv.isWindows()) {
			endPath = in.substring(in.lastIndexOf(File.separatorChar) + 1);
		} else {
			int index = in.lastIndexOf(File.separator) + 1;
			if (index == 0 && in.lastIndexOf('/') > 0) {
				index = in.lastIndexOf('/') + 1;
			}
			endPath = in.substring(index);
		}

		if (endPath.equals(in)) {
			if (in.indexOf('\\') < 0 && !HomeEnv.isWindows()) {
				endPath = in.substring(in.lastIndexOf('\\') + 1);
			} else {
				int index = in.lastIndexOf('\\') + 1;
				if (index == 0 && in.lastIndexOf('\\') > 0) {
					index = in.lastIndexOf('\\') + 1;
				}
				endPath = in.substring(index);
			}
		}
		return endPath;
	}

	static public File checkValidPath(String inName, File wsPath) {
		// String clean = inName.replaceAll("%20", " ");
		File file = new File(inName); // first see if absolute works

		if (file.isFile() || UriUtil.isRemote(inName))
			return file;
		String endPath = getEndOfPath(inName);

		String s = FileUtil.concat(HomeEnv.getInstance().getUserDocumentsFolder(), endPath);
		file = new File(s);
		if (file.isFile())
			return file; // try relative to user documents folder

		if (wsPath != null) {
			String name = wsPath.getName();
			String wspath = name.substring(0, name.lastIndexOf(File.separator) + 1);
			s = wspath + endPath;
			file = new File(s);
			if (file.isFile())
				return file; // try relative to project folder

			if (inName.startsWith(".")) {
				s = wspath + inName;
			}
			file = new File(s);
			if (file.isFile())
				return file;
		}
		return null;
	}

	public static String getExtension(File file) {
		String filename = file.getName();
		int extIdx = filename.lastIndexOf('.');
		return filename.substring(extIdx + 1);
	}

	public static String uniqueName(String inName) {
		File f = new File(inName);
		if (!f.exists())
			return inName;
		String baseName = inName;
		int version = 1;
		String extension = "";
		int extensionIndex = inName.lastIndexOf('.');
		if (extensionIndex > inName.length() - 6) // assumes extension won't be over 5 chars
		{
			extension = inName.substring(extensionIndex);
			baseName = baseName.substring(0, extensionIndex);
		}
		int idx = baseName.lastIndexOf('-');
		if (idx > 0) {
			String suffix = baseName.substring(idx + 1);
			if (ParseUtil.isNumber(suffix)) {
				version = ParseUtil.getInteger(suffix) + 1;
				baseName = baseName.substring(0, idx);
			}
		} else {
			baseName = baseName + '-';
		}

		while (true) {
			String name = baseName + version + extension;

			if (!exists(name))
				return name;
			version++;
		}
	}

	public static File ensureValidExtension(File file, String... acceptedExts) {
		String pathname = ensureValidExtension(file.getAbsolutePath(), acceptedExts);
		return new File(pathname);
	}

	public static String ensureValidExtension(String filename, String... acceptedExts) {
		for (String ext : acceptedExts) {
			if (ext == null || ext.isEmpty()) {
				continue;
			}
			String suffix = StringUtil.ensurePrefix(ext, ".");
			if (filename.toLowerCase().endsWith(suffix.toLowerCase()))
				return filename;
		}
		if (acceptedExts.length > 0) {
			String suffix = acceptedExts[0];
			if (suffix != null && !suffix.isEmpty())
				return filename + StringUtil.ensurePrefix(suffix, ".");
		}
		return filename;
	}

	public static boolean hasValidExtension(File file, String... acceptedExts) {
		return hasValidExtension(file.getName(), acceptedExts);
	}

	public static boolean hasValidExtension(String filename, String... acceptedExts) {
		for (String ext : acceptedExts) {
			String suffix = ext.startsWith(".") ? ext : "." + ext;
			if (filename.toLowerCase().endsWith(suffix.toLowerCase()))
				return true;
		}
		return false;
	}

	public static boolean ensureDirectoryExists(String path) {
		return ensureDirectoryExists(new File(path));
	}

	public static boolean ensureDirectoryExists(File dir) {
		if (dir == null)
			return false;
		if (dir.isDirectory())
			return true;
		else if (!dir.exists())
			return dir.mkdirs();
		return false;
	}

	public static boolean ensureParentExists(File f) {
		if (f == null)
			return false;
		return ensureDirectoryExists(f.getParent());
	}

	public static boolean exists(String fileName) {
		return fileName == null ? false : new File(fileName).exists();
	}

	public static boolean exists(File file) {
		return file == null ? false : file.exists();
	}

	public static boolean isFile(File file) {
		return file == null ? false : file.isFile();
	}

	public static boolean isHidden(File file) {
		return file.isHidden() || file.getName().startsWith(".");
	}

	public static boolean isLink(File file) {
		return file.getName().endsWith(".lnk");
	} // Doubtful this test works on mac aliases

	public static boolean fileInTrash(File f) {
		String path = f.getAbsolutePath();
		if (path.contains(".Trash"))
			return true; // TODO INTL MAC, ENGLISH SPECIFIC?
		if (path.contains("$RECYCLE.BIN"))
			return true;
		return false;
	}

	public static final Pattern WINDOWS_DRIVE_PATTERN = Pattern.compile("^[a-zA-Z]:\\\\.*");

	public static boolean startsWithWindowsDrivePrefix(String path) {
		return WINDOWS_DRIVE_PATTERN.matcher(path).matches();
	}

	/** Only pass individual file path components, as this method removes separator characters */
	public static String removeUnsafeChars(String filename) {
		return filename.replace('/', '_').replace('\\', '_');
	}

//	// AM moved from GraphWinExport
//	public static String removeSpaces(String filename) {
//		String os = System.getProperty("os.name").toLowerCase();
//		if (os.startsWith("window")) {
//			int endPathIndex = filename.lastIndexOf(File.separatorChar);
//			if (endPathIndex >= 0 && endPathIndex < filename.length()) {
//				// folder is delineated by double quotes
//				String folder = filename.substring(0, endPathIndex + 1);
//				String docFilename = filename.substring(endPathIndex + 1);
//				docFilename = docFilename.replaceAll(" ", "_");
//				return folder + docFilename;
//			}
//		}
//		return filename;
//	}
////
////	public static int sizeRecursive(Collection<File> inFiles, boolean includeHidden) {
////		int size = 0;
////		for (File file : inFiles) {
////			if (!includeHidden && file.isHidden()) {
////				continue;
////			}
////			if (file.isDirectory()) {
////				for (String childFile : file.list()) {
////					// Recursive call
////					size += sizeRecursive(Arrays.asList(new File(childFile)), includeHidden);
////				}
////			}
////			// Base case
////			else { // if(file.isFile()){
////				size++;
////			}
////		}
////		return size;
////	}
//
//	public static boolean deleteDir(File dir) {
//		File[] files = dir.listFiles();
//		if (files != null) {
//			for (File f : files) {
//				deleteDir(f);
//			}
//		}
//		return dir.delete();
//	}
	public static Map<String, String> readMap(File f)
	{
		Map<String, String> map = new HashMap<String, String>();
		boolean firstLine = true;
		String raw = readFile(f);
		if (raw != null) 
		{
			String[] lines = raw.split("\n"); 
			for (String line : lines)
			{
				if (firstLine) { firstLine = false;	 continue; }
				if (line.startsWith("#")) {  continue; }				
				int delim = line.indexOf("=");
				if (delim>0)
					map.put(line.substring(0,delim),  line.substring(delim+1));
			}
		}
		return map;
	}
	
	
	public static String getTimestamp() {
		SimpleDateFormat fmt = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
		Date date = new Date(System.currentTimeMillis());
		String stringDate = fmt.format(date);
		return "#" + stringDate; 
	}
	
	public static void writeMap(Map<String, String> props, File f) throws IOException
	{
		String time = getTimestamp() + "\n";
		StringBuilder buff = new StringBuilder(time);
		for (String prop : props.keySet())
			buff.append(prop).append("=").append(props.get(prop)).append("\n");
		write(f, buff.toString());
	}
	
	public static String readFile(File f) {
		StringBuffer accum = new StringBuffer();
		BufferedReader buffer = null;
		FileReader reader = null;
		try {
			reader = new FileReader(f);
			buffer = new BufferedReader(reader);
			String line;
			while ((line = buffer.readLine()) != null) {
				accum.append(line + '\n');
			}
		} catch (Exception e) {
			System.out.println("Error reading file: " + f + " :: " + e);

		} finally {
			FileUtil.close(buffer);
		}
		return accum.toString();
	}

	public static byte[] read(InputStream in, int maxBytes) throws IOException {
		return read(in, maxBytes, TRANSFER_CHUNK);
	}
	
	public static String readUTF8(InputStream in, int maxBytes) throws IOException
	{
		byte[] bytes = read(in, maxBytes);
		return new String(bytes, "UTF-8");
	}

	public static byte[] read(final InputStream in, int maxBytes, int chunkSize) throws IOException {
		int readCnt = 1;
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		final byte[] buffer = new byte[chunkSize];

		while (readCnt >= 0) {
			readCnt = in.read(buffer);
			if (readCnt >= 0) {
				data.write(buffer, 0, readCnt);
				if (data.size() > maxBytes)
					throw new IOException("Maximum read count exceeded");
			}
		}
		return data.toByteArray();
	}

	public static void write(File f, String content) throws IOException {
		f.createNewFile(); // ensure file exists
		if (StringUtil.isEmpty(content))
			return;
		Writer output = new BufferedWriter(new FileWriter(f));
		try {
			output.write(content);
		} finally {
			output.close();
		}
	}

//	public static void transfer(InputStream is, OutputStream os) throws IOException {
//		transfer(is, os, null);
//	}
//
//	public static void transfer(InputStream is, OutputStream os, TransferListener.Wrapper l) throws IOException {
//		transfer(is, os, TRANSFER_CHUNK_SIZE, l);
//	}
//
//	public static void transfer(InputStream is, OutputStream os, int chunkSize, TransferListener.Wrapper l) throws IOException {
//		int len;
//		byte[] buf = new byte[chunkSize];
//		long bytesTransfered = 0;
//
//		try {
//			while ((len = is.read(buf)) >= 0) {
//				if (l != null && l.isCancelled())
//					throw new IOException(CANCELLED.lookup());
//				os.write(buf, 0, len);
//				bytesTransfered += len;
//				if (l != null) {
//					l.update(bytesTransfered);
//				}
//			}
//			if (l != null) {
//				l.complete();
//			}
//
//		} catch (IOException ex) {
//			if (l != null) {
//				l.failed(ex);
//			}
//			throw ex;
//		} finally {
//			close(is);
//			close(os);
//		}
//	}

	public static boolean close(Closeable o) {
		if (o == null)
			return true;
		try {
			o.close();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public static boolean close(Socket socket) {
		if (socket == null)
			return true;
		try {
			socket.close();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	// Sortable helper for oldest to newest file arrays/collections
	public static FileLastModifiedComparable getFileLastModifiedComparable() {
		return new FileLastModifiedComparable();
	}

	public static class FileLastModifiedComparable implements Comparator<File> {
		@Override
		public int compare(File file1, File file2) {
			if (file1.lastModified() == file2.lastModified())
				return 0;
			else
				return file1.lastModified() > file2.lastModified() ? -1 : 1;
		}
	}

	public static List<File> collectFiles(String path, String suffix) {

		File root = new File(path);
		List<File> list = new ArrayList<File>();

		for (File f : root.listFiles()) {
			if (f.isDirectory()) {
				list.addAll(collectFiles(f.getAbsolutePath(), suffix));
			} else {
				if (f.getName().toLowerCase().endsWith(suffix.toLowerCase())) {
					list.add(f);
				}
			}
		}
		return list;

	}
}
