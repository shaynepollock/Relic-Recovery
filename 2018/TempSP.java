package CrownJoules2018;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;  
import com.qualcomm.robotcore.hardware.Servo;  
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;  
import com.qualcomm.robotcore.hardware.Gamepad;  
import com.qualcomm.robotcore.hardware.DcMotor; 

@TeleOp

public class TempSP extends LinearOpMode{

    private DcMotor frontRight;  
    public DcMotor leftMotors;
    public DcMotor rightMotors;
    public DcMotor vLiftMotor; 
    public float motorPower =1; 
    
    public Servo intakePivotServo;
    public float intakePivotServoPos = 0; //What the servo above's current pos is; Sets to this at start
    public float intakePivotSensitivity = 0.18f;
    
    public DcMotor liftPivotMotor;
    public float liftPivotServoPos = 0; //What the motor above's current pos is; Sets to this at start
    public float liftPivotSensitivity = 0.18f; 
    
    public boolean isIntakeActive;
    
    @Override  
    public void runOpMode()   
    {  
        //connects motors to hub & phone- use name in quotes for config  
        leftMotors = hardwareMap.get(DcMotor.class, "left_Motors");
        rightMotors = hardwareMap.get(DcMotor.class, "right_Motors");
        vLiftMotor = hardwareMap.get(DcMotor.class, "vLift_Motor");  
        
        leftMotors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotors.setDirection(DcMotor.Direction.REVERSE);
        rightMotors.setDirection(DcMotor.Direction.FORWARD);
        vLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        vLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      
        waitForStart(); //press play button, actives opMode
        intakePivotServo.setPosition(intakePivotServoPos);
        while (opModeIsActive())   
        {  
            drive();
            pivotIntake();
            pivotLift();
            
        }//opModeIsActive  
      
    }//runOpMode 
    
    public void drive()
    {
        leftMotors.setPower(gamepad1.left_stick_y);
        rightMotors.setPower(gamepad1.right_stick_y);
    }
    
    public void pivotIntake()
    {
        while(gamepad1.dpad_down)
        {
            intakePivotServoPos -= intakePivotSensitivity;
            intakePivotServo.setPosition(intakePivotServoPos);
        }
        while(gamepad1.dpad_up)
        {
            intakePivotServoPos += intakePivotSensitivity;
            intakePivotServo.setPosition(intakePivotServoPos);   
        }
    }
    
    public void toggleIntake()
    {
        if(gamepad1.x)
        {
            isIntakeActive = !isIntakeActive; 
            //switch(isIntakeActive)
            //Turn motors on if on, else turn motors off
        }
    }
    
    public void pivotLift()
    {
        while(gamepad1.left_bumper)
        {
            if(liftPivotServoPos > 0)
            {
                liftPivotServoPos -= liftPivotSensitivity;
                intakePivotServo.setPosition(liftPivotServoPos);   
            }
        }
        while(gamepad1.right_bumper)
        {
            if(liftPivotServoPos < 1)
            {
                liftPivotServoPos += liftPivotSensitivity;
                liftPivotServo.setPosition(liftPivotServoPos);     
            }
        }
    }
}
