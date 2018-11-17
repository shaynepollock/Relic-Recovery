package CrownJoules2018;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;  
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;  
import com.qualcomm.robotcore.hardware.Servo;  
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;  
import com.qualcomm.robotcore.hardware.Gamepad;  
import com.qualcomm.robotcore.hardware.DcMotor;

public class GabeRobot {
    
    /* public DcMotor frontRight;  
    public DcMotor frontLeft;  
    public DcMotor backRight;  
    public DcMotor backLeft;  
    */
    
    public DcMotor leftMotors;
    public DcMotor rightMotors;
    public DcMotor vLiftMotor;  
    
    public Servo intakeLeft;
    public Servo intakeRight;
    
    public float motorPower = 1;
    
    //Constructor
    public void Robot()
    {
        //connects motors to hub & phone- use name in quotes for config  
    /*  frontRight = hardwareMap.get(DcMotor.class, "front_Right");  
        frontLeft  = hardwareMap.get(DcMotor.class, "front_Left");  
        backRight  = hardwareMap.get(DcMotor.class, "back_Right");  
        backLeft   = hardwareMap.get(DcMotor.class, "back_Left");  
    */ 
        leftMotors   = hardwareMap.get(DcMotor.class, "left_Motors");
        rightMotors   = hardwareMap.get(DcMotor.class, "right_Motors");
        vLiftMotor = hardwareMap.get(DcMotor.class, "vLift_Motor");  
        intakeLeft = hardwareMap.get(Servo.class, "intake_Left");
        intakeRight = hardwareMap.get(Servo.class, "intake_Right");
        
        //set direction of motors  
        /* frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);  
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); */
        leftMotors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        /* backRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);  
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); */
        leftMotors.setDirection(DcMotor.Direction.REVERSE);
        rightMotors.setDirection(DcMotor.Direction.FORWARD);
        vLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        vLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
} //end class
