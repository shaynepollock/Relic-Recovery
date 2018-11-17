package CrownJoules2018;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robocol.Telemetry;

public class Robot {

    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public DcMotor vLiftMotor;  
    public Servo intakeLeft;
    public Servo intakeRight;
    
    public OpMode opMode;
    public Gamepad gamepad1 = opMode.gamepad1;
    public Gamepad gamepad2 = opMode.gamepad2;
    
    public float motorPower = 1;
    
    //Constructor
    public void Robot(HardwareMap hwMap)
    {
        leftMotor = hwMap.get(DcMotor.class, "left_Motors");
        rightMotor = hwMap.get(DcMotor.class, "right_Motors");
        vLiftMotor = hwMap.get(DcMotor.class, "vLift_Motor");  
        intakeLeft = hwMap.get(Servo.class, "intake_Left");
        intakeRight = hwMap.get(Servo.class, "intake_Right");
    
        //set direction of motors  
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        vLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        vLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
} //end class
