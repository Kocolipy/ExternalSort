import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExternalSort {
	public static int quickSortSize = 1024;
	public static int fileSize; //number of integers in the file
	
	public static void sort(String f1, String f2) throws FileNotFoundException, IOException {
		DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(f1))));
		fileSize = (int) input.available()/4;
		if (fileSize == 0){ input.close(); return;} //file is empty	
		
		RandomAccessFile f1a = new RandomAccessFile(f1,"rw");
		RandomAccessFile f2a = new RandomAccessFile(f2,"rw");
		DataOutputStream f1d = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f1a.getFD())));
		DataOutputStream f2d = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f2a.getFD())));
		
		//perform quicksort on partitions of quickSortSize
		quickSortSize = (int) (Math.min(quickSortSize, fileSize)); 
		int numDivisions = (int) (Math.floor(fileSize/quickSortSize));
		int excess = (int) (fileSize%(numDivisions*quickSortSize));
		int[] tmp = new int[quickSortSize];
		for (int j = 0; j < numDivisions; j++){
			for (int i = 0; i< quickSortSize; i++){
				tmp[i] = input.readInt();
			}
			quickSort(tmp);
			for (int i: tmp){
				f2d.writeInt(i);
			}
		}
		tmp = new int[excess];
		for (int i = 0; i< excess; i++){
			tmp[i] = input.readInt();
		}
		quickSort(tmp);
		for (int i: tmp){
			f2d.writeInt(i);
		}
		f2d.flush();
		input.close();
		
		//Perform Merge sort on the larger partitions
		int iteration = (int) Math.ceil(Math.log(fileSize/(quickSortSize))/Math.log(2));
		for (int i  = 0; i < iteration; i++){
			int size = (int) Math.pow(2, i)*quickSortSize;
			if (i%2 == 0){ 
				f1a.seek(0);
				stepSorter(f2, f1d, size);
			} else {
				f2a.seek(0);
				stepSorter(f1, f2d, size);
			}
		}

		//Copies f2 to f1 if the iteration ends with f2
		if (iteration%2==0){
			FileChannel source = null;
			FileChannel destination = null;
			try {
				source = new FileInputStream(f2).getChannel();
				destination = new FileOutputStream(f1).getChannel();
		        destination.transferFrom(source, 0, source.size());
		    }
		    finally {
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
		    }
		}
		f1a.close();
		f2a.close();
		f1d.close();
		f2d.close();
	}
	public static void quickSort(int[] array) {
		quickSortHelper(array, 0, array.length);
	}
	private static void quickSortHelper(int[] a, int start, int end) {
		if (end - start <= 1){
			return;
		}
		int pivot = a[start];
		int[] smaller = new int[end-start-1];		
		int[] bigger = new int[end-start-1];		
		int smallCount = 0;
		int bigCount = 0;
		for (int i=start+1; i < end; i++){
			if (pivot >= a[i]){
				smaller[smallCount] = a[i];
				smallCount++;
			} else {
				bigger[bigCount] = a[i];
				bigCount++;
			}
		}
		for (int i=start; i < end; i++){
			if (i - start < smallCount){
				a[i] = smaller[i-start];
			} else if (i - start == smallCount){
				a[i] = pivot;
			} else{
				a[i] = bigger[i-start-smallCount -1];
			}
		}
		quickSortHelper(a, start, start+smallCount);
		quickSortHelper(a, start+smallCount + 1, end);
	}
	private static void stepSorter(String file, DataOutputStream d,  int size) throws FileNotFoundException, IOException {
		/* file is the name of the file which we will use to get data to sort
		 * d is the dataoutputstream writing the sorted data
		 * size is the current size of the merge sort divisions
		 */
		DataInputStream inputA = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(file))));
		DataInputStream inputB = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(file))));
		int numDivisions = (int) Math.ceil(fileSize/((double) size));
		inputB.skip(size*4);
		for (int j = 0; j < numDivisions/2; j++){	
			int counterA = size;
			int counterB = Math.min(fileSize - j*size*2 - size, size);
			Integer a = inputA.readInt();
			Integer b = inputB.readInt();
			while (counterA > 0 && counterB > 0){
				if (a < b){
					d.writeInt(a);
					if (counterA > 1){
						a = inputA.readInt();
					}
					counterA--;
				} else {
					d.writeInt(b);
					if (counterB > 1){
						b = inputB.readInt();
					}
					counterB--;
				}
			}
			if (counterA == 0){
				d.writeInt(b);
				counterB--;
				for (int i =0; i < counterB; i++){
					d.writeInt(inputB.readInt());
				}
			} else {
				d.writeInt(a);
				counterA--;
				for (int i =0; i < counterA; i++){
					d.writeInt(inputA.readInt());
				}
			}
			inputA.skip(size*4);
			inputB.skip(size*4);
		}
		
		//copy the excess
		while (inputA.available()>0){
			d.writeInt(inputA.readInt());
		}
		inputA.close();
		inputB.close();
		d.flush();
	}
	private static String byteToHex(byte b) {
		String r = Integer.toHexString(b);
		if (r.length() == 8) {
			return r.substring(6);
		}
		return r;
	}

	public static String checkSum(String f) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			DigestInputStream ds = new DigestInputStream(
					new FileInputStream(f), md);
			byte[] b = new byte[512];
			while (ds.read(b) != -1)
				;

			String computed = "";
			for(byte v : md.digest()) 
				computed += byteToHex(v);

			return computed;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "<error computing checksum>";
	}
}
