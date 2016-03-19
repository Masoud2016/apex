import java.util.Random;
import java.util.concurrent.Semaphore;

public class Process extends Thread {

	private int id;
	private Banker banker;
	private int length;
	private Random r = new Random();
	private int result = -1;
	private int x;

	/*******************************************************************************/
	/****Constructors****/
	
	public Process(int i, Banker b) {
		id = i;
		banker = b;
		length = r.nextInt(15);

	}//END Constructor
	
	public Process() {}

	//int id=(int) pro.getId();
	
	
	public void run() {
		int[] res = new int[banker.getNumberOfResources()];

		for (int i = 0; i < length; i++) {
			try {
				Thread.sleep(r.nextInt(5000));

				/**** Acquire 60% ****/
				if (r.nextInt(100) < 60) {// Assuming that 60% is Acquire and
											// 40% is Release
					for (int j = 0; j < res.length; j++) {
						res[j] = r.nextInt(banker.get_need()[id][j] + 2);// to
	
					}// END for

					result = banker.acquire(res, id);// resource and id

					
					switch (result) {

					case 1:
						banker.releaseAll(banker.get_allocation()[id], id);
						System.out.printf("Process %d\t Acquire\t Test 1 failed\n",id);
						Thread.currentThread().interrupt();//to terminate the process
						break;

					case 2:
						do {
							try {
								System.out.printf("Process %d\t Acquire\t Wait\n",id);
								Thread.sleep(r.nextInt(3000));
							}// END try

							catch (Exception e) {
								System.out.println(" ");
							}// END catch

							result = banker.acquire(res, id);// to update new
																// ones***check
																// again

						}while (result == 2);//END DO-WHILE

						break;

					default:
						System.out.println(" ");

					}// END switch
					
					System.out.printf("Process %d\t Acquire\n",id);

				}// END if 60%

				/**** Release 40% ****/
				else {// 40% release
					for (int j = 0; j < res.length; j++) {
						res[j] = r.nextInt(banker.get_allocation()[id][j] + 1);// to
						//System.out.printf("process %d\t Release\t Granted\n",res[j]);
					}// END for
					banker.release(res, id);
					System.out.printf("process %d\t Release\t Granted\t \n",id);
					//System.out.printf("");


				}// END else 40%
			}// END try

			catch (InterruptedException exception ) 
	         {
	             exception.printStackTrace();
	         }

		}// END for
		// release all
		banker.releaseAll(banker.get_allocation()[id], id);
		
			System.out.printf("process %d\t Release All\t Finished\t ",id);
			for (int i = 0; i < banker.getNumberOfResources(); i++){
				System.out.printf(" %d ",banker.get_available()[i]);	
				}
			System.out.println();
		System.out.println("Safe");
		
		
	}// END run	

}// END class
