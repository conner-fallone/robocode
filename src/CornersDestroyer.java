package sample;
import robocode.*;
 
 
/*******************************************************************
 * Authors: Conner Fallone & Corey Alberda
 * Class: CIS-365
 * Winter 2014
 * CornersDestroyer. This robot destroys the sample robot corners.
 *******************************************************************/

public class CornersDestroyer extends AdvancedRobot
{       
		double bearing;
		double velocity;
		double fieldX;
		double fieldY;

		int counter = 5;
       
		boolean inCorner = false;
		boolean justSpotted = true;
       
		ScannedRobotEvent cornersInfo;
       
		public void run() {
			// Get battlefield width and length
			fieldX = getBattleFieldWidth();
			fieldY = getBattleFieldHeight();
			// Go to center of battlefield
			goTo((int)fieldX / 2,(int) fieldY / 2);
			
			// Main robot loop
			while(true) {
				// Get enemies bearing and velocity and square off
				if(cornersInfo != null) {
				
					bearing = cornersInfo.getBearing();
					velocity = cornersInfo.getVelocity();
					
					// Square off against enemy : Turn 90 degrees from him so we will be able to strafe
					setTurnRight(bearing + 90);
					execute();
					
					// Enemy is in corner. We have squared off with him and he is pretty much 90 degrees from us.
					if (((bearing <= -89.9 && bearing >= -90.1) || (bearing <= 90.1 && bearing >= 89.9)) && inCorner == false && velocity == 0){
						inCorner = true;
					// Enemy isn't in corner yet. Keep waiting
					}else{
					// Do Nothing
					}
					
				}
				
				// Opponent is not in a corner. Just keep scanning enemy & strafing
				if (!inCorner){
					ahead(200);
					back(200);
					turnGunRight(360);
				}			
				// Opponent is in a corner, strafe back and fourth and fire
				else {
					// Special case. We want to turn our gun ONCE to aim at him in the corner.
					if (justSpotted){
						turnGunLeft(90);
						justSpotted = false;
					}
					fire(5);
					ahead(200);		
						
					// Stall before strafing back - helps to avoid bullets 
					while (counter > 0){
						ahead(1);
						back(1);
						counter--;
					}
					counter = 5;
					back(200);
				}
			}
		}
		
		/**
		* onScannedRobot: What to do when you see another robot
		*/
		public void onScannedRobot(ScannedRobotEvent e) {
			cornersInfo = e;
			System.out.println("Enemy bearing: " + bearing);
			System.out.println("Enemy velocity: " + velocity);
		}
		
		/**
		 * From RoboWiki
		 * GoTo coordinates. Used for our robot to get to the center of the map.
		 */
		private void goTo(int x, int y) {
    		double a;
    		turnRightRadians(Math.tan(
        	a = Math.atan2(x -= (int) getX(), y -= (int) getY()) 
              	- getHeadingRadians()));
    		ahead(Math.hypot(x, y) * Math.cos(a));
		}
}