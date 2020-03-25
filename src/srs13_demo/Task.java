package srs13_demo;

import java.io.*;
import java.util.*;

public class Task {

	public static String keyFile = "Keyfile.bin";
	public static String byteOffset = "ByteOffset.bin";
	public static int sizeOfRun;
	public static int fileSize = 64;
	public String[] res;
	public String[] res2;
	public int remainGlobal;
	public Vector<ArrayList<Integer>> sortNums;

	public void setSortNums(Vector<ArrayList<Integer>> sortNums) {
		this.sortNums = sortNums;
	}

	public Vector<ArrayList<Integer>> getSortNums() {
		return sortNums;
	}

	public void getKeys(String Inputfilename) throws IOException {
		RandomAccessFile file = new RandomAccessFile(Inputfilename, "rw");
		RandomAccessFile file2 = new RandomAccessFile(keyFile, "rw");
		RandomAccessFile file3 = new RandomAccessFile(byteOffset, "rw");
//		Vector<Integer> v = new Vector<Integer>();
		int counter1 = 6;
		int counter2 = 1;
		int counter3 = 12;
		int res = 0;
		file.seek(counter1);
		for (int i = 0; i < 64; i++) {
			file2.writeInt(file.readInt());
			res = counter1 * (counter2 += 3);
			file.seek(res);
		}

		file2.close();
		counter1 = 6;
		counter2 = 1;
		file.seek(counter3);
		for (int i = 0; i < 64; i++) {
			file3.writeInt(file.readInt());
			res = counter1 * (counter2 += 3);
			res += 6;
			file.seek(res);
		}
		file.close();
		file3.close();

	}

	String[] DivideInputFileIntoRuns(String Inputfilename, int runSize) throws IOException {
		sizeOfRun = runSize;

		getKeys(Inputfilename);

		RandomAccessFile keyFile = new RandomAccessFile(Task.keyFile, "rw");
		keyFile.seek(0);
		long newSize = keyFile.length() / 4;
		long loopSize = newSize / runSize;
		long remaining = newSize - (loopSize * runSize);
		remainGlobal = (int) remaining;

		if (remaining > 0) {

			res = new String[(int) loopSize + 1];
			res2 = new String[(int) loopSize + 1];
		} else {
			res = new String[(int) loopSize];
			res2 = new String[(int) loopSize];
		}
		for (int i = 0; i < loopSize; i++) {

			RandomAccessFile runFiles = new RandomAccessFile("runFiles" + i + ".txt", "rw");
			for (int j = 0; j < runSize; j++) {

				runFiles.writeInt(keyFile.readInt());

			}
			runFiles.close();

			if (i == loopSize - 1 && remaining > 0) {
				for (int top = 0; top < 1; top++) {
					RandomAccessFile runFiles2 = new RandomAccessFile("runFilesRemaining" + top + ".txt", "rw");
					for (int k = 0; k < remaining; k++) {
						runFiles2.writeInt(keyFile.readInt());
					}
					runFiles2.close();
				}
			}

		}

		keyFile.close();

		for (int i = 0; i < loopSize; i++) {
			res[i] = "runFiles" + i + ".txt";
		}

		if (remaining > 0) {

			res[(int) (loopSize)] = "runFilesRemaining" + 0 + ".txt";
		}

		return res;

	}

