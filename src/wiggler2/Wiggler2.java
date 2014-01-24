package wiggler2;

import java.awt.MouseInfo;
import java.awt.Robot;




public class Wiggler2 {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		wiggler( args );
		
		// foo( String.class );
		
		// new CodeTest();
	}
	
	private static void wiggler( String[] args ) {

		System.out.println( "Getting my wiggle on..." );
		Robot robot;
		try {
			robot = new Robot();
			int		baseX = (int) MouseInfo.getPointerInfo().getLocation().getX();
			int		baseY = (int) MouseInfo.getPointerInfo().getLocation().getY();
			int		idleDelay = 60;	// in seconds
			int		timer = 0;
			int 	timeToStop = 0;
			boolean debug = false;
			
			CmdLine.load( args );
			if ( CmdLine.get( "debug" ) != null){
				debug = true;
				System.out.println( "debug set to [" + (debug ? "true" : "false") + "]");					
			}
			if ( (idleDelay = Integer.parseInt(CmdLine.get( "interval" ))) > 0 ) {
				if (debug)
					System.out.println( "interval set to " + idleDelay );					
			}
			if ( (timeToStop = Integer.parseInt(CmdLine.get( "stoptime" ))) > 0){
				if (debug)
					System.out.println( "stop time set to " + timeToStop );					
			}
			
			
			
//			Getopt g = new Getopt("wiggler", args, "i:t:"); //-D to enable debug log
//			while((opt = g.getopt()) != -1) {
//			    switch(opt){
//			        case 'i':								//  interval between wiggles
//			        	idleDelay = g.getOPtArg();
//			            break;
//			        case 't':								// time to stop wiggling
//			            timeToStop = g.getOptArg();                    
//			            break;
//			        case ':':                               /* error - missing operand */
//			            fprintf(stderr, "Option -%c requires an operand\n", optopt);
//			            break;
//			        case '?':                               /* error - unknown option */
//			            fprintf(stderr,"Unrecognized option: -%c\n", optopt);
//			            break;
//			    }
//			}
			
//			if (args.length > 0 ) {
//				try {
//					idleDelay = Integer.parseInt( args[0] );
//				}
//				catch ( Exception e ) {
//					System.out.println( "Ignoring idleDelay parameter: " + args[0] + ", use an integer next time" );
//				}
//			}
//			System.out.println( "Using idle delay: " + idleDelay );
			
			while ( true ) {
				robot.delay( 1000 );
				timer++;	// timer is in seconds

				int curX = (int) MouseInfo.getPointerInfo().getLocation().getX();
				int curY = (int) MouseInfo.getPointerInfo().getLocation().getY();
				
				// movement idle timer
				if ( timer < idleDelay ) {
					// if the mouse has moved, update base, reset timer
					if ( curX != baseX  ||  curY != baseY ) {
						baseX = curX;
						baseY = curY;
						timer = 0;
						if (debug)
							System.out.println( "idle timer reset" );
					}
					continue;
				}

				if (debug)
					System.out.println( "wiggling" );
				
				// beyond the timer, wiggle
				int	endX = baseX + (int) (15f * (Math.random()-.5f));
				int	endY = baseY + (int) (15f * (Math.random()-.5f));
				
				for ( int i=0 ; i<100 ; i++ ){  
				    int x = ((endX * i)/100) + (baseX*(100-i)/100);
				    int y = ((endY * i)/100) + (baseY*(100-i)/100);
				    robot.mouseMove( x, y );
				    robot.delay( 5 );
				}				
			    
			    // then move back to the origin
			    robot.mouseMove( baseX, baseY );
			    
			    // reset the timer to 70% and let the idle loop continue
			    timer = (int) (idleDelay * .7f);
			}
		} 
		catch ( Exception e ) {
			System.out.println( "Wiggler: " + e.toString() );
		}
		
	}

	/*
	public static void foo( Class ... cl ) {
		System.out.println( cl );
		String	bar = "wade";
		for ( Class c : cl ) {
			// if ( bar instanceof cl )
			System.out.println( c );
			System.out.println( bar.getClass() );
			if ( c.isAssignableFrom( bar.getClass() ) )
				System.out.println( "woo" );
		}
	}

	public CodeTest() {
		try {
			BlockingQueue	queue = new LinkedBlockingQueue();
			
			LinkedBlockingQueue	queue2 = new LinkedBlockingQueue();

			// create each thread
			Thread	t1, t2;
			t1 = new Consumer( queue );
			t2 = new Consumer( queue );
			
			for ( int i=0 ; i<50000 ; i++ ) {
				queue.offer( (int) (Math.random() * 1000) );
			}
			queue.offer( -1 );
			queue.offer( -1 );

			long start = System.nanoTime();
			t1.start();
			t2.start();
			while ( t1.isAlive() && t2.isAlive() ) Thread.sleep( 25 );
			System.out.println( "Run nanos: " + (System.nanoTime() - start) );					
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}	

	private class Consumer extends Thread {
		BlockingQueue	queue;
		
		public Consumer( BlockingQueue bq ) {
			queue = bq;
		}
		
		@Override
		public void run() {
			long	threadId = Thread.currentThread().getId();
			
			while ( true ) {
				Integer i, last = 0, timeout = 1;
				try {
					i = (Integer) queue.take();
					if ( i == -1 ) return;
					
					if ( !(i % 2 == threadId % 2) ) {
						if ( i == last )
							timeout += 2;
						else
							timeout = 1;
						
						queue.offer( i );
						Thread.sleep( timeout );
						
						// System.out.println( "Thread: " + threadId + " got " + i );						
					}
				}
				catch ( InterruptedException e ) {}				
			}
		}
	}
	*/
}
