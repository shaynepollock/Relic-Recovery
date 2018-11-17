package CrownJoules2018;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode; 

@TeleOp

public class Manual extends LinearOpMode{
    
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
        robot.waitForStart(); //Gamepad Start Sequence: start+a/b 
        
        ///////////////
        //'Play' Code//
        while (robot.opModeIsActive())   
        {  
            //Methods.mecanumDrive(robot);  
            Methods.drive(robot);  
            //pivot();  
            
            //vLift(); Commented until needed
            
            //claw(); 
            
            while (robot.gamepad1.b & robot.gamepad1.y)   
                Methods.test(robot);   
        
        //'Play' Code End//   
        }//////////////////
        
    //'INIT' Code End// 
    }///////////////////
}
