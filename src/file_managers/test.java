package file_managers;

public class test {
	public static void main(String [] args) {
		// Depotes
		String state = "(at pallet10 depot0) "
				+ "(at pallet1 distributor0) "
				+ "(at hoist0 depot0) "
				+ "(at hoist1 distributor10) "
				+ "(available hoist0) "
				+ "(available hoist1) "
				+ "(at truck0 depot0) "
				+ "(at truck1 depot1) "
				+ "(at crate0 depot1) "
				+ "(at crate3 depot1) "
				+ "(on crate0 pallet1) "
				+ "(on crate3 crate0) "
				+ "(clear crate3)";
		FileManager mng = new DepotsFileManager(null, null, null);
		String str = mng.getShortRepresentation(state);
		System.out.println("Depots");
		System.out.println(state);
		System.out.println(str + "\n");
		
		// Campus
		state = "bank watson_theater hayman_theater davis_theater jones_theater " +
				"bookmark_cafe library cbs psychology_bldg angazi_cafe tav " +
				"(banking) (lecture-1-taken) (lecture-2-taken) (lecture-3-taken) " +
				"(lecture-4-taken) (group-meeting-1) (group-meeting-2) (group-meeting-3) " +
				"(coffee) (breakfast) (lunch)";
				
		mng = new CampusFileManager(null, null, null);
		str = mng.getShortRepresentation(state);
		System.out.println("Campus");
		System.out.println(state);
		System.out.println(str + "\n");		

		// Logistics.
		mng = new LogicticsFileManager(null, null, null);
		state = "(at airplane1 airport2) (at truck1 pos11) (at truck2 pos22) " +
				"(at obj11 pos11) (at obj12 pos12) (at obj13 pos13) " +
				"(in-city airport1 city1) (in-city pos11 city1) (in-city pos12 city1) (in-city pos13 city1) " +
				"(in-city airport2 city2)";
		str = mng.getShortRepresentation(state);
		System.out.println("Logictics");
		System.out.println(state);
		System.out.println(str + "\n");

		// Driver's log
		mng = new DriverlogFileManager(null, null, null);
		state = "(at driver1 s1) (DRIVER driver1) (at driver2 s0) " +
			  "(DRIVER driver2) (at driver3 s0) (DRIVER driver3) " +
			  "(at truck1 s1) (empty truck1) (TRUCK truck1) " +
			  "(at truck2 s1) (empty truck2) (TRUCK truck2) " +
			  "(at package1 s0) (OBJ package1) (at package2 s0) " +
			  " (OBJ package2) (at package3 s2) (OBJ package3) " +
			  "(at package4 s2) (OBJ package4) (at package5 s1) " +
			  "(OBJ package5) (LOCATION s0) (LOCATION s1) " +
			  "(LOCATION s2) (LOCATION p0-1) (LOCATION p0-2) " +
			  "(LOCATION p1-2) (path s0 p0-1) (path p0-1 s0) " +
			  "(path s1 p0-1) (path p0-1 s1) (path s0 p0-2) " +
			  "(path p0-2 s0) (path s2 p0-2) (path p0-2 s2) " +
			  "(path s1 p1-2) (path p1-2 s1) (path s2 p1-2) " +
			  "(path p1-2 s2) (link s0 s1) (link s1 s0) " +
			  "(link s0 s2) (link s2 s0) (link s1 s2) (link s2 s1)";
		str = mng.getShortRepresentation(state);
		System.out.println("Drivers log");
		System.out.println(state);
		System.out.println(str + "\n");
		
		//WDR
		mng = new DWRFileManager(null, null, null);
		state = "(adjacent l1 l2) "
				+ "(adjacent l2 l1) (attached p1 l1) (attached q1 l1) "
				+ "(attached p2 l2) (attached q2 l2) (belong k1 l1) "
				+ "(belong k2 l2) (in ca p1) (in cb p1) (in cc p1) "
				+ "(in cd q1) (in ce q1) (in cf q1) (on ca pallet) "
				+ "(on cb ca) (on cc cb) (on cd pallet) (on ce cd) "
				+ "(on cf ce) (top cc p1) (top cf q1) (top pallet p2) "
				+ "(top pallet q2) (at r1 l1) (unloaded r1) "
				+ "(occupied l1) (empty k1) (empty k2)";
		str = mng.getShortRepresentation(state);
		System.out.println("dwr");
		System.out.println(state);
		System.out.println(str + "\n");
		
		// Easy IPC
		mng = new EasyIpcFileManager(null, null, null);
		state = "(at-robot place_0_0) (conn place_0_0 place_0_1) (conn place_0_0 place_1_0) "
			  + "(open place_0_4) (locked place_0_5) (lock-shape place_0_5 shape_7) "
			  + "(at key_0 place_0_0) (key-shape key_0 shape_0)";
		str = mng.getShortRepresentation(state);
		System.out.println("easy ipc");
		System.out.println(state);
		System.out.println(str + "\n");
		
		// Ferry
		mng = new FerryFileManager(null, null, null);
		state = "(location l0) (car c0) (not-eq l0 l1) (not-eq l1 l0) (not-eq l0 l2) (empty-ferry) (at car0 location0) "
				+ "(at car3 location0) (at-ferry l2)";
		str = mng.getShortRepresentation(state);
		System.out.println("ferry");
		System.out.println(state);
		System.out.println(str + "\n");
	}
}
