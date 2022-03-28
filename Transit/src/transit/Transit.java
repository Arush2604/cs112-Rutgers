package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {

		// UPDATE THIS METHOD
		TNode walkZero = new TNode();
		TNode busZero = new TNode(0, null, walkZero);

		trainZero = new TNode(0, null, busZero);

		int l = locations.length;

		TNode ptrTrain  = trainZero;
		TNode ptrBus  = busZero;
		TNode ptrWalk  = walkZero;

		for(int ixw = 0, ixb = 0, ixt = 0; ixw < l; ixw ++) { //walk bus and train index

			int station = locations[ixw];
			TNode current = new TNode(station);
			ptrWalk.setNext(current);

			if(ixb < busStops.length && busStops[ixb] == station) {
				TNode currBus = new TNode(station, null, current);
				ptrBus.setNext(currBus);
				ptrBus = currBus;
				ixb++;

			}

			if(ixt < trainStations.length && trainStations[ixt] == station) {
				TNode currTrain = new TNode(station, null, ptrBus);
				ptrTrain.setNext(currTrain);
				ptrTrain = currTrain;
				ixt ++;
			}
			ptrWalk = current;
		}
	}
				

	


	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {

	    // UPDATE THIS METHOD
		TNode current = trainZero.getNext();
		TNode ptr = trainZero;

		while(current != null){
			if (current.getLocation() == station){
				ptr.setNext(current.getNext());
			} 

			ptr = current;
			current = current.getNext();
		} //simply deleting a node in a linked list 
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {

	    // UPDATE THIS METHOD
		TNode busstops = trainZero.getDown();
        TNode walkstops = busstops.getDown();

        while(walkstops.getNext()!=null && walkstops.getLocation()!=busStop)
        {
            walkstops = walkstops.getNext();
            if (busstops.getNext()!=null && walkstops.getLocation()==busstops.getNext().getLocation()) busstops = busstops.getNext();
        }
        if (walkstops.getLocation()<busStop || busstops.getLocation()==busStop)
        {
            return;
        } 

        TNode newBusStop = new TNode(busStop,busstops.getNext(), walkstops);

        busstops.setNext(newBusStop);
	}
	
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {

	    // UPDATE THIS METHOD
	    ArrayList<TNode> path = new ArrayList<>();
		TNode current = trainZero;

		while(current!=null && current.getLocation() <= destination){
			path.add(current);
			current = current.getNext();
		}

		ArrayList<TNode> tpath = path;
		current = tpath.get(tpath.size()-1).getDown();
		while(current!=null && current.getLocation() <= destination){
			path.add(current);
			current = current.getNext();
		}

		tpath = path;
		current = tpath.get(tpath.size()-1).getDown();

		while(current!=null && current.getLocation() <= destination){
			path.add(current);
			current = current.getNext();
		}

		return path;
	}


	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

		TNode first = new TNode();
        TNode tfirst = first;
        TNode t0 = trainZero;
        TNode tTemp;
        TNode Temp;

        while(t0 != null)
        {
            Temp = tfirst;
            tTemp = t0.getNext();
            while(tTemp != null)
            {
                Temp.setNext(new TNode(tTemp.getLocation()));
                Temp = Temp.getNext();
                tTemp = tTemp.getNext();
            }
            t0 = t0.getDown();
            if(t0 != null)
            {
                tfirst.setDown(new TNode(t0.getLocation()));
                tfirst = tfirst.getDown();
            }
        }
        TNode newTC;
        tfirst = first;
        while(tfirst.getDown() != null)
        {
            Temp = tfirst;
            newTC = tfirst.getDown();
            while(Temp != null)
            {
                while(newTC.getLocation() < Temp.getLocation())
                {
                    newTC = newTC.getNext();
                }
                Temp.setDown(newTC);
                Temp = Temp.getNext();
            }
            tfirst = tfirst.getDown();
        }
        return first;

	    
		




		
        }

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {

	    // UPDATE THIS METHOD
		TNode stops = trainZero.getDown().getDown();
		TNode bus = trainZero.getDown();
		TNode scooter = new TNode(0, null, bus.getDown());

		TNode traindown = trainZero.getDown();
		traindown.setDown(scooter);
		trainZero.setDown(traindown);

		bus = bus.getNext();
		stops = stops.getNext();

		int counter = 0;
		while(counter < scooterStops.length && stops != null) {
			if (stops.getLocation() == scooterStops[counter]) {
				//scooter = scooter.getNext();
				TNode next = new TNode(scooterStops[counter], null, stops);
				scooter.setNext(next);
				if (bus != null && bus.getLocation() == scooterStops[counter]) {
					bus.setDown(scooter.getNext());
					bus = bus.getNext();
				}
				scooter = scooter.getNext();
				counter++;
			}
			stops = stops.getNext();
		}
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