	String[] SortEachRunOnMemoryAndWriteItBack(String[] RunsFilesNames) throws IOException {

		sortNums = new Vector<ArrayList<Integer>>(RunsFilesNames.length);
		int newRunSize = sizeOfRun - remainGlobal;
		for (int i = 0; i < RunsFilesNames.length; i++) {

			RandomAccessFile runFiles = new RandomAccessFile(RunsFilesNames[i], "rw");
			ArrayList<Integer> eachRunV2 = new ArrayList<Integer>();
			if (remainGlobal > 0 && i == RunsFilesNames.length - 1) {
				for (int j = 0; j < sizeOfRun - newRunSize; j++) {
					eachRunV2.add(runFiles.readInt());
				}
				sortNums.add(eachRunV2);
				runFiles.close();
				break;
			}
			for (int j = 0; j < sizeOfRun; j++) {

				eachRunV2.add(runFiles.readInt());

			}
			sortNums.add(eachRunV2);
			runFiles.close();
		}

		for (int i = 0; i < sortNums.size(); i++) {
			Collections.sort(sortNums.elementAt(i)); // sorting each run here.
			if (i == sortNums.size() - 1 && remainGlobal > 0) {
				for (int top = 0; top < 1; top++) {
					RandomAccessFile runFilesRemainSorted = new RandomAccessFile(
							"runFilesSORTEDRemaining" + top + ".txt", "rw");
					for (int k = 0; k < remainGlobal; k++) {
						runFilesRemainSorted.writeInt(sortNums.elementAt(i).get(k));
					}
					runFilesRemainSorted.close();
				}
				break;
			}
			RandomAccessFile runFilesSorted = new RandomAccessFile("runFilesSORTED" + i + ".txt", "rw");
			for (int j = 0; j < sizeOfRun; j++) {
				runFilesSorted.writeInt(sortNums.elementAt(i).get(j));
			}
			runFilesSorted.close();

		}

		for (int j = 0; j < sortNums.size(); j++) {
			System.out.println(sortNums.elementAt(j)); // displaying each sorted run here.

		}

		for (int i = 0; i < RunsFilesNames.length; i++) {
			res2[i] = "runFilesSORTED" + i + ".txt";
		}

		if (remainGlobal > 0) {

			res2[sortNums.size() - 1] = "runFilesSORTEDRemaining" + 0 + ".txt";
		}

		return res2;

	}// you should display each run after sorting it

	void DoKWayMergeAndWriteASortedFile(String[] SortedRunsNames, int K, String Sortedfilename) throws IOException {
		RandomAccessFile sortedFile = new RandomAccessFile(Sortedfilename, "rw");
		KMerge merger = new KMerge();
		int[] sortedRunsArr = new int[fileSize];
		int seek = 0;
		int counter = 0;
		int loopCounter = 0;
		if (remainGlobal > 0) {
			loopCounter = (fileSize / sizeOfRun) + 1;
		} else {
			loopCounter = fileSize / sizeOfRun;
		}

		RandomAccessFile sortedFiles;
		for (int i = 0; i < loopCounter; i++) {
			if (i == loopCounter - 1 && remainGlobal > 0) {
				seek = 0;
				for (int j = 0; j < remainGlobal; j++) {
					sortedFiles = new RandomAccessFile("runFilesSORTEDRemaining0.txt", "rw");
					sortedFiles.seek(seek);
					sortedRunsArr[counter] = sortedFiles.readInt();
					seek += 4; // next number in file [34,363,...]
					sortedFiles.close();
					counter++;
				}
				break;
			}

			for (int j = 0; j < sizeOfRun; j++) {
				sortedFiles = new RandomAccessFile("runFilesSORTED" + i + ".txt", "rw");
				sortedFiles.seek(seek);
				sortedRunsArr[counter] = sortedFiles.readInt();
				seek += 4; // next number in file [34,363,...]
				sortedFiles.close();
				counter++;
			}
			seek = 0;
		}
		merger.sort(sortedRunsArr, K);

		/** Write output to a new file */
		seek = 0;
		sortedFile.seek(seek);
		for (int i = 0; i < sortedRunsArr.length; i++) {
			sortedFile.writeInt(sortedRunsArr[i]);
		}
		sortedFile.close();

	} // You should Display the sorted file after merging is done.

	int BinarySearchOnSortedFile(String Sortedfilename, int RecordKey) throws IOException {
		RandomAccessFile searchFile = new RandomAccessFile(Sortedfilename, "rw");
		long low = 0;
		long high = (searchFile.length() / 4) - 1;
		long mid;
		int value = 0;
		while (low <= high) {
			mid = (low + high) / 2;
			searchFile.seek(mid * 4);
			value = searchFile.readInt();
			if (RecordKey < value) {
				high = mid - 1;
			} else if (RecordKey == value) {
				searchFile.close();
				return value;
			} else {
				low = mid + 1;
			}
		}

		searchFile.close();
		return -1;

	}
}
