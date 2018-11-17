package CrownJoules2018;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
@Disabled
@Autonomous

public class Auto extends LinearOpMode {
    
    //Robot obj
    CrownJoules2018.Robot robot = new CrownJoules2018.Robot();
    
    ///////////////
    //'INIT' Code//
    public void runOpMode()   
    {  
        /////////////
        //Telemetry//
        robot.telemetry.addData("Status", "Initialized");  
        robot.telemetry.update();
        //Telemetry End//
        /////////////////  
        
        robot.waitForStart(); //Gamepad Start Sequence: 'start+a/b'
        
        ///////////////
        //'Play' Code//
        while (robot.opModeIsActive())   
        { 
            //INSERT AUTO CODE HERE
            while (robot.gamepad1.b & robot.gamepad1.y)   
                Methods.test(robot);   
                
          //'Play' Code End//   
        }//////////////////// 
        
     //'INIT' Code End// 
    }///////////////////
}
