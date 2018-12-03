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
    public float motorPower = 0.3f;

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
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        drawerMotor.setDirection(DcMotor.Direction.REVERSE);
        drawerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftPivotMotor.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
        drawerMotor.setPower(-1);

        intakePivotServo.setPosition(intakePivotServoPos);

        liftPivotMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftPivotMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drawerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drawerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/

        waitForStart(); //press play button, actives opMode

        while (opModeIsActive())   
        {  
            drive();
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

        leftMotor.setPower(-(gamepad1.left_stick_x + gamepad1.left_stick_y) * motorPower);
        rightMotor.setPower(-(gamepad1.left_stick_x + -gamepad1.left_stick_y) * motorPower);

        telemetry.addData("Left Motor: ", leftMotor.getPower());
        telemetry.addData("Right Motor: ", rightMotor.getPower());
        telemetry.update();

        if(gamepad1.left_bumper)
            motorPower = 0.85f;
        else if(gamepad1.right_bumper)
            motorPower = 0.55f;
    }

    public void pivotIntake()
    {
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
        liftPivotMotor.setPower(gamepad2.right_stick_y / liftPivotSensitivity);

        if (gamepad2.x) {
            liftPivotMotor.setPower(0.8);
            sleep(700);
            liftPivotMotor.setPower(0);
            sleep(3000);
            liftPivotMotor.setPower(-0.4);
            sleep(100);
            liftPivotMotor.setPower(0);

            liftPivotMotor.setTargetPosition(1120); //Positive = spins right?

        } else if (gamepad2.y) {
            liftPivotMotor.setTargetPosition(-1120); //Positive = spins right?        }
        }
    }
    public void toggleDrawerSlides()
    {
        if(gamepad1.dpad_up)
        {
            drawerMotor.setTargetPosition(-1000);
            drawerMotor.setPower(1);
        }
        if(gamepad1.dpad_down)
        {
            drawerMotor.setTargetPosition(1000);
            drawerMotor.setPower(-1);
        }
        //i`f(gamepad1.dpad_left)
        //    drawerMotor.setPower(0);
        //if(!gamepad1.dpad_down && !gamepad1.dpad_up)
          //  drawerMotor.setPower(0);
        //telemetry.addData("in toggleDrawerSlides", drawerMotor.getPower());
        telemetry.update();
    }
}
