package srs13_demo;

public class KMerge {

	/** Input Array */
	int[] numArr;

	/** Something like temp Array */
	int[] helperArr;

	public void sort(int[] numArr, int kPartitions) {
		this.numArr = numArr;
		this.helperArr = new int[this.numArr.length];
		for (int i = 0; i < numArr.length; i++) {
			this.helperArr[i] = this.numArr[i];
		}
		KWayMergeSort(kPartitions, 0, this.numArr.length - 1);
	}

	public String toString(int[] arr) {
		String out = "";
		int i;
		for (i = 0; i < arr.length; i++) {
			out += arr[i] + "\n";
		}
		return out;
	}

	private void KWayMergeSort(int kPartitions, int startPart, int endPart) {
		/** Length of boundary (n) */
		int totalSize = endPart - startPart + 1;
		int sizePartitions = Math.max(totalSize / kPartitions, 1);
		int sizeEndPartition = totalSize - (sizePartitions * (kPartitions - 1));

		/** We must recurse until the size of each partition is 1 */
		if (sizePartitions > 1) {
			for (int i = 0; i < kPartitions; i++) {

				int newEndPart;
				if (i == kPartitions - 1) {
					newEndPart = i * sizePartitions + startPart + sizeEndPartition - 1;
				} else {
					newEndPart = i * sizePartitions + startPart + sizePartitions - 1;
				}
				/** Recurse for each partition */
				KWayMergeSort(kPartitions, i * sizePartitions + startPart, newEndPart);
			}

		} else {
			if (sizeEndPartition > 1) {
				KWayMergeSort(kPartitions, endPart - sizeEndPartition, endPart);
			}
		}
		/** Once we are done recursing, start merging partitions. */
		KWayMerge(kPartitions, startPart, endPart);

	}

	private void KWayMerge(int kPartitions, int low, int high) {

		int totalSize = high - low + 1;
		int sizePartitions = Math.max(totalSize / kPartitions, 1);
		int sizeLastPartition = totalSize - sizePartitions * (kPartitions - 1);

		if (totalSize < kPartitions) {
			KWayMerge(totalSize, low, high);
			return;
		}

		int[] indices = new int[kPartitions];

		int count = low;
		int min;
		int minPosition;
		int currentPart;

		while (count <= high) {

			min = Integer.MAX_VALUE;
			minPosition = 0;
			currentPart = low;
			for (int i = 0; i < kPartitions; i++) {

				if (i == kPartitions - 1 && indices[i] == sizeLastPartition) {
					break;
				}

				if (indices[i] == sizePartitions && i != kPartitions - 1) {
					currentPart += sizePartitions;
					continue;
				}

				if (helperArr[currentPart + indices[i]] < min) {
					min = helperArr[currentPart + (indices[i])];
					minPosition = i;
				}

				/** Move to next partition */
				currentPart += sizePartitions;

			}
			/* Update numArr and move the counter over. */
			numArr[count++] = min;
			/** Update the index of the partition */
			indices[minPosition]++;
		}

		/** Copy changes from numArr into helperArr okay man! */
		for (int i = low; i <= high; i++) {
			helperArr[i] = numArr[i];

		}
	}
}
