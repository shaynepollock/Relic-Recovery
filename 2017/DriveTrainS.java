/* Shayne Pollock */
package CrownJoules2017;   
  
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;  
import com.qualcomm.robotcore.hardware.ColorSensor;  
import com.qualcomm.robotcore.hardware.Servo;  
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;  
import com.qualcomm.robotcore.hardware.Gamepad;  
import com.qualcomm.robotcore.hardware.DcMotor;  
  
@TeleOp  
public class DriveTrainS extends LinearOpMode  
{  
    private DcMotor frontRight;  
    private DcMotor frontLeft;  
    private DcMotor backRight;  
    private DcMotor backLeft;  
    private DcMotor vLiftMotor;  
    private Servo leftClaw_t;
    private Servo leftClaw_b;
    private Servo rightClaw_t;
    private Servo rightClaw_b; 
    private Servo topJewel;
    private Servo bottomJewel;
    public float motorPower =1; 
    
    @Override  
    public void runOpMode()   
    {  
    //when user presses INIT this code will be used 
    
        telemetry.addData("Status", "Initialized");  
        telemetry.update();  
          
        //connects motors to hub & phone- use name in quotes for config  
        frontRight = hardwareMap.get(DcMotor.class, "front_Right");  
        frontLeft  = hardwareMap.get(DcMotor.class, "front_Left");  
        backRight  = hardwareMap.get(DcMotor.class, "back_Right");  
        backLeft   = hardwareMap.get(DcMotor.class, "back_Left");  
        vLiftMotor = hardwareMap.get(DcMotor.class, "vLift_Motor");  
        leftClaw_t  = hardwareMap.get(Servo.class,"topLeft_Claw");  
        rightClaw_t = hardwareMap.get(Servo.class,"topRight_Claw"); 
        leftClaw_b  = hardwareMap.get(Servo.class,"bottomLeft_Claw");  
        rightClaw_b = hardwareMap.get(Servo.class,"bottomRight_Claw");
        topJewel = hardwareMap.get(Servo.class,"top_Jewel");
        bottomJewel = hardwareMap.get(Servo.class,"bottom_Jewel");
        
        //set direction of motors  
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);  
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);  
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        vLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      
        waitForStart(); //this is waiting for play 
        // dont forget the gamepad start sequence (start+a/b)  
      
        //when user presses play this code will be used  
        while (opModeIsActive())   
        {  
            topJewel.setPosition(.195);
            bottomJewel.setPosition(.5);
            
            mecanumDrive();  
              
            pivot();  
            
            vLift();
              
            claw();  

            while (gamepad1.b & gamepad1.y)   
            {  
                test();   
            }
        }//opModeIsActive  
      
    }//runOpMode 
    
    //----------------------------------------------------------------------------------------------
    //Methods
    //----------------------------------------------------------------------------------------------
       
        //weird code for mecanum wheels that gets the angle based on joystick and sets power      
        protected void mecanumDrive()  
        {  
            motorPower = 1 - gamepad1.right_trigger; 
            
            if (motorPower < .40)
            {   
                motorPower= (float) .40;
            }
            
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);  
            double robotAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;  
            double rightX = -gamepad1.right_stick_x;  
            final double FL = r * Math.cos(robotAngle) + rightX;  
            final double FR = r * Math.sin(robotAngle) - rightX;  
            final double BL = r * Math.sin(robotAngle) + rightX;  
            final double BR = r * Math.cos(robotAngle) - rightX;  
            
            frontLeft.setPower(FL* motorPower);  
            frontRight.setPower(FR* motorPower);  
            backLeft.setPower(BL* motorPower);  
            backRight.setPower(BR* motorPower); 
        }     
          
        protected void pivot()  
        {  
            //left pivot  
            while (gamepad1.dpad_right)   
            {  
                frontLeft.setPower(-.5);  
                frontRight.setPower(.5);  
                backLeft.setPower(-.5);  
                backRight.setPower(.5);    
            }  
              
            //right pivot  
            while (gamepad1.dpad_left)   
            {  
                frontLeft.setPower(.5);  
                frontRight.setPower(-.5);  
                backLeft.setPower(.5);  
                backRight.setPower(-.5);  
            }  
        }  
       
        protected void claw()   
        {  
            if (gamepad2.dpad_right) //top in 
            {  
                leftClaw_t.setPosition(.8);  
                rightClaw_t.setPosition(.2);
            }  
            
            if (gamepad2.left_bumper) //top semi out
            {
                leftClaw_t.setPosition(.49);
                rightClaw_t.setPosition(.51);
            }
            
            if (gamepad2.dpad_left)  //top out
            {  
                leftClaw_t.setPosition(.2);  
                rightClaw_t.setPosition(.8);  
            }  
            
            if(gamepad2.right_bumper) //bottom semi out
            {
                leftClaw_b.setPosition(.5);
                rightClaw_b.setPosition(.45);
            }
            
            if(gamepad2.b) //bottom out to channel
            {
                leftClaw_b.setPosition(.56);
                rightClaw_b.setPosition(.4);
            }
            
            if(gamepad2.x) //bottom in
            {
                leftClaw_b.setPosition(.3);
                rightClaw_b.setPosition(.7);
            }
            
            if (gamepad2.dpad_up) //top more out
            {
                leftClaw_t.setPosition(.1);
                rightClaw_t.setPosition(.9);
            }
            
            if (gamepad2.dpad_down) //bottom more out
            {
                leftClaw_b.setPosition(.9);
                rightClaw_b.setPosition(.10);
            } 
        }//end claw()  
          
        protected void vLift()  
        {  
            if(gamepad2.y){ 
                vLiftMotor.setPower(-1);
            }
            
            else if(gamepad2.a){
                vLiftMotor.setPower(1);
            }
            
            else{
                vLiftMotor.setPower(0);
            }
        }  
        
        // protected void slowMode()
        // {
        //     //trigger returns value between 0.0 and +1.0
        //     while (gamepad1.right_trigger >= 0.1) { 
        //         // frontRight.setPower(0.25);
        //         // frontLeft.setPower(0.25);
        //         // backLeft.setPower(0.25);
        //         // backRight.setPower(0.25);
        //     double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);  
        //     double robotAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;  
        //     double rightX = -gamepad1.right_stick_x;  
        //     final double FL = (r * Math.cos(robotAngle) + rightX)*(.45);  
        //     final double FR = (r * Math.sin(robotAngle) - rightX)*(.45);  
        //     final double BL = (r * Math.sin(robotAngle) + rightX)*(.45);  
        //     final double BR = (r * Math.cos(robotAngle) - rightX)*(.45);  
            
        //     frontLeft.setPower(FL);  
        //     frontRight.setPower(FR);  
        //     backLeft.setPower(BL);  
        //     backRight.setPower(BR); 
        //     }
        // }
        
        // tests each motor to see if something is wrong (ie sprockets loose) 
        // must hold down y and b while testing  
        protected void test()   
        {  
            frontLeft.setPower(gamepad1.left_stick_y);  
            frontRight.setPower(gamepad1.right_stick_y);  
            backLeft.setPower(gamepad1.left_stick_x);  
            backRight.setPower(gamepad1.right_stick_x);  
        }  
    
}//end class  

