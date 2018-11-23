package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;  
import com.qualcomm.robotcore.hardware.Gamepad;  
import com.qualcomm.robotcore.hardware.DcMotor; 

@TeleOp

public class TempSP extends LinearOpMode{

    private DcMotor frontRight;  
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public DcMotor drawerMotor;
    public float motorPower = 0.55f;

    public Servo intakePivotServo;
    public float intakePivotServoPos = 0.5f; //What the servo above's current pos is; Sets to this at start
    public float intakePivotSensitivity = 500;
    
    public DcMotor liftPivotMotor;
    public float liftPivotServoPos = 0; //What the motor above's current pos is; Sets to this at start
    public float liftPivotSensitivity = 2;
    private boolean liftLocked; //If true, the lift stays in place
    private float liftLockPower = 0.5f; //What the motors's power needs to be to stay in place holding our lift's weight

    public boolean isIntakeActive;
    public CRServo intakeServo1;
    public CRServo intakeServo2;
    @Override
    public void runOpMode()   
    {

        //connects motors to hub & phone- use name in quotes for config  
        leftMotor = hardwareMap.get(DcMotor.class, "left_Motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_Motor");
        drawerMotor = hardwareMap.get(DcMotor.class, "drawer_Motor");
        liftPivotMotor= hardwareMap.get(DcMotor.class, "pivot_Motor");
        intakeServo1 = hardwareMap.get(CRServo.class, "leftVexMotor");
        intakeServo2 = hardwareMap.get(CRServo.class, "rightVexMotor");
        intakePivotServo = hardwareMap.get(Servo.class, "intakePivotServo");

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        drawerMotor.setDirection(DcMotor.Direction.REVERSE);
        drawerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftPivotMotor.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
        //drawerMotor.setPower(-1);
        waitForStart(); //press play button, actives opMode

        intakePivotServo.setPosition(intakePivotServoPos);

        while (opModeIsActive())   
        {  
            drive();
            slowDrive();
            pivotIntake();
            pivotLift();
            toggleIntake();
            toggleDrawerSlides();
            telemetry.addData("motorPower", motorPower);
        }//opModeIsActive
      
    }//runOpMode 
    
    public void drive()
    {
        leftMotor.setPower(gamepad1.left_stick_y * motorPower);
        rightMotor.setPower(gamepad1.right_stick_y * motorPower);

        if(gamepad1.left_bumper)
            motorPower = 1;
        else if(gamepad1.right_bumper)
            motorPower = 0.55f;
    }

    public void slowDrive()
    {
        // add something here so that we can go slow while in to drop off shit
        // while (gamepad2.)
        //leftMotor.setPower(gamepad1.left_stick_y);

    }
    public void pivotIntake()
    {
        /*if(gamepad1.dpad_down)
        {
            intakePivotServoPos -= intakePivotSensitivity;
            intakePivotServo.setPosition(intakePivotServoPos);
        }
        if(gamepad1.dpad_up)
        {
            intakePivotServoPos += intakePivotSensitivity;
            intakePivotServo.setPosition(intakePivotServoPos);   
        }
        if(gamepad2.left_stick_y > 0.1 || gamepad2.left_stick_y < -0.1)
        {
            intakePivotServo.setPosition(intakePivotServo.getPosition()+(gamepad2.left_stick_y / 5));
        }*/

        //hey its shayne. This is obviously stupid but in case we can't get fancy stuff to work.
        /*if(gamepad2.dpad_up) //up
            intakePivotServo.setPosition(1);
        if(gamepad2.dpad_down)//down
            intakePivotServo.setPosition(0);
        if(gamepad2.dpad_right)//middle
            intakePivotServo.setPosition(.5); */

        /*while(gamepad2.left_stick_y) //or if()... feel like if you do while then code is in that loop and you cant move robot at same time
            intakePivotServo.setPosition(intakePivotServo.getPosition()+.1);*/
        intakePivotServo.setPosition(intakePivotServo.getPosition() + (gamepad2.left_stick_y / intakePivotSensitivity));
        telemetry.addData("IntakePosition", intakePivotServo.getPosition());
        }
    
    public void toggleIntake()
    {
        if(gamepad2.y)
        {
            telemetry.addData("gp2_y", intakeServo1.getPower());
            telemetry.update();
            intakeServo1.setPower(0.7);
            intakeServo2.setPower(-0.7);
        }
        if(gamepad2.b)
        {
            telemetry.addData("gp2_b", intakeServo1.getPower());
            telemetry.update();
            intakeServo1.setPower(0);
            intakeServo2.setPower(0);
        }
        if(gamepad2.a)
        {
            intakeServo1.setPower(-0.7);
            intakeServo2.setPower(0.7);
        }
    }
    
    public void pivotLift() {
        /*if (gamepad1.left_bumper) {
            if (liftPivotServoPos > -1) {
                liftPivotServoPos -= liftPivotSensitivity;
                liftPivotMotor.setPower(liftPivotServoPos);
            }
        }
        if (gamepad1.right_bumper) {
            if (liftPivotServoPos < 1) {
                liftPivotServoPos += liftPivotSensitivity;
                liftPivotMotor.se
         }*/
        //if(!liftLocked)
        //if(liftPivotMotor.getCurrentPosition())

        liftPivotMotor.setPower(gamepad2.right_stick_y / liftPivotSensitivity);

        if(gamepad2.x)
        {
            liftPivotMotor.setPower(0.8);
            sleep(700);
            liftPivotMotor.setPower(0);
            sleep(3000);
            liftPivotMotor.setPower(-0.4);
            sleep(100);
            liftPivotMotor.setPower(0);

        }
        /*if(gamepad1.b && !liftLocked)
        {
            liftPivotMotor.setPower(liftLockPower);
            liftLocked = true;
        }
        if(gamepad1.a && liftLocked)
        {
            liftLocked = false;
        }*/
    }

    public void toggleDrawerSlides()
    {

        if(gamepad1.dpad_up)
            drawerMotor.setPower(1);
        if(gamepad1.dpad_down)
            drawerMotor.setPower(-1);
        if(gamepad1.dpad_left)
            drawerMotor.setPower(0);
        //if(!gamepad1.dpad_down && !gamepad1.dpad_up)
          //  drawerMotor.setPower(0);
        telemetry.addData("in toggleDrawerSlides", drawerMotor.getPower());
        telemetry.update();

    }
}
