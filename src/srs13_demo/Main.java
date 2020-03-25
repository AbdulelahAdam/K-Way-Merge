package srs13_demo;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {

	public static final String Inputfilename = "Index.bin";
	public static final String mergeOutputFileName = "sortedMerger.bin";

	public static void main(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile(Inputfilename, "rw");
		RandomAccessFile file2 = new RandomAccessFile(mergeOutputFileName, "rw");
		Task a1 = new Task();

		/* ABDULELAH ADAM-20170416 */
		String[] test = a1.DivideInputFileIntoRuns(Inputfilename, 16);
		String[] test2 = a1.SortEachRunOnMemoryAndWriteItBack(test);

		/* ALI GAD-20170362 */
		a1.DoKWayMergeAndWriteASortedFile(test2, 4, mergeOutputFileName);

		/* ALI GAD-20170362 & ABDULELAH ADAM-20170416 */
		int num = a1.BinarySearchOnSortedFile(mergeOutputFileName, 39);
		System.out.println(num);
		file.close();
		file2.close();

	}

}
